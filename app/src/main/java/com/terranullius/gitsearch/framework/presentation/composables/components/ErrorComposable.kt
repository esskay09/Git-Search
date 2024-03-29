package com.terranullius.gitsearch.framework.presentation.composables.components

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.terranullius.gitsearch.R

@Composable
fun ErrorComposable(
    modifier: Modifier = Modifier,
    msg: String = "Something went wrong",
    onRetry: () -> Unit
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        AndroidView(
            factory = {
                LottieAnimationView(it).apply {
                    setAnimation(R.raw.error)
                    repeatCount = LottieDrawable.INFINITE
                    repeatMode = LottieDrawable.RESTART
                }
            }
        ) {
            it.playAnimation()
        }
        Spacer(Modifier.height(15.dp))
        Text(text = msg)
        Spacer(Modifier.height(30.dp))

        Button(onClick = { /*TODO*/ }) {
            Text(text = "RETRY")
        }
    }

}