package com.kampungpedia.android.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kampungpedia.android.R

@Composable
fun EmptyDataView(modifier: Modifier = Modifier){
    val animate by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_animation))
    LottieAnimation(composition = animate, restartOnPlay =  true, modifier = modifier, iterations = LottieConstants.IterateForever)

}