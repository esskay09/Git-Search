package com.terranullius.gitsearch.framework.datasource.network.abstraction

import com.terranullius.gitsearch.business.domain.model.Repo


interface GitNetworkService {
    suspend fun searchRepos(
        query: String,
        page: Int = 1,
        itemCountPerPage: Int = 20
    ): List<Repo>
}