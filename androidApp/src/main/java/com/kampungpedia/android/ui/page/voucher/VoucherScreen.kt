package com.kampungpedia.android.ui.page.voucher

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import com.kampungpedia.android.ui.theme.*
import com.kampungpedia.data.network.voucher.VoucherType
import com.kampungpedia.data.network.voucher.model.VoucherData
import com.kampungpedia.logic.*
import kotlinx.coroutines.flow.filterIsInstance
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class VoucherScreen(
    private val voucherType: VoucherType
) : Screen, KoinComponent {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        AppTheme {
            Scaffold {
                Box(modifier = Modifier.padding(it)) {
                    Column {
                        Text(
                            text = "List Voucher",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .padding(horizontal = spaceL())
                                .padding(top = spaceL())
                        )
                        when (voucherType) {
                            VoucherType.Games -> ListGamesView()
                            VoucherType.Voucher -> ListVoucherView()
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ListGamesView() {
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow
        val listGames: ListGames by inject()
        val stateListVoucher = listGames.observeState().collectAsState()
        val error = listGames.observeSideEffect()
            .filterIsInstance<ListGamesEffect.Error>()
            .collectAsState(null)
        LaunchedEffect(Unit) {
            listGames.dispatch(ListGamesAction.Refresh(true))
        }

        LaunchedEffect(error.value) {
            error.value?.let {
                Toast.makeText(context, it.exception.message, Toast.LENGTH_SHORT).show()
                it.exception.printStackTrace()
            }
        }
        LazyVerticalGrid(
            modifier = Modifier
                .padding(top = spaceXS())
                .padding(horizontal = spaceM())
                .fillMaxWidth(),
            columns = GridCells.Fixed(2),
        ) {
            stateListVoucher.value.data?.let {
                items(it.listDtu) { dtu ->
                    VoucherItemView(gamesData = dtu, onClick = {
                        navigator.push(VoucherDetailScreen(dtu))
                    })
                }
            }
        }
    }

    @Composable
    private fun ListVoucherView() {
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow
        val listVoucher: ListVoucher by inject()
        val stateListVoucher = listVoucher.observeState().collectAsState()
        val error = listVoucher.observeSideEffect()
            .filterIsInstance<ListVoucherEffect.Error>()
            .collectAsState(null)
        LaunchedEffect(Unit) {
            listVoucher.dispatch(ListVoucherAction.Refresh(true))
        }

        LaunchedEffect(error.value) {
            error.value?.let {
                Toast.makeText(context, it.exception.message, Toast.LENGTH_SHORT).show()
                it.exception.printStackTrace()
            }
        }
        LazyVerticalGrid(
            modifier = Modifier
                .padding(top = spaceXS())
                .padding(horizontal = spaceM())
                .fillMaxWidth(),
            columns = GridCells.Fixed(2),
        ) {
            stateListVoucher.value.data?.let {
                items(it.listVoucher) { dtu ->
                    VoucherItemView(gamesData = dtu, onClick = {
                        navigator.push(VoucherDetailScreen(dtu))
                    })
                }
            }
        }
    }


    @Composable
    private fun VoucherItemView(gamesData: VoucherData, onClick: () -> Unit) {
        Card(
            shape = Shapes.small,
            modifier = Modifier.padding(spaceS())
                .clickable { onClick() }
        ) {
            Column(
                modifier = Modifier.padding(spaceS())
            ) {
                AsyncImage(
                    model = gamesData.image,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spaceXS()),
                    contentScale = ContentScale.Crop,
                    filterQuality = FilterQuality.Medium
                )
                Text(text = gamesData.name ?: "", style = MaterialTheme.typography.titleSmall)
            }
        }
    }

}