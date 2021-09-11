package com.terranullius.gitsearch.business.data.cache.abstraction

import com.terranullius.gitsearch.business.domain.model.Repo

interface RepoCacheDataSource {

    suspend fun insertRepo(repo: Repo): Long

    suspend fun deleteAllRepo(): Int

    suspend fun getRepos() : List<Repo>

}