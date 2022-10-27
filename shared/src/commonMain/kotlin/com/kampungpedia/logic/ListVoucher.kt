package com.kampungpedia.logic

import com.kampungpedia.data.VoucherRepository
import com.kampungpedia.data.network.voucher.VoucherType
import com.kampungpedia.data.network.voucher.model.ListVoucherResponse
import com.kampungpedia.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ListVoucherState(
    val isProgress: Boolean,
    val data: ListVoucherResponse?
) : State

sealed class ListVoucherAction : Action {
    data class Refresh(val forceRefresh: Boolean) : ListVoucherAction()
    data class Data(val data: ListVoucherResponse) : ListVoucherAction()
    data class Error(val exception: Exception) : ListVoucherAction()
}

sealed class ListVoucherEffect : Effect {
    data class Error(val exception: Exception) : ListVoucherEffect()
}

class ListVoucher(
    private val repository: VoucherRepository
) : Store<ListVoucherState, ListVoucherAction, ListVoucherEffect>,
    CoroutineScope by CoroutineScope(Dispatchers.Main) {
    private val state = MutableStateFlow(ListVoucherState(false, null))
    private val effect = MutableSharedFlow<ListVoucherEffect>()

    override fun observeState(): StateFlow<ListVoucherState> =
    state

    override fun observeSideEffect(): Flow<ListVoucherEffect> =
        effect

    override fun dispatch(action: ListVoucherAction) {
        val oldState = state.value

        val newState = when(action){
            is ListVoucherAction.Data -> {
                if (oldState.isProgress){
                    ListVoucherState(false, action.data)
                }else{
                    launch { effect.emit(ListVoucherEffect.Error(UnexpectedException())) }
                    oldState
                }
            }
            is ListVoucherAction.Error -> {
                if (oldState.isProgress){
                    launch { effect.emit(ListVoucherEffect.Error(action.exception)) }
                    ListVoucherState(false, oldState.data)
                }else{
                    launch { effect.emit(ListVoucherEffect.Error(UnexpectedException())) }
                    oldState
                }
            }
            is ListVoucherAction.Refresh -> {
                if (oldState.isProgress){
                    launch { effect.emit(ListVoucherEffect.Error(LoadingException())) }
                    oldState
                }else{
                    launch { loadData(action.forceRefresh) }
                    oldState.copy(isProgress = true)
                }
            }
        }

        if (newState != oldState){
            state.value = newState
        }
    }

    private suspend fun loadData(forceRefresh: Boolean){
        try {
            val data = repository.getVoucher()
            dispatch(ListVoucherAction.Data(data))
        }catch (e: Exception){
            dispatch(ListVoucherAction.Error(e))
        }
    }
}