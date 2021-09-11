package com.terranullius.gitsearch.business.data.cache.implementation

import com.terranullius.gitsearch.business.data.cache.abstraction.RepoCacheDataSource
import com.terranullius.gitsearch.business.domain.model.Repo
import com.terranullius.gitsearch.framework.datasource.cache.abstraction.RepoDaoService
import javax.inject.Inject

class RepoCacheDataSourceImpl @Inject constructor(
    private val repoDaoService: RepoDaoService
) : RepoCacheDataSource {

    override suspend fun insertRepo(repo: Repo): Long {
        return repoDaoService.insertRepo(repo)
    }

    override suspend fun deleteAllRepo(): Int {
        return repoDaoService.deleteAllRepo()
    }

    override suspend fun getRepos(): List<Repo> {
        return repoDaoService.getRepos()
    }
}