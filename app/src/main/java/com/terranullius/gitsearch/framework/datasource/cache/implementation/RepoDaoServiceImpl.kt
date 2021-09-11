package com.terranullius.gitsearch.framework.datasource.cache.implementation

import com.terranullius.gitsearch.framework.datasource.cache.abstraction.RepoDaoService
import com.terranullius.gitsearch.framework.datasource.cache.database.RepoDao
import com.terranullius.gitsearch.framework.datasource.cache.mappers.CacheMapper
import com.terranullius.gitsearch.business.domain.model.Repo
import javax.inject.Inject

class RepoDaoServiceImpl @Inject constructor(
    private val repoDao: RepoDao,
    private val cacheMapper: CacheMapper
) : RepoDaoService {


    override suspend fun insertRepo(repo: Repo): Long {
        val repoCacheEntity = cacheMapper.mapToEntity(repo)
        return repoDao.insertRepo(repoCacheEntity)
    }

    override suspend fun deleteAllRepo(): Int {
        return repoDao.deleteAllRepo()
    }

    override suspend fun getRepos(): List<Repo> {

        val repoEntityList = repoDao.getRepos()
        val repoList = cacheMapper.mapListEntityToListDomain(repoEntityList)

        return repoList
    }
}