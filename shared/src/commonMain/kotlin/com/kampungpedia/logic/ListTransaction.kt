package com.kampungpedia.logic

import com.kampungpedia.data.TransactionRepository
import com.kampungpedia.data.network.transaction.model.ListTransactionResponse
import com.kampungpedia.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ListTransactionState(
    val isProgress: Boolean,
    val data: ListTransactionResponse?
): State

sealed class ListTransactionAction : Action {
    data class Refresh(val forceRefresh: Boolean): ListTransactionAction()
    data class Data(val data: ListTransactionResponse): ListTransactionAction()
    data class Error(val exception: Exception): ListTransactionAction()
    object Delete: ListTransactionAction()
}

sealed class ListTransactionEffect: Effect {
    data class Error(val exception: Exception): ListTransactionEffect()
}

class ListTransaction(
    private val repository: TransactionRepository
): Store<ListTransactionState, ListTransactionAction, ListTransactionEffect>, CoroutineScope by CoroutineScope(Dispatchers.Main) {
    private val state = MutableStateFlow(ListTransactionState(false, null))
    private val effect = MutableSharedFlow<ListTransactionEffect>()

    override fun observeState(): StateFlow<ListTransactionState> =
        state

    override fun observeSideEffect(): Flow<ListTransactionEffect> =
        effect

    override fun dispatch(action: ListTransactionAction) {
        val  oldState = state.value

        val newState = when(action){
            is ListTransactionAction.Data -> {
                if (oldState.isProgress){
                    ListTransactionState(false, action.data)
                }else{
                    launch { effect.emit(ListTransactionEffect.Error(UnexpectedException())) }
                    oldState
                }
            }
            is ListTransactionAction.Delete -> {
                if(oldState.isProgress){
                    launch { effect.emit(ListTransactionEffect.Error(LoadingException())) }
                    oldState
                }else{
                    launch { deleteData() }
                    oldState.copy(isProgress = true)
                }
            }
            is ListTransactionAction.Error -> {
                if (oldState.isProgress){
                    launch { effect.emit(ListTransactionEffect.Error(action.exception)) }
                    ListTransactionState(false, oldState.data)
                }else{
                    launch { effect.emit(ListTransactionEffect.Error(UnexpectedException())) }
                    oldState
                }
            }
            is ListTransactionAction.Refresh -> {
                if (oldState.isProgress){
                    launch { effect.emit(ListTransactionEffect.Error(LoadingException())) }
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
            val data = repository.getListTransaction(forceRefresh)
            dispatch(ListTransactionAction.Data(data))
        }catch (e: Exception){
            dispatch(ListTransactionAction.Error(e))
        }
    }

    private fun deleteData(){
        try {
            repository.delete()
        }catch (e: Exception){
            dispatch(ListTransactionAction.Error(e))
        }
    }

}