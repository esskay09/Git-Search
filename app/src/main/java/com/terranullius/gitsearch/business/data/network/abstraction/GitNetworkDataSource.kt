package com.terranullius.gitsearch.business.data.network.abstraction

import com.terranullius.gitsearch.business.domain.model.Repo


interface GitNetworkDataSource {

    suspend fun searchRepos(
        query: String,
        page: Int = 1,
        itemCountPerPage: Int = 20
    ): List<Repo>

}