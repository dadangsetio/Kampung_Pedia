package com.kampungpedia.android.ui.page.main.account

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coil.compose.AsyncImage
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kampungpedia.android.R
import com.kampungpedia.android.ui.page.login.LoginScreen
import com.kampungpedia.android.ui.page.main.AppMenu
import com.kampungpedia.android.ui.theme.*
import com.kampungpedia.android.ui.theme.spaceL
import com.kampungpedia.android.ui.theme.spaceM
import com.kampungpedia.android.ui.theme.spaceS
import com.kampungpedia.android.ui.theme.spaceXS
import com.kampungpedia.logic.*
import kotlinx.coroutines.flow.filterIsInstance
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AccountScreen : Tab, KoinComponent {
    override val options: TabOptions
        @Composable
        get() {
            val menu = AppMenu.Navigation[3]
            val icon = rememberVectorPainter(menu.icon)

            return remember {
                TabOptions(
                    index = menu.id.toUShort(),
                    title = menu.title,
                    icon = icon
                )
            }
        }

    private fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//         Request id token if you intend to verify google user from your backend server
            .requestIdToken(context.getString(R.string.web_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, signInOptions)
    }

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val auth by inject<Auth>()
        val error = auth.observeSideEffect()
            .filterIsInstance<AuthEffect.Error>()
            .collectAsState(initial = null)
        val navigator = LocalNavigator.currentOrThrow
        LaunchedEffect(error.value) {
            error.value?.let {
                Toast.makeText(context, it.exception.message, Toast.LENGTH_SHORT).show()
                it.exception.printStackTrace()
            }
        }

        AppTheme {
            Column {
                Text(
                    text = "Account",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(horizontal = spaceL())
                        .padding(top = spaceL())
                )

                ProfileView(context)

                LazyColumn(
                    modifier = Modifier
                        .padding(top = spaceM())
                        .padding(horizontal = spaceL())
                ) {
                    items(AppMenu.Account) { menu ->
                        Card(
                            modifier = Modifier.clickable {
                                if (menu.id == 99) {
                                    getGoogleSignInClient(context).signOut().addOnFailureListener {
                                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                                    }.addOnSuccessListener {
                                        auth.dispatch(AuthAction.Logout)
                                        navigator.parent?.replaceAll(LoginScreen())
                                    }
                                }
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(spaceS()),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = menu.icon,
                                    contentDescription = menu.title,
                                    modifier = Modifier.padding(
                                        spaceXS()
                                    )
                                )
                                Text(text = menu.title, style = MaterialTheme.typography.titleSmall)
                                Spacer(modifier = Modifier.weight(1f, true))
                                Icon(
                                    imageVector = Icons.Filled.NavigateNext,
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ProfileView(context: Context) {
        val profile by inject<Profile>()
        val profileState = profile.observeState().collectAsState()
        val profileEffect = profile.observeSideEffect()
            .filterIsInstance<ProfileEffect.Error>()
            .collectAsState(initial = null)

        LaunchedEffect(key1 = Unit, block = {
            profile.dispatch(ProfileAction.Refresh(false))
        })

        LaunchedEffect(key1 = profileEffect.value) {
            profileEffect.value?.let {
                it.exception.printStackTrace()
                Toast.makeText(context, it.exception.message, Toast.LENGTH_SHORT).show()
            }
        }

        profileState.value.profile?.let {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = spaceL())
            ) {
                if (it.foto.isNullOrEmpty()) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_round_account_circle_24),
                        contentDescription = ""
                    )
                } else {
                    AsyncImage(
                        model = it.foto,
                        contentDescription = "user photo",
                        modifier = Modifier.size(56.dp)
                    )
                }
                Column(
                    modifier = Modifier.padding(start = spaceS())
                ) {
                    Text(text = it.name.orEmpty(), style = MaterialTheme.typography.titleMedium)
                    Text(text = it.email.orEmpty(), style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.weight(1f, true))
                IconButton(onClick = {
                    Toast.makeText(context, "Under Development", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(Icons.Filled.Edit, contentDescription = "")
                }
            }
        }
    }
}