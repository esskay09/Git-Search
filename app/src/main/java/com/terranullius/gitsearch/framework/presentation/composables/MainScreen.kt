package com.terranullius.gitsearch.framework.presentation.composables

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.terranullius.gitsearch.business.domain.state.StateResource
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
        val savedRepos = viewModel.savedRepoStateFlow.collectAsState()

        MainScreenContent(
            modifier = modifier.padding(paddingValues),
            repoPagingItems = repoPagingItems,
            savedRepos = savedRepos,
            listType = listType,
            imageHeight = imageHeight,
            viewModel = viewModel
        ) {
            setRepoSelected(it, viewModel)
            navigateRepoDetail(navController)
        }
    }
}


@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    repoPagingItems: LazyPagingItems<Repo>,
    savedRepos: State<StateResource<List<Repo>>>,
    listType: ListType,
    imageHeight: Dp,
    viewModel: MainViewModel,
    onCardClick: (Repo) -> Unit
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {

        when (repoPagingItems.loadState.refresh) {
            is LoadState.Error -> {
                SavedRepoList(
                    modifier = Modifier.fillMaxSize(),
                    repos = null,
                    listType = listType,
                    imageHeight = imageHeight,
                    savedRepos = savedRepos.value,
                    viewModel = viewModel
                ) {
                    onCardClick(it)
                }
            }
            is LoadState.Loading -> LoadingComposable()
            else -> {
                viewModel.deleteAllRepo()
                RepoList(
                    modifier = Modifier.fillMaxSize(),
                    repos = repoPagingItems,
                    listType = listType,
                    imageHeight = imageHeight,
                    viewModel = viewModel,
                    savedRepos = emptyList()
                ) {
                    onCardClick(it)
                }
            }
        }
    }
}

@Composable
fun BoxScope.SavedRepoList(
    modifier: Modifier = Modifier,
    repos: LazyPagingItems<Repo>?,
    savedRepos: StateResource<List<Repo>>,
    listType: ListType,
    imageHeight: Dp,
    viewModel: MainViewModel,
    onCardClick: (Repo) -> Unit,
) {
    when (savedRepos) {
        is StateResource.Loading -> LoadingComposable()
        is StateResource.Error -> {
            ErrorComposable() {
                repos?.refresh()
            }
        }
        is StateResource.Success -> {
            RepoList(
                repos = null,
                savedRepos = savedRepos.data,
                listType = listType,
                imageHeight = imageHeight,
                viewModel = viewModel
            ) {
                onCardClick(it)
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun RepoList(
    modifier: Modifier = Modifier,
    repos: LazyPagingItems<Repo>?,
    savedRepos: List<Repo>,
    listType: ListType,
    imageHeight: Dp,
    viewModel: MainViewModel,
    onCardClick: (Repo) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when (listType) {
            ListType.LINEAR -> {
                repos?.let { lazyItems ->
                    itemsIndexed(
                        items = lazyItems,
                        key = null
                    ) { index, item ->

                        LinearContent(
                            index = index,
                            item = item,
                            savedItem = savedRepos.getOrNull(index),
                            imageHeight = imageHeight,
                            viewModel = viewModel,
                            onCardClick = onCardClick
                        )
                    }
                } ?: itemsIndexed(savedRepos) { index, item ->
                    LinearContent(
                        index = index,
                        item = null,
                        savedItem = item,
                        imageHeight = imageHeight,
                        viewModel = viewModel,
                        onCardClick = onCardClick
                    )
                }
            }

            ListType.GRID -> {
                repos?.let { lazyItems ->
                    itemsIndexed(items = lazyItems, key = null) { index, item ->
                        GridContent(
                            index = index,
                            repos = repos,
                            savedRepos = savedRepos,
                            imageHeight = imageHeight,
                            viewModel = viewModel,
                            onCardClick = onCardClick
                        )
                    }
                } ?: itemsIndexed(savedRepos) { index, item ->
                    GridContent(
                        index = index,
                        repos = null,
                        savedRepos = savedRepos,
                        imageHeight = imageHeight,
                        viewModel = viewModel,
                        onCardClick = onCardClick
                    )
                }

            }
        }

        when (repos?.loadState?.append) {
            is LoadState.Loading -> item { LoadingComposable() }
            is LoadState.Error -> item {
                ErrorComposable() {
                    repos.retry()
                }
            }
            else -> {
            }
        }
    }
}

@Composable
private fun LinearContent(
    index: Int,
    item: Repo?,
    savedItem: Repo?,
    imageHeight: Dp,
    viewModel: MainViewModel,
    onCardClick: (Repo) -> Unit
) {

    LaunchedEffect(key1 = index) {
        viewModel.saveRepo(item)
    }

    val translationXAnimState = getTranslationXAnim(index)

    RepoItem(
        modifier = Modifier
            .padding(vertical = spaceBetweenImages)
            .graphicsLayer {
                translationX = translationXAnimState.value
            },
        repo = item ?: savedItem,
        imageHeight = imageHeight
    ) {
        onCardClick(it)
    }
}

@Composable
private fun GridContent(
    index: Int,
    repos: LazyPagingItems<Repo>?,
    savedRepos: List<Repo>,
    imageHeight: Dp,
    viewModel: MainViewModel,
    onCardClick: (Repo) -> Unit
) {

    LaunchedEffect(key1 = index) {
        viewModel.saveRepo(repos?.get(index))
    }

    Row(Modifier.fillMaxWidth()) {
        if (index % 2 != 0) {
            val chunkedItem = listOf(
                repos?.get(index) ?: savedRepos.getOrNull(index), repos?.get(index + 1)
                    ?: savedRepos.getOrNull(index + 1)
            )
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
    repo: Repo?,
    imageHeight: Dp,
    onCardClick: (Repo) -> Unit
) {

    repo?.let {
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

}