package com.terranullius.gitsearch.framework.datasource.network.implementation

import com.terranullius.gitsearch.business.domain.model.Repo
import com.terranullius.gitsearch.business.domain.model.User
import com.terranullius.gitsearch.framework.datasource.network.abstraction.GitNetworkService
import com.terranullius.gitsearch.framework.datasource.network.mappers.NetworkMapper
import com.terranullius.gitsearch.framework.datasource.network.mappers.toUser
import javax.inject.Inject

class GitNetworkServiceImpl @Inject constructor(
    private val networkMapper: NetworkMapper,
    private val apiService: ApiService
) : GitNetworkService {

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

    override suspend fun getContributors(repoName: String): List<User> {
        return apiService.getContributors(repoName).map {
            it.toUser()
        }
    }
}