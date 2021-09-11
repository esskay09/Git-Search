package com.terranullius.gitsearch.framework.presentation.composables

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.terranullius.gitsearch.business.domain.model.Repo
import com.terranullius.gitsearch.framework.presentation.MainViewModel
import com.terranullius.gitsearch.framework.presentation.composables.theme.getHeadlineTextColor
import com.terranullius.gitsearch.framework.presentation.composables.theme.getTextColor
import com.terranullius.gitsearch.framework.presentation.composables.components.ErrorComposable
import com.terranullius.gitsearch.framework.presentation.composables.components.RepoCard
import kotlin.random.Random


/**
 * Calculate Screen Height for Supporting all screen sizes
 * */


@Composable
fun RepoDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {

    val screenHeight = LocalConfiguration.current.screenHeightDp
    val imageHeight = screenHeight.div(2.4).dp

    val selectedRepo = remember {
        viewModel.selectedRepo
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        when (selectedRepo.value) {
            null -> {
                ErrorComposable(){

                }
            }
            else -> {
                Scaffold(
                ) { paddingValues ->
                    RepoDetailContent(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = paddingValues
                                    .calculateTopPadding()
                                    .plus(8.dp),
                                bottom = paddingValues.calculateBottomPadding(),
                                start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                                end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
                            ),
                        repo = selectedRepo.value!!,
                        imageHeight = imageHeight
                    )
                }
            }
        }
    }
}

@Composable
fun RepoDetailContent(
    modifier: Modifier = Modifier,
    repo: Repo,
    imageHeight: Dp,
) {

    /**
     *  Set Different layout depending on screen orientation
     * */


    when (LocalConfiguration.current.orientation) {
        ORIENTATION_LANDSCAPE -> RepoDetailContentLandScape(
            modifier = modifier,
            repo = repo,
            imageHeight = imageHeight
        )
        ORIENTATION_PORTRAIT -> RepoDetailContentPotrait(
            modifier = modifier,
            repo = repo,
            imageHeight = imageHeight
        )
        else -> RepoDetailContentPotrait(
            modifier = modifier,
            repo = repo,
            imageHeight = imageHeight
        )
    }
}


@Composable
private fun RepoDetailContentPotrait(
    modifier: Modifier,
    repo: Repo,
    imageHeight: Dp
) {
    Column(modifier = modifier) {
        RepoCard(
            repo = repo,
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
        ) {
        }

        RepoDetailDescription(repo)
    }
}

@Composable
fun RepoDetailContentLandScape(
    modifier: Modifier = Modifier,
    repo: Repo,
    imageHeight: Dp
) {
    Row(modifier = modifier) {
        RepoCard(
            repo = repo,
            onClick = {},
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
        }

        RepoDetailDescription(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(horizontal = 8.dp), repo = repo
        )
    }

}

@Composable
private fun RepoDetailDescription(repo: Repo, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            Row {
                Text(
                    text = repo.userName,
                    style = MaterialTheme.typography.h5.copy(
                        fontWeight = FontWeight.Bold,
                        color = getHeadlineTextColor()
                    )
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(18.dp))
        }

        item {
            Column(Modifier.padding(8.dp)) {
                RepoDescriptionItem(text = repo.description.toString(), icon = Icons.Default.ThumbUp)
                Spacer(modifier = Modifier.height(12.dp))
                RepoDescriptionItem(text = repo.watchers.toString(), icon = Icons.Default.Visibility)
                Spacer(modifier = Modifier.height(12.dp))
                RepoDescriptionItem(text = repo.stargazers.toString(), icon = Icons.Default.Person)
                Spacer(modifier = Modifier.height(12.dp))
                RepoDescriptionItem(
                    text = Random.nextInt(50, 850).toString(),
                    icon = Icons.Default.Comment
                )
            }

        }
    }
}

@Composable
fun RepoDescriptionItem(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector
) {
    Row(modifier = modifier) {
        Icon(icon, contentDescription = "", tint = getTextColor())
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = text, color = getTextColor())
    }
}