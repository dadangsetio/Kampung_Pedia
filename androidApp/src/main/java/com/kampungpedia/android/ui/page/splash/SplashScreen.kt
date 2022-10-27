package com.kampungpedia.android.ui.page.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kampungpedia.android.R
import com.kampungpedia.android.ui.component.LottieView
import com.kampungpedia.android.ui.page.login.LoginScreen
import com.kampungpedia.android.ui.page.main.MainScreen
import com.kampungpedia.android.ui.theme.AppTheme
import com.kampungpedia.logic.Auth
import com.kampungpedia.logic.AuthAction
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SplashScreen: Screen, KoinComponent {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val auth by inject<Auth>()
        val authState = auth.observeState().collectAsState()

        auth.dispatch(AuthAction.Auth)
        LaunchedEffect(key1 = true){
            delay(5000)
            authState.value.data.let{
                if (it != null){
                    navigator.replaceAll(MainScreen())
                }else{
                    navigator.replaceAll(LoginScreen())
                }
            }
        }

        val animate by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.hello_animation))

        AppTheme {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LottieAnimation(composition = animate)
            }
        }
    }

}