package com.terranullius.gitsearch.business.data.network.implementation

import com.terranullius.gitsearch.business.data.network.abstraction.GitNetworkDataSource
import com.terranullius.gitsearch.business.domain.model.Repo
import com.terranullius.gitsearch.business.domain.model.User
import com.terranullius.gitsearch.framework.datasource.network.abstraction.GitNetworkService
import javax.inject.Inject

class GitNetworkDataSourceImpl @Inject constructor(
    private val gitNetworkService: GitNetworkService
) : GitNetworkDataSource {

    override suspend fun searchRepos(query: String, page: Int, itemCountPerPage: Int): List<Repo> {
        return gitNetworkService.searchRepos(
            query = query,
            page = page,
            itemCountPerPage = itemCountPerPage
        )
    }

    override suspend fun getContributors(repoName: String): List<User> {
        return gitNetworkService.getContributors(repoName)
    }
}