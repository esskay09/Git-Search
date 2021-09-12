package com.terranullius.gitsearch.business.interactors.repository

import android.util.Log
import com.terranullius.gitsearch.business.domain.model.Repo
import com.terranullius.gitsearch.business.domain.model.User
import com.terranullius.gitsearch.business.domain.state.StateResource
import com.terranullius.gitsearch.business.interactors.MainRepoInteractors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val mainRepoInteractors: MainRepoInteractors
) {
    fun searchRepo(query: String): RepoPagingSource {
        return RepoPagingSource(
            searchRepos = mainRepoInteractors.searchRepos,
            query = query
        )
    }

    suspend fun getSavedRepos(): Flow<StateResource<List<Repo>>> {
        return mainRepoInteractors.cacheInteractor.getSavedRepos()
    }

    suspend fun insertRepo(repo: Repo){
        mainRepoInteractors.cacheInteractor.insertRepo(repo)
    }

    suspend fun deleteAllRepo(){
        mainRepoInteractors.cacheInteractor.deleteAllRepo()
    }

    fun getContributors(repoName: String): Flow<StateResource<List<User>>> {
        return mainRepoInteractors.getContributors.getContributors(repoName)
    }

}