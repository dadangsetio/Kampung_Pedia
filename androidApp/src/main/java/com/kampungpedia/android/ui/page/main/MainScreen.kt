package com.kampungpedia.android.ui.page.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.kampungpedia.android.ui.page.main.account.AccountScreen
import com.kampungpedia.android.ui.page.main.home.HomeScreen
import com.kampungpedia.android.ui.page.main.notification.NotificationScreen
import com.kampungpedia.android.ui.page.main.transaction.TransactionScreen
import org.koin.core.component.KoinComponent

class MainScreen: Screen, KoinComponent {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        TabNavigator(HomeScreen()) {
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(tab = HomeScreen())
                        NavigationBarItem(tab = NotificationScreen())
                        NavigationBarItem(tab = TransactionScreen())
                        NavigationBarItem(tab = AccountScreen())
                    }
                }
            ) {
                Box(modifier = Modifier.padding(it)) {
                    CurrentTab()
                }
            }
        }
    }

    @Composable
    private fun RowScope.NavigationBarItem(tab: Tab) {
        val tabNavigator = LocalTabNavigator.current

        NavigationBarItem(
            selected = tabNavigator.current.key == tab.key,
            onClick = { tabNavigator.current = tab },
            label = { Text(text = tab.options.title) },
            icon = { Icon(painter = tab.options.icon!!, contentDescription = tab.options.title) }
        )
    }
}