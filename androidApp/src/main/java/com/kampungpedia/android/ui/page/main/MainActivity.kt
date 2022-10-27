package com.kampungpedia.android.ui.page.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.kampungpedia.android.ui.page.splash.SplashScreen
import com.kampungpedia.android.ui.theme.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Box(
                    Modifier.padding(
                        WindowInsets.statusBars.asPaddingValues()
                    )
                ) {
                    Navigator(screen = SplashScreen())
                }
            }
        }
    }


}
