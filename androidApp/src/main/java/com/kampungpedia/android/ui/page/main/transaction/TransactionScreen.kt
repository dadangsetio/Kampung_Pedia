package com.kampungpedia.android.ui.page.main.transaction

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.kampungpedia.android.ui.component.EmptyDataView
import com.kampungpedia.android.ui.page.main.AppMenu
import com.kampungpedia.android.ui.theme.AppTheme
import com.kampungpedia.android.ui.theme.spaceL
import com.kampungpedia.android.ui.theme.spaceM
import com.kampungpedia.android.ui.theme.spaceS
import com.kampungpedia.android.ui.utils.asDateFormat
import com.kampungpedia.data.network.transaction.model.TransactionData
import com.kampungpedia.logic.ListTransaction
import com.kampungpedia.logic.ListTransactionAction
import com.kampungpedia.logic.ListTransactionEffect
import kotlinx.coroutines.flow.filterIsInstance
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TransactionScreen : Tab, KoinComponent {
    override val options: TabOptions
        @Composable
        get() {
            val menu = AppMenu.Navigation[2]
            val icon = rememberVectorPainter(menu.icon)

            return remember {
                TabOptions(
                    index = 2u,
                    title = menu.title,
                    icon = icon
                )
            }
        }

    @Preview
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val listTransaction by inject<ListTransaction>()
        val stateListTransaction = listTransaction.observeState().collectAsState()
        val errorListTransaction = listTransaction.observeSideEffect()
            .filterIsInstance<ListTransactionEffect.Error>()
            .collectAsState(initial = null)
        LaunchedEffect(key1 = errorListTransaction.value) {
            errorListTransaction.value?.let {
                Toast.makeText(context, it.exception.message, Toast.LENGTH_SHORT).show()
                it.exception.printStackTrace()
            }
        }
        LaunchedEffect(key1 = Unit, block = {
            listTransaction.dispatch(ListTransactionAction.Refresh(true))
        })

        AppTheme {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Transaction",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(horizontal = spaceL())
                        .padding(top = spaceL())
                )

                stateListTransaction.value.data?.let {
                   if (it.data.isEmpty()){
                       EmptyDataView()
                   }else{
                       LazyColumn(modifier = Modifier.padding(horizontal = spaceM())) {
                           items(it.data) { items ->
                               ItemTransactionView(items)
                           }
                       }
                   }
                }
            }
        }
    }

    @Composable
    private fun ItemTransactionView(transactionData: TransactionData) {
        Card(modifier = Modifier.padding(spaceS())) {
            Column(
                modifier = Modifier
                    .padding(spaceM())
                    .fillMaxWidth()
            ) {
                Text(text = transactionData.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = transactionData.status,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = transactionData.createdAt.asDateFormat(),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}