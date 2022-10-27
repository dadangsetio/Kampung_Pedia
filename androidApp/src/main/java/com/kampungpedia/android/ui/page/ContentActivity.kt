package com.kampungpedia.android.ui.page

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.kampungpedia.android.ui.page.login.LoginScreen
import com.kampungpedia.android.ui.page.voucher.VoucherScreen
import com.kampungpedia.android.ui.page.main.AppMenu
import com.kampungpedia.android.ui.page.scanner.ScannerScreen
import com.kampungpedia.android.ui.theme.AppTheme
import com.kampungpedia.data.network.voucher.VoucherType

class ContentActivity: ComponentActivity() {

    companion object {
        private const val KEY_MENUS = "key-menus"
        fun newInstance(context: Context, menuId: Int): Intent {
            val intent = Intent(context, ContentActivity::class.java)
            intent.putExtra(KEY_MENUS, menuId)
            return intent
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Scaffold { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)){
                        intent.extras?.getInt(KEY_MENUS)?.let {
                            when(it){
                                AppMenu.GAMES -> Navigator(screen = VoucherScreen(VoucherType.Games))
                                AppMenu.VOUCHER -> Navigator(screen = VoucherScreen(VoucherType.Voucher))
                                AppMenu.PULSA -> Navigator(screen = LoginScreen())
                                AppMenu.SCAN -> Navigator(screen = ScannerScreen())
                            }
                        }
                    }
                }
            }
        }
    }
}