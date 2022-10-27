package com.kampungpedia.logic

import com.kampungpedia.data.BannerRepository
import com.kampungpedia.data.network.banner.model.BannerResponse
import com.kampungpedia.utils.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


data class BannersState(
    val isProgress: Boolean,
    val data: BannerResponse?
): State

sealed class BannersAction: Action {
    data class Refresh(val forceRefresh: Boolean): BannersAction()
    data class Data(val data: BannerResponse): BannersAction()
    object Delete: BannersAction()
    data class Error(val exception: Exception): BannersAction()
}

sealed class BannersEffect: Effect{
    data class Error(val exception: Exception): BannersEffect()
}

class Banners(
    private val bannerRepository: BannerRepository
): Store<BannersState, BannersAction, BannersEffect>, CoroutineScope by CoroutineScope(Dispatchers.Main) {
    private val state = MutableStateFlow(BannersState(false, null))
    private val effect = MutableSharedFlow<BannersEffect>()

    override fun observeState(): StateFlow<BannersState> = state

    override fun observeSideEffect(): Flow<BannersEffect>  = effect

    override fun dispatch(action: BannersAction) {
       val oldState = state.value

        val newState = when(action){
            is BannersAction.Data -> {
                if (oldState.isProgress){
                    BannersState(false, action.data)
                }else{
                    launch { effect.emit(BannersEffect.Error(UnexpectedException())) }
                    oldState
                }
            }
            is BannersAction.Delete -> {
                if (oldState.isProgress){
                    launch { effect.emit(BannersEffect.Error(LoadingException())) }
                    oldState
                }else{
                    launch { deleteData() }
                    oldState.copy(isProgress = true)
                }
            }
            is BannersAction.Error -> {
                if (oldState.isProgress){
                    launch { effect.emit(BannersEffect.Error(action.exception)) }
                    BannersState(false, oldState.data)
                }else{
                    launch { effect.emit(BannersEffect.Error(UnexpectedException())) }
                    oldState
                }
            }
            is BannersAction.Refresh -> {
                if (oldState.isProgress){
                    launch { effect.emit(BannersEffect.Error(LoadingException())) }
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

    private fun deleteData(){
        try {
            bannerRepository.delete()
        }catch (e: Exception){
            dispatch(BannersAction.Error(e))
        }
    }

    private suspend fun loadData(forceRefresh: Boolean){
        try {
            val data = bannerRepository.get(forceRefresh)
            dispatch(BannersAction.Data(data))
        }catch (e: Exception){
            dispatch(BannersAction.Error(e))
        }
    }

}