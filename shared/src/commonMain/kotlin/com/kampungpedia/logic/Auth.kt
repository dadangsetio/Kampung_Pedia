package com.kampungpedia.logic

import com.kampungpedia.data.AuthRepository
import com.kampungpedia.data.network.auth.model.AuthResponse
import com.kampungpedia.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AuthState(
    val isProgress: Boolean,
    val data: AuthResponse?
): State

sealed class AuthAction: Action {
    data class Login(val email: String, val password: String): AuthAction()
    data class LoginOrRegister(val token: String): AuthAction()
    data class Data(val data: AuthResponse?): AuthAction()
    object Logout: AuthAction()
    object Auth: AuthAction()
    data class Error(val exception: Exception): AuthAction()
}

sealed class AuthEffect: Effect {
    data class Error(val exception: Exception): AuthEffect()
}

class Auth(
    private val authRepository: AuthRepository
): Store<AuthState, AuthAction, AuthEffect>, CoroutineScope by CoroutineScope(Dispatchers.Main) {
    private val state = MutableStateFlow(AuthState(false, null),)
    private val effect = MutableSharedFlow<AuthEffect>()

    override fun observeState(): StateFlow<AuthState> =
        state

    override fun observeSideEffect(): Flow<AuthEffect> =
        effect

    override fun dispatch(action: AuthAction) {

        val oldState = state.value

        val newState = when(action){
            is AuthAction.Data -> {
                if (oldState.isProgress){
                    AuthState(false, action.data)
                }else{
                    launch { effect.emit(AuthEffect.Error(UnexpectedException())) }
                    oldState
                }
            }
            is AuthAction.Logout -> {
                if (oldState.isProgress){
                    launch { effect.emit(AuthEffect.Error(LoadingException())) }
                    oldState
                }else{
                    launch { logout() }
                    AuthState(true, null)
                }
            }
            is AuthAction.Error -> {
                if (oldState.isProgress){
                    launch { effect.emit(AuthEffect.Error(action.exception)) }
                    AuthState(false, oldState.data)
                }else{
                    launch { effect.emit(AuthEffect.Error(UnexpectedException())) }
                    oldState
                }
            }
            is AuthAction.Login -> {
                if (oldState.isProgress){
                    launch { effect.emit(AuthEffect.Error(LoadingException())) }
                    oldState
                }else{
                    launch { login(action.email, action.password) }
                    oldState.copy(isProgress = true)
                }
            }
            is AuthAction.LoginOrRegister -> {
                if (oldState.isProgress){
                    launch { effect.emit(AuthEffect.Error(LoadingException())) }
                    oldState
                }else{
                    launch { loginOrRegister(action.token) }
                    oldState.copy(isProgress = true)
                }
            }
            is AuthAction.Auth -> {
                if (oldState.isProgress){
                    launch { effect.emit(AuthEffect.Error(LoadingException())) }
                    oldState
                }else{
                    launch { auth() }
                    oldState.copy(isProgress = true)
                }
            }
        }

        if (newState != oldState){
            state.value = newState
        }
    }

    private suspend fun logout(){
        try {
            authRepository.logout()
        }catch (e: Exception){
            dispatch(AuthAction.Error(e))
        }
    }

    private suspend fun login(email: String, password: String){
        try {
            val data = authRepository.auth(email, password)
            data.collect{
                dispatch(AuthAction.Data(it))
            }
        }catch (e: Exception){
            dispatch(AuthAction.Error(e))
        }
    }

    private suspend fun loginOrRegister(token: String){
        try {

            val data = authRepository.auth(token)
           data.collect{
               dispatch(AuthAction.Data(it))
           }
        }catch (e: Exception){
            e.printStackTrace()
            dispatch(AuthAction.Error(e))
        }
    }

    private suspend fun auth(){
        try {
            val data = authRepository.auth
            data.collect{
                dispatch(AuthAction.Data(it))
            }

        }catch (e: Exception){
            dispatch(AuthAction.Error(e))
        }
    }


}