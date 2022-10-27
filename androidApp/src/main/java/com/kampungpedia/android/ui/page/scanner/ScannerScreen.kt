package com.kampungpedia.android.ui.page.scanner

import android.Manifest
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import cafe.adriel.voyager.core.screen.Screen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.common.util.concurrent.ListenableFuture
import com.kampungpedia.android.ui.theme.AppTheme
import com.kampungpedia.android.ui.theme.spaceL
import com.kampungpedia.android.ui.theme.spaceM
import org.koin.core.component.KoinComponent
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScannerScreen : Screen, KoinComponent {

    @OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

        AppTheme {
            Scaffold {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                ) {
                    Text(
                        text = "Scan Your Codebar",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(horizontal = spaceL())
                            .padding(top = spaceL())
                    )
                    Text(
                        text = "Move camera into codebar, we will analize to giving information abut it",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(horizontal = spaceL())
                    )
                    when (cameraPermissionState.status) {
                        is PermissionStatus.Denied -> {
                            Column {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(spaceL()),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "We need permission a camera to view your barcode",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Button(onClick = {
                                        cameraPermissionState.launchPermissionRequest()
                                    }, modifier = Modifier.padding(spaceM())) {
                                        Text(text = "Permit")
                                    }
                                }
                            }
                        }
                        PermissionStatus.Granted -> CameraPreview()
                    }
                }
            }

        }
    }

    @Composable
    fun CameraPreview() {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        var preview by remember { mutableStateOf<Preview?>(null) }
        val barCodeVal = remember { mutableStateOf("") }

        AndroidView(
            factory = { AndroidViewContext ->
                PreviewView(AndroidViewContext).apply {
                    this.scaleType = PreviewView.ScaleType.FILL_CENTER
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(spaceL())
                .height(300.dp),
            update = { previewView ->
                val cameraSelector: CameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
                val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
                val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                    ProcessCameraProvider.getInstance(context)

                cameraProviderFuture.addListener({
                    preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                    val barcodeAnalyser = BarCodeAnalyser { barcodes ->
                        barcodes.forEach { barcode ->
                            barcode.rawValue?.let { barcodeValue ->
                                barCodeVal.value = barcodeValue
                                Toast.makeText(context, barcodeValue, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also {
                            it.setAnalyzer(cameraExecutor, barcodeAnalyser)
                        }

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalysis
                        )
                    } catch (e: Exception) {
                        Log.d("TAG", "CameraPreview: ${e.localizedMessage}")
                    }
                }, ContextCompat.getMainExecutor(context))
            }
        )
    }
}