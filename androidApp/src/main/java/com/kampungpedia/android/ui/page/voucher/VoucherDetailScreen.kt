package com.kampungpedia.android.ui.page.voucher

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import coil.compose.AsyncImage
import com.kampungpedia.android.ui.theme.*
import com.kampungpedia.android.ui.theme.spaceM
import com.kampungpedia.android.ui.theme.spaceS
import com.kampungpedia.android.ui.utils.asCurrency
import com.kampungpedia.data.network.voucher.model.VoucherData
import org.koin.core.component.KoinComponent

@OptIn(ExperimentalMaterial3Api::class)
class VoucherDetailScreen(
    private val voucherData: VoucherData
): Screen, KoinComponent {

    @Composable
    override fun Content() {
        AppTheme {
            Scaffold {
                Box(modifier = Modifier
                    .padding(it)
                    .padding(spaceM())
                    .fillMaxSize()){
                    Column {
                        AsyncImage(model = voucherData.image, contentDescription ="item_image", modifier = Modifier
                            .size(300.dp)
                            .clip(RoundedCornerShape(radiusL()))
                            .align(Alignment.CenterHorizontally))
                        Spacer(modifier = Modifier.height(spaceM()))
                        Text(text = voucherData.name ?: "", style = MaterialTheme.typography.titleMedium)
                        Text(text = voucherData.publisher.orEmpty(), style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(spaceM()))
                        LazyVerticalGrid(
                            modifier = Modifier
                                .padding(horizontal = spaceM())
                                .fillMaxWidth(),
                            columns = GridCells.Fixed(2),
                        ) {
                            items(voucherData.denom){ denom ->
                                Card(modifier = Modifier
                                    .padding(
                                        spaceS()
                                    )) {
                                    Column(modifier = Modifier.padding(spaceS())){
                                        Text(text = denom.packages ?: "", style = MaterialTheme.typography.bodyMedium)
                                        Text(text = (denom.price ?: 0L).asCurrency(), style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}