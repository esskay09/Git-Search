package com.terranullius.gitsearch.framework.datasource.network.implementation

import com.terranullius.gitsearch.business.domain.model.Repo
import com.terranullius.gitsearch.framework.datasource.network.abstraction.RepoNetworkService
import com.terranullius.gitsearch.framework.datasource.network.mappers.NetworkMapper
import javax.inject.Inject

class RepoNetworkServiceImpl @Inject constructor(
    private val networkMapper: NetworkMapper,
    private val apiService: ApiService
) : RepoNetworkService {

    override suspend fun searchRepos(
        query: String,
        page: Int,
        itemCountPerPage: Int
    ): List<Repo> {

        val response = apiService.searchRepos(
            query = query,
            page = page,
            itemCountPerPage = itemCountPerPage
        )

        return networkMapper.toRepoList(
            response
        )
    }
}