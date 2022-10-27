package com.kampungpedia.android.ui.page.main.home

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.kampungpedia.android.ui.page.login.LoginScreen
import com.kampungpedia.android.ui.page.main.AppMenu
import com.kampungpedia.android.ui.page.scanner.ScannerScreen
import com.kampungpedia.android.ui.page.voucher.VoucherScreen
import com.kampungpedia.android.ui.theme.*
import com.kampungpedia.android.ui.theme.spaceL
import com.kampungpedia.android.ui.theme.spaceM
import com.kampungpedia.android.ui.theme.spaceS
import com.kampungpedia.android.ui.theme.spaceXS
import com.kampungpedia.data.network.banner.model.BannerData
import com.kampungpedia.data.network.voucher.VoucherType
import com.kampungpedia.logic.Banners
import com.kampungpedia.logic.BannersAction
import com.kampungpedia.logic.BannersEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.yield
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeScreen : Tab, KoinComponent {

    override val options: TabOptions
        @Composable
        get() {
            val menu = AppMenu.Navigation[0]
            val icon = rememberVectorPainter(menu.icon)

            return remember {
                TabOptions(
                    index = 0u,
                    title = menu.title,
                    icon = icon
                )
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow
        val tabNavigator = LocalTabNavigator.current
        val searchValue = remember {
            mutableStateOf("")
        }
        val banner: Banners by inject()
        val stateBanners = banner.observeState().collectAsState()
        val error = banner.observeSideEffect()
            .filterIsInstance<BannersEffect.Error>()
            .collectAsState(null)

        LaunchedEffect(error.value) {
            error.value?.let {
                Toast.makeText(context, it.exception.message, Toast.LENGTH_SHORT).show()
                it.exception.printStackTrace()
            }
        }

        LaunchedEffect(Unit) {
            banner.dispatch(BannersAction.Refresh(true))
        }

        AppTheme {
            Column {
                Text(
                    text = "Home",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(horizontal = spaceL())
                        .padding(top = spaceL())
                )
                TextField(
                    value = searchValue.value, onValueChange = {
                        searchValue.value = it
                    }, shape = RoundedCornerShape(100),
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Filled.Search, "")
                    },
                    placeholder = {
                        Text(text = "Search anything...")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spaceM()),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        disabledTextColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(spaceS()),
                    modifier = Modifier.padding(horizontal = spaceM())
                ) {

                    items(AppMenu.Home) { menu ->
                        Card(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(70.dp),
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        if (menu.id == AppMenu.PULSA){
                                            Toast.makeText(context, "Upcoming feature", Toast.LENGTH_SHORT).show()
                                        }else{
                                            val screen = when (menu.id) {
                                                AppMenu.GAMES -> VoucherScreen(
                                                    VoucherType.Games
                                                )
                                                AppMenu.VOUCHER -> VoucherScreen(
                                                    VoucherType.Voucher
                                                )
//                                            AppMenu.PULSA -> LoginScreen()
                                                AppMenu.SCAN -> ScannerScreen()
                                                else -> LoginScreen()
                                            }
                                            navigator.parent?.push(screen)
                                        }
                                    }
                            ) {
                                Image(
                                    painter = painterResource(id = menu.imageRes),
                                    contentDescription = "scanner"
                                )
                                Text(text = menu.title, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }

                stateBanners.value.data?.let {
                    SliderBanner(list = it.data)
                }
            }
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun SliderBanner(list: List<BannerData>) {
        val state = rememberPagerState()
        val imageUrl = remember {
            mutableStateOf("")
        }
        HorizontalPager(
            count = list.size,
            state = state,
            modifier = Modifier
                .height(100.dp),
            contentPadding = PaddingValues(horizontal = spaceM())
        ) { page ->
            imageUrl.value = list[page].banner
            AsyncImage(
                model = imageUrl.value,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spaceXS()),
                contentScale = ContentScale.Crop
            )
        }

        LaunchedEffect(key1 = true) {
            while (true) {
                yield()
                delay(10000)
                tween<Float>(600)
                state.animateScrollToPage(
                    page = (state.currentPage + 1) % (state.pageCount)
                )
            }
        }
    }
}