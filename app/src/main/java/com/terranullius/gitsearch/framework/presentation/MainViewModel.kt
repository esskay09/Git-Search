package com.terranullius.gitsearch.framework.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terranullius.gitsearch.business.domain.model.Repo
import com.terranullius.gitsearch.business.domain.state.StateResource
import com.terranullius.gitsearch.business.interactors.imagelist.MainRepoInteractors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject


/**
 * ViewModel for MainActivity and Composable Screens
 * */

@ExperimentalCoroutinesApi
@FlowPreview
@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepoInteractors: MainRepoInteractors
) : ViewModel() {

    private val _searchQueryStateFLow: MutableStateFlow<String> = MutableStateFlow("esskay099")
    val searchQueryStateFLow: StateFlow<String>
        get() = _searchQueryStateFLow

    private val _repoStateFlow: MutableStateFlow<StateResource<List<Repo>>> =
        MutableStateFlow(StateResource.None)

    val repoStateFlow: StateFlow<StateResource<List<Repo>>>
        get() = _repoStateFlow


    private val _selectedRepo = mutableStateOf<Repo?>(null)
    val selectedRepo: State<Repo?>
        get() = _selectedRepo

    init {

        /**
         * Make Safe API requests with an interval of atleast 300ms, only for distinct query
         * */

        viewModelScope.launch {
            _searchQueryStateFLow.debounce(300)
                .filter { query ->
                    if (query.isBlank()) return@filter false else true
                }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    queryApi(query)
                }
                .collect {
                    _repoStateFlow.value = it
                }
        }
    }

    fun setSelectedRepo(repo: Repo) {
        _selectedRepo.value = repo
    }

    fun searchRepos(query: String) {
        _searchQueryStateFLow.value = query
    }

    private fun queryApi(query: String): Flow<StateResource<List<Repo>>> {
        return mainRepoInteractors.searchRepos.searchRepos(query, page = 1)
    }


}