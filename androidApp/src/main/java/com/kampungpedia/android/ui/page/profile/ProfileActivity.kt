package com.kampungpedia.android.ui.page.profile

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kampungpedia.android.R
import com.kampungpedia.android.ui.theme.AppTheme

class ProfileActivity: ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            AppTheme {
                Scaffold {
                    Box(modifier = Modifier.padding(it)){
                        BuildContent()
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    private fun BuildContent(){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(imageVector = Icons.Filled.AccountCircle, contentDescription = "account_image", Modifier.size(100.dp))
            Text(text = "Nama Anda", style = MaterialTheme.typography.titleMedium)
            Text(text = "mail@mail.com", style = MaterialTheme.typography.bodySmall)
        }
    }
}