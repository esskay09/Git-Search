package com.terranullius.gitsearch.framework.presentation.composables

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.terranullius.gitsearch.R
import com.terranullius.gitsearch.business.domain.model.Repo
import com.terranullius.gitsearch.business.domain.model.User
import com.terranullius.gitsearch.business.domain.state.StateResource
import com.terranullius.gitsearch.framework.presentation.MainViewModel
import com.terranullius.gitsearch.framework.presentation.composables.theme.getHeadlineTextColor
import com.terranullius.gitsearch.framework.presentation.composables.theme.getTextColor
import com.terranullius.gitsearch.framework.presentation.composables.components.ErrorComposable
import com.terranullius.gitsearch.framework.presentation.composables.components.LoadingComposable
import com.terranullius.gitsearch.framework.presentation.composables.components.RepoCard
import com.terranullius.gitsearch.framework.presentation.util.Screen
import kotlinx.coroutines.ExperimentalCoroutinesApi


/**
 * Calculate Screen Height for Supporting all screen sizes
 * */


@Composable
fun RepoDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    navController: NavHostController
) {

    val screenHeight = LocalConfiguration.current.screenHeightDp
    val imageHeight = screenHeight.div(2.4).dp

    val selectedRepo = remember {
        viewModel.selectedRepo
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        when (selectedRepo.value) {
            null -> {
                ErrorComposable() {
                }
            }
            else -> {
                val contributorListState =
                    viewModel.getContributors(selectedRepo.value!!.fullName).collectAsState(
                        initial = StateResource.Loading
                    )

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
                        contributorListState = contributorListState,
                        imageHeight = imageHeight,
                        navController = navController,
                        viewModel = viewModel
                    ) {
                        viewModel.setSelectedUrl(selectedRepo.value!!.repoUrl)
                        navWebScreen(navController)
                    }
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
    contributorListState: State<StateResource<List<User>>>,
    navController: NavHostController,
    viewModel: MainViewModel,
    onProjectLinkClick: () -> Unit,
) {

    /**
     *  Set Different layout depending on screen orientation
     * */

    when (LocalConfiguration.current.orientation) {
        ORIENTATION_LANDSCAPE -> RepoDetailContentLandScape(
            modifier = modifier,
            repo = repo,
            imageHeight = imageHeight,
            contributorListState = contributorListState,
            onProjectLinkClick = onProjectLinkClick,
            navController = navController,
            viewModel = viewModel
        )
        ORIENTATION_PORTRAIT -> RepoDetailContentPortrait(
            modifier = modifier,
            repo = repo,
            imageHeight = imageHeight,
            contributorListState = contributorListState,
            onProjectLinkClick = onProjectLinkClick,
            navController = navController,
            viewModel = viewModel
        )
        else -> RepoDetailContentPortrait(
            modifier = modifier,
            repo = repo,
            imageHeight = imageHeight,
            contributorListState = contributorListState,
            onProjectLinkClick = onProjectLinkClick,
            navController = navController,
            viewModel = viewModel
        )
    }
}


@Composable
private fun RepoDetailContentPortrait(
    modifier: Modifier,
    repo: Repo,
    imageHeight: Dp,
    contributorListState: State<StateResource<List<User>>>,
    navController: NavHostController,
    viewModel: MainViewModel,
    onProjectLinkClick: () -> Unit
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
        RepoDetailDescription(
            repo = repo,
            contributorListState = contributorListState,
            onProjectLinkClick = onProjectLinkClick,
            navController = navController,
            viewModel = viewModel
        )
    }
}


@Composable
fun RepoDetailContentLandScape(
    modifier: Modifier = Modifier,
    repo: Repo,
    imageHeight: Dp,
    contributorListState: State<StateResource<List<User>>>,
    navController: NavHostController,
    viewModel: MainViewModel,
    onProjectLinkClick: () -> Unit
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
                .padding(horizontal = 8.dp),
            repo = repo,
            contributorListState = contributorListState,
            navController = navController,
            viewModel = viewModel,
            onProjectLinkClick = onProjectLinkClick
        )
    }

}

@Composable
private fun RepoDetailDescription(
    repo: Repo,
    modifier: Modifier = Modifier,
    contributorListState: State<StateResource<List<User>>>,
    viewModel: MainViewModel,
    navController: NavHostController,
    onProjectLinkClick: () -> Unit
) {
    LazyColumn(modifier = modifier) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            Row {
                Text(
                    text = repo.name,
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
                RepoDescriptionItem(
                    text = repo.description,
                    icon = Icons.Default.Description
                )
                RepoDescriptionItem(
                    text = repo.stargazers.toString(),
                    icon = Icons.Default.Star
                )
                RepoDescriptionItem(
                    text = repo.forks.toString(),
                    icon = ImageVector.vectorResource(id = R.drawable.ic_fork)
                )
                RepoDescriptionItem(
                    text = repo.license,
                    icon = Icons.Default.Book
                )
                Row(modifier = Modifier.clickable {
                    onProjectLinkClick()
                }) {
                    Icon(Icons.Default.Link, contentDescription = "", tint = getTextColor())
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Project link", color = Color.Blue)
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(12.dp))
            ContributorsRow(
                contributors = contributorListState,
                modifier = Modifier.height(100.dp).fillMaxWidth()
            ) {
                viewModel.searchRepo("user:${it.username}")
                navMainScreen(navController)
            }
        }
    }
}


@Composable
fun ColumnScope.RepoDescriptionItem(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector
) {
    Row(modifier = modifier) {
        Icon(icon, contentDescription = "", tint = getTextColor())
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = text, color = getTextColor())
    }
    Spacer(modifier = Modifier.height(12.dp))
}

fun navWebScreen(
    navController: NavHostController
) {
    navController.navigate(Screen.Web.route)
}


@Composable
fun ContributorsRow(
    contributors: State<StateResource<List<User>>>,
    modifier: Modifier = Modifier,
    onContributorClick: (User) -> Unit
) {

    Column(modifier = modifier) {

        Text(
            text = "Contributors",
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        when (val contributorsStateRes = contributors.value) {
            is StateResource.Loading -> LoadingComposable(Modifier.align(Alignment.CenterHorizontally))
            is StateResource.Error -> ErrorComposable(Modifier.align(Alignment.CenterHorizontally)) {}
            is StateResource.Success -> {
                LazyRow(modifier = modifier) {
                    items(contributorsStateRes.data) { user ->
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            ContributorItem(
                                user = user,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(1f, matchHeightConstraintsFirst = true)
                            ) {
                                onContributorClick(user)
                            }
                        }
                    }
                }
            }
            else -> {
            }
        }

    }
}

@Composable
fun ContributorItem(
    user: User,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Image(
        painter = rememberImagePainter(data = user.avatarUrl),
        contentDescription = "",
        modifier = modifier
            .clickable {
                onClick()
            }
            .clip(
                CircleShape
            )
    )
}

fun navMainScreen(navController: NavHostController) {
    navController.navigate(Screen.Main.route) {
        popUpTo(route = Screen.Main.route) {
            inclusive = true
        }
    }
}