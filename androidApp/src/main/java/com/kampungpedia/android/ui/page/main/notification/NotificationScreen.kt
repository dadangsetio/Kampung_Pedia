package com.kampungpedia.android.ui.page.main.notification

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.kampungpedia.android.ui.component.EmptyDataView
import com.kampungpedia.android.ui.page.main.AppMenu
import com.kampungpedia.android.ui.theme.AppTheme
import com.kampungpedia.android.ui.theme.spaceL
import org.koin.core.component.KoinComponent

class NotificationScreen: Tab, KoinComponent {
    override val options: TabOptions
        @Composable
        get() {
            val menu = AppMenu.Navigation[1]
            val icon = rememberVectorPainter(menu.icon)

            return remember {
                TabOptions(
                    index = 1u,
                    title = menu.title,
                    icon = icon
                )
            }
        }

    @Preview
    @Composable
    override fun Content() {
        AppTheme {
            Column {
                Text(
                    text = "Notification",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(horizontal = spaceL())
                        .padding(top = spaceL())
                )
                Box(contentAlignment = Alignment.Center, modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()){
                    EmptyDataView(modifier = Modifier.size(200.dp))
                }
            }
        }
    }
}