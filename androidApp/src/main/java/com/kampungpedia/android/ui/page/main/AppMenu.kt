package com.kampungpedia.android.ui.page.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.kampungpedia.android.R

object AppMenu {
    const val SCAN = 0
    const val PULSA = 1
    const val GAMES = 2
    const val VOUCHER = 3
    val Home = listOf<Menu>(
        Menu(
            id = SCAN,
            title = "Scan",
            imageRes = R.drawable.ic_baseline_qr_code_scanner_24
        ),
        Menu(
            id = PULSA,
            title = "Pulsa",
            imageRes = R.drawable.ic_baseline_phone_iphone_24
        ),
        Menu(
            id = GAMES,
            title = "Games",
            imageRes = R.drawable.ic_baseline_games_24
        ),
        Menu(
            id = VOUCHER,
            title = "Voucher",
            imageRes = R.drawable.ic_baseline_card_giftcard_24
        )
    )

    val Navigation =  listOf(
        NavMenu(
            id = 0,
            title = "Home",
            icon = Icons.Filled.Home
        ),
        NavMenu(
            id = 1,
            title = "Notification",
            icon = Icons.Filled.Notifications
        ),
        NavMenu(
            id = 2,
            title = "Transaction",
            icon = Icons.Filled.ReceiptLong
        ),
        NavMenu(
            id = 3,
            title = "Account",
            icon = Icons.Filled.AccountCircle
        ),
    )

    val Account = listOf(
        NavMenu(
            99,
            "Log out",
            Icons.Filled.Logout
        )
    )
}

data class Menu(
    val id: Int = 0,
    val title: String,
    val imageRes: Int
)

data class NavMenu(
    val id: Int = 0,
    val title: String,
    val icon: ImageVector
)