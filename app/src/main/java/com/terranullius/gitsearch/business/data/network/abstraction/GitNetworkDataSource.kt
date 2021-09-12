package com.terranullius.gitsearch.business.data.network.abstraction

import com.terranullius.gitsearch.business.domain.model.Repo
import com.terranullius.gitsearch.business.domain.model.User


interface GitNetworkDataSource {

    suspend fun searchRepos(
        query: String,
        page: Int = 1,
        itemCountPerPage: Int = 20
    ): List<Repo>

    suspend fun getContributors(
        repoName: String
    ): List<User>

}