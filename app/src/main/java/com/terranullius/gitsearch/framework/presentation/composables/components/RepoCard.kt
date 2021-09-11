package com.terranullius.gitsearch.framework.presentation.composables.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.terranullius.gitsearch.R
import com.terranullius.gitsearch.business.domain.model.Repo

@ExperimentalCoilApi
@Composable
fun RepoCard(
    modifier: Modifier = Modifier,
    repo: Repo,
    onClick: (Repo) -> Unit,
    bottomContent: @Composable BoxScope.(Repo) -> Unit
) {
    Card(
        modifier = modifier
            .clickable {
                onClick(repo)
            },
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            val painter = rememberImagePainter(data = repo.owner.avatarUrl, builder = {
                this.memoryCacheKey(repo.id.toString())
                crossfade(true)
            })

            Image(
                painter = painter,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                contentDescription = "image"
            )

            when (painter.state) {
                is ImagePainter.State.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is ImagePainter.State.Success -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black
                                    ),
                                    startY = 300f
                                )
                            )
                    )
                }
                is ImagePainter.State.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Image(painterResource(id = R.drawable.ic_image_error), "image error")
                    }
                }
            }


            bottomContent(repo)
        }

    }
}