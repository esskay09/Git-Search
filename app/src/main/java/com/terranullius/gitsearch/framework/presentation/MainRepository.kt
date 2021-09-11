package com.terranullius.gitsearch.framework.presentation

import com.terranullius.gitsearch.business.interactors.imagelist.MainRepoInteractors
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