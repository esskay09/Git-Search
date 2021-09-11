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
import coil.annotation.ExperimentalCoilApi
import com.terranullius.gitsearch.business.domain.model.Repo
import com.terranullius.gitsearch.business.domain.state.StateResource
import com.terranullius.gitsearch.framework.presentation.MainViewModel
import com.terranullius.gitsearch.framework.presentation.composables.components.ErrorComposable
import com.terranullius.gitsearch.framework.presentation.composables.components.RepoCard
import com.terranullius.gitsearch.framework.presentation.composables.components.LoadingComposable
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
                            searchImage(it, viewModel)
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
                                        viewModel.searchRepos("")
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
        val imageStateFlow = viewModel.repoStateFlow.collectAsState()
        MainScreenContent(
            modifier = modifier.padding(paddingValues),
            imageStateFlow,
            listType,
            imageHeight
        ) {
            setImageSelected(it, viewModel)
            navigateImageDetail(navController)
        }
    }
}

fun searchImage(query: String, viewModel: MainViewModel) {
    viewModel.searchRepos(query = query)
}

fun navigateImageDetail(navController: NavHostController) {
    navController.navigate(Screen.ImageDetail.route)
}

@ExperimentalCoroutinesApi
fun setImageSelected(repo: Repo, viewModel: MainViewModel) {
    viewModel.setSelectedRepo(repo)
}

@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    imageStateFlow: State<StateResource<List<Repo>>>,
    listType: ListType,
    imageHeight: Dp,
    onCardClick: (Repo) -> Unit
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        when (imageStateFlow.value) {
            is StateResource.Loading -> {
                LoadingComposable()
            }
            is StateResource.Error -> {
                val errorMsg = (imageStateFlow.value as StateResource.Error).message
                ErrorComposable(msg = errorMsg.substringAfter("Reason:"))
            }
            is StateResource.Success -> {
                val repoList = (imageStateFlow.value as StateResource.Success<List<Repo>>).data

                RepoList(
                    modifier = Modifier.fillMaxSize(),
                    repos = repoList,
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
    repos: List<Repo>,
    listType: ListType,
    imageHeight: Dp,
    onCardClick: (Repo) -> Unit,
) {
    LazyColumn(modifier = modifier) {

        when (listType) {
            ListType.LINEAR -> itemsIndexed(repos) { index: Int, item: Repo ->

                val translationXAnimState = getTranslationXAnim(index)

                RepoItem(
                    modifier = Modifier
                        .padding(vertical = spaceBetweenImages)
                        .graphicsLayer {
                            translationX = translationXAnimState.value
                        },
                    repo = item,
                    imageHeight = imageHeight
                ) {
                    onCardClick(it)
                }
            }

            ListType.GRID -> {

                val chunkedList = repos.chunked(2)

                itemsIndexed(chunkedList) { _: Int, chunkedItem: List<Repo> ->

                    Row(Modifier.fillMaxWidth()) {
                        chunkedItem.forEachIndexed { index: Int, image: Repo ->

                            val translationXAnimState = getTranslationXAnim(index)

                            RepoItem(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(spaceBetweenImages)
                                    .graphicsLayer {
                                        translationX = translationXAnimState.value
                                    },
                                repo = image,
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