package com.kampungpedia.android.ui.page.login

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.kampungpedia.android.R
import com.kampungpedia.android.ui.component.RoundedTextField
import com.kampungpedia.android.ui.page.main.MainScreen
import com.kampungpedia.android.ui.theme.AppTheme
import com.kampungpedia.android.ui.theme.spaceM
import com.kampungpedia.android.ui.theme.spaceXS
import com.kampungpedia.logic.Auth
import com.kampungpedia.logic.AuthAction
import com.kampungpedia.logic.AuthEffect
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterIsInstance
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(ExperimentalMaterial3Api::class)
class LoginScreen : Screen, KoinComponent {

    @Preview
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val auth by inject<Auth>()
        val loadingState = remember {
            MutableTransitionState(false)
        }
        val authState = auth.observeState().collectAsState()
        val error = auth.observeSideEffect()
            .filterIsInstance<AuthEffect.Error>()
            .collectAsState(null)
        val navigator = LocalNavigator.currentOrThrow
        LaunchedEffect(error.value) {
            error.value?.let {
                Toast.makeText(context, it.exception.message, Toast.LENGTH_SHORT).show()
                it.exception.printStackTrace()
                loadingState.targetState = false
            }
        }
        LaunchedEffect(key1 = authState.value, block = {
            authState.value.data?.let {
                navigator.replaceAll(MainScreen())
            }
        })
        val animationComp by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.hello_animation))
        AppTheme {
            Scaffold {
                Box(
                    modifier = Modifier
                        .padding(it)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LottieAnimation(
                            animationComp,
                            modifier = Modifier.weight(1f)
                        )
                        LoginFormVIew(auth, onClick = {
                            loadingState.targetState = true
                        })
                        DividerLoginView()
                        LoginGoogleButton(context, auth, loadingState)
                        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
                    }
                    AnimatedVisibility(visible = loadingState.currentState) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginFormVIew(auth: Auth, onClick: () -> Unit) {
    val focusManager = LocalFocusManager.current
    val email = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    val passwordVisible = remember {
        mutableStateOf(false)
    }
    RoundedTextField(
        value = email.value,
        onValueChange = {
            email.value = it
        },
        placeholder = {
            Text("Email")
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(vertical = spaceXS()),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        )
    )
    RoundedTextField(
        value = password.value,
        onValueChange = {
            password.value = it
        },
        placeholder = {
            Text("Password")
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(vertical = spaceXS()),
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions (onDone = {
            auth.dispatch(AuthAction.Login(email.value, password.value))
            focusManager.clearFocus()
        }),
        trailingIcon = {
            val image = if (passwordVisible.value)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff
            val description = if (passwordVisible.value) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                Icon(imageVector = image, description)
            }
        },
    )
    Button(
        onClick = {
            auth.dispatch(AuthAction.Login(email.value, password.value))
            onClick()
        },
        Modifier
            .fillMaxWidth(0.8f)
            .padding(vertical = spaceXS())
    ) {
        Text(text = "Login", style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun DividerLoginView() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.aligned(Alignment.CenterHorizontally),
        modifier = Modifier.fillMaxWidth()
    ) {
        Divider(modifier = Modifier.fillMaxWidth(0.2f))
        Text(text = "Or", modifier = Modifier.padding(spaceM()))
        Divider(modifier = Modifier.fillMaxWidth(0.3f))
    }
}

@Composable
private fun LoginGoogleButton(
    context: Context,
    auth: Auth,
    loadingState: MutableTransitionState<Boolean>
) {
    val sigInRequestCode = 93
    val googleAuthLauncher =
        rememberLauncherForActivityResult(contract = GoogleAuthContract()) { task ->
            try {
                loadingState.targetState = true
                val account: GoogleSignInAccount? = task?.getResult(ApiException::class.java)
                val token = account?.idToken
                token?.let {
                    auth.dispatch(AuthAction.LoginOrRegister(it))
                }
            } catch (e: ApiException) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                val message = if (e.statusCode == 7) {
                    "Network Error"
                } else {
                    "Invalid Error: ${e.status.statusCode}"
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                loadingState.targetState = false
            }
        }

    Button(onClick = {
        googleAuthLauncher.launch(sigInRequestCode)
    }, modifier = Modifier.fillMaxWidth(0.8f)) {
        Icon(
            painter = painterResource(id = R.drawable.ic_google_logo),
            contentDescription = "apple logo",
            modifier = Modifier.size(16.dp),
            tint = Color.Unspecified
        )
        Text(
            text = "Sign in with Google",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}