package com.kampungpedia.logic

import com.kampungpedia.data.VoucherRepository
import com.kampungpedia.data.network.voucher.model.ListGamesResponse
import com.kampungpedia.data.network.voucher.model.ListVoucherResponse
import com.kampungpedia.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ListGamesState(
    val isProgress: Boolean,
    val data: ListGamesResponse?
) : State

sealed class ListGamesAction : Action {
    data class Refresh(val forceRefresh: Boolean) : ListGamesAction()
    data class Data(val data: ListGamesResponse) : ListGamesAction()
    data class Error(val exception: Exception) : ListGamesAction()
}

sealed class ListGamesEffect : Effect {
    data class Error(val exception: Exception) : ListGamesEffect()
}

class ListGames(
    private val repository: VoucherRepository
) : Store<ListGamesState, ListGamesAction, ListGamesEffect>,
    CoroutineScope by CoroutineScope(Dispatchers.Main) {
    private val state = MutableStateFlow(ListGamesState(false, null))
    private val effect = MutableSharedFlow<ListGamesEffect>()

    override fun observeState(): StateFlow<ListGamesState> =
        state

    override fun observeSideEffect(): Flow<ListGamesEffect> =
        effect

    override fun dispatch(action: ListGamesAction) {
        val oldState = state.value

        val newState = when(action){
            is ListGamesAction.Data -> {
                if (oldState.isProgress){
                    ListGamesState(false, action.data)
                }else{
                    launch { effect.emit(ListGamesEffect.Error(UnexpectedException())) }
                    oldState
                }
            }
            is ListGamesAction.Error -> {
                if (oldState.isProgress){
                    launch { effect.emit(ListGamesEffect.Error(action.exception)) }
                    ListGamesState(false, oldState.data)
                }else{
                    launch { effect.emit(ListGamesEffect.Error(UnexpectedException())) }
                    oldState
                }
            }
            is ListGamesAction.Refresh -> {
                if (oldState.isProgress){
                    launch { effect.emit(ListGamesEffect.Error(LoadingException())) }
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
            val data = repository.getGames()
            dispatch(ListGamesAction.Data(data))
        }catch (e: Exception){
            dispatch(ListGamesAction.Error(e))
        }
    }
}