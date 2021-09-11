package com.terranullius.gitsearch.framework.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
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
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _searchQueryStateFLow: MutableStateFlow<String> = MutableStateFlow("compose")
    val searchQueryStateFLow: StateFlow<String>
        get() = _searchQueryStateFLow

    private val _repoStateFlow: MutableStateFlow<PagingData<Repo>> =
        MutableStateFlow(PagingData.empty())

    val repoStateFlow: StateFlow<PagingData<Repo>>
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
                .collectLatest {
                    _repoStateFlow.value = it
                }
        }
    }

    fun setSelectedRepo(repo: Repo) {
        _selectedRepo.value = repo
    }

    fun searchRepo(query: String) {
        _searchQueryStateFLow.value = query
    }

    fun queryApi(query: String): Flow<PagingData<Repo>> {
        val source = mainRepository.searchRepo(query = query)

        val flow = Pager(
            PagingConfig(
                pageSize = 20
            )
        ) {
            source
        }.flow
            .cachedIn(viewModelScope)

        return flow
    }


}