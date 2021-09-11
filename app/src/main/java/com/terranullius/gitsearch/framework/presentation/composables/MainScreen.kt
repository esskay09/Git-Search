package com.terranullius.gitsearch.framework.presentation.composables

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import coil.annotation.ExperimentalCoilApi
import com.terranullius.gitsearch.business.domain.model.Repo
import com.terranullius.gitsearch.framework.presentation.MainViewModel
import com.terranullius.gitsearch.framework.presentation.composables.components.ErrorComposable
import com.terranullius.gitsearch.framework.presentation.composables.components.LoadingComposable
import com.terranullius.gitsearch.framework.presentation.composables.components.RepoCard
import com.terranullius.gitsearch.framework.presentation.composables.theme.getTextColor
import com.terranullius.gitsearch.framework.presentation.composables.theme.spaceBetweenImages
import com.terranullius.gitsearch.framework.presentation.composables.util.ListType
import com.terranullius.gitsearch.framework.presentation.util.Screen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay

@FlowPreview
@ExperimentalCoroutinesApi
@Composable
fun MainScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    navController: NavHostController,
    viewModel: MainViewModel
) {

    /**
     * Calculate Screen Height for Supporting all screen sizes
     * */
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val imageHeight =
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) screenHeight.div(
            1.2
        ).dp else screenHeight.div(3.3).dp

    var listType by rememberSaveable {
        mutableStateOf(ListType.LINEAR)
    }

    val searchQuery = viewModel.searchQueryStateFLow.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = searchQuery.value,
                        onValueChange = {
                            searchRepo(it, viewModel)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = getTextColor()
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.value.isNotBlank()) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = getTextColor(),
                                    modifier = Modifier.clickable {
                                        viewModel.searchRepo("")
                                    }
                                )
                            } else {
                            }
                        },
                        singleLine = true,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                actions = {
                    IconButton(onClick = {
                        listType =
                            if (listType == ListType.LINEAR) ListType.GRID else ListType.LINEAR
                    }) {
                        Icon(
                            imageVector = if (listType == ListType.GRID) Icons.Default.List else Icons.Default.GridView,
                            contentDescription = "list",
                            tint = getTextColor()
                        )
                    }
                }
            )
        }
    ) { paddingValues ->


        val repoPagingItems = viewModel.repoStateFlow.collectAsLazyPagingItems()


        MainScreenContent(
            modifier = modifier.padding(paddingValues),
            repoPagingItems,
            listType,
            imageHeight
        ) {
            setRepoSelected(it, viewModel)
            navigateRepoDetail(navController)
        }
    }
}

fun searchRepo(query: String, viewModel: MainViewModel) {
    viewModel.searchRepo(query = query)
}

fun navigateRepoDetail(navController: NavHostController) {
    navController.navigate(Screen.ImageDetail.route)
}

@ExperimentalCoroutinesApi
fun setRepoSelected(repo: Repo, viewModel: MainViewModel) {
    viewModel.setSelectedRepo(repo)
}

@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    repoPagingItems: LazyPagingItems<Repo>,
    listType: ListType,
    imageHeight: Dp,
    onCardClick: (Repo) -> Unit
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {

        when (repoPagingItems.loadState.refresh) {
            is LoadState.Error -> ErrorComposable(){
                repoPagingItems.refresh()
            }
            is LoadState.Loading -> LoadingComposable()
            else -> {
                RepoList(
                    modifier = Modifier.fillMaxSize(),
                    repos = repoPagingItems,
                    listType = listType,
                    imageHeight = imageHeight
                ) {
                    onCardClick(it)
                }
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun RepoList(
    modifier: Modifier = Modifier,
    repos: LazyPagingItems<Repo>,
    listType: ListType,
    imageHeight: Dp,
    onCardClick: (Repo) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when (listType) {
            ListType.LINEAR -> itemsIndexed(items = repos, key = null) { index, item ->
                val translationXAnimState = getTranslationXAnim(index)
                RepoItem(
                    modifier = Modifier
                        .padding(vertical = spaceBetweenImages)
                        .graphicsLayer {
                            translationX = translationXAnimState.value
                        },
                    repo = item!!,
                    imageHeight = imageHeight
                ) {
                    onCardClick(it)
                }

            }

            ListType.GRID -> {

                itemsIndexed(items = repos, key = null) { index, item ->

                    Row(Modifier.fillMaxWidth()) {
                        if (index % 2 != 0) {
                            val chunkedItem = listOf(repos[index], repos[index + 1])
                            chunkedItem.forEachIndexed { i: Int, repo: Repo? ->

                                val translationXAnimState = getTranslationXAnim(i)

                                repo?.let {
                                    RepoItem(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(spaceBetweenImages)
                                            .graphicsLayer {
                                                translationX = translationXAnimState.value
                                            },
                                        repo = repo,
                                        imageHeight = imageHeight
                                    ) {
                                        onCardClick(it)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        when (repos.loadState.append) {
            is LoadState.Loading -> item { LoadingComposable() }
            is LoadState.Error -> item { ErrorComposable(){
                repos.retry()
            } }
            else -> {}
        }
    }
}

@Composable
fun getTranslationXAnim(index: Int): State<Float> {
    var translationXState by remember {
        mutableStateOf(if (index % 2 == 0) -1000f else 1000f)
    }

    val translationXAnimState = animateFloatAsState(targetValue = translationXState)

    LaunchedEffect(Unit) {
        if (translationXState < 0f) {
            while (translationXState < 0f) {
                translationXState = translationXState.plus(100f)
                delay(35L)
            }
        } else {
            while (translationXState > 0f) {
                translationXState = translationXState.minus(100f)
                delay(35L)
            }
        }
    }

    return translationXAnimState
}

@Composable
private fun RepoItem(
    modifier: Modifier = Modifier,
    repo: Repo,
    imageHeight: Dp,
    onCardClick: (Repo) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        RepoCard(
            repo = repo,
            modifier = Modifier.height(imageHeight),
            onClick = {
                onCardClick(it)
            }
        ) { repo ->
            Text(
                text = repo.userName.take(15),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.h6,
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = 8.dp, y = (-8).dp)
            )
        }
        Spacer(modifier = Modifier.height(spaceBetweenImages))
    }
}