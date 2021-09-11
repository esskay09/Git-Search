package com.terranullius.gitsearch.business.interactors.repository

import com.terranullius.gitsearch.business.interactors.MainRepoInteractors
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


}