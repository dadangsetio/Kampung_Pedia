package com.kampungpedia.logic

import com.kampungpedia.data.ProfileRepository
import com.kampungpedia.data.network.profile.ProfileEditRequest
import com.kampungpedia.data.network.profile.model.ProfileResponse
import com.kampungpedia.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProfileState(
    val isProgress: Boolean,
    val profile: ProfileResponse?
): State

sealed class ProfileAction: Action{
    data class Refresh(val forceRefresh: Boolean): ProfileAction()
    data class Data(val data: ProfileResponse): ProfileAction()
    data class Edit(val data: ProfileEditRequest): ProfileAction()
    data class Error(val exception: Exception): ProfileAction()
    object Delete: ProfileAction()
}

sealed class ProfileEffect: Effect{
    data class Error(val exception: Exception): ProfileEffect()
}

class Profile(
    private val profileRepository: ProfileRepository
) : Store<ProfileState, ProfileAction, ProfileEffect>, CoroutineScope by CoroutineScope(Dispatchers.Main){
    private val state = MutableStateFlow(ProfileState(false, null))
    private val effect = MutableSharedFlow<ProfileEffect>()

    override fun observeState(): StateFlow<ProfileState> =
        state

    override fun observeSideEffect(): Flow<ProfileEffect> =
        effect

    override fun dispatch(action: ProfileAction) {
        val oldState = state.value

        val newState = when(action){
            is ProfileAction.Refresh -> {
                if (oldState.isProgress){
                    launch { effect.emit(ProfileEffect.Error(LoadingException())) }
                    oldState
                }else{
                    launch { loadData(action.forceRefresh) }
                    oldState.copy(isProgress = true)
                }
            }
            is ProfileAction.Edit -> {
                if (oldState.isProgress){
                    launch { effect.emit(ProfileEffect.Error(LoadingException())) }
                    oldState
                }else{
                    launch { editData(action.data) }
                    oldState.copy(isProgress = true)
                }
            }
            is ProfileAction.Error -> {
                if (oldState.isProgress){
                    launch { effect.emit(ProfileEffect.Error(action.exception)) }
                    ProfileState(false, oldState.profile)
                }else{
                    launch { effect.emit(ProfileEffect.Error(UnexpectedException())) }
                    oldState
                }
            }
            is ProfileAction.Data -> {
                if (oldState.isProgress){
                    ProfileState(false, action.data)
                }else{
                    launch { effect.emit(ProfileEffect.Error(UnexpectedException())) }
                    oldState
                }
            }
            is ProfileAction.Delete -> {
                if (oldState.isProgress){
                    launch { effect.emit(ProfileEffect.Error(LoadingException())) }
                    oldState
                }else{
                    launch { deleteData() }
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
            profileRepository.delete()
        }catch (e: Exception){
            dispatch(ProfileAction.Error(e))
        }
    }

    private suspend fun loadData(forceUpdate: Boolean){
        try {
            val data = profileRepository.getProfile(forceUpdate)
            dispatch(ProfileAction.Data(data))
        }catch (e: Exception){
            dispatch(ProfileAction.Error(e))
        }
    }

    private suspend fun editData(request: ProfileEditRequest){
        try {
            val data = profileRepository.editProfile(request)
            dispatch(ProfileAction.Data(data))
        }catch (e: Exception){
            dispatch(ProfileAction.Error(e))
        }
    }

}
