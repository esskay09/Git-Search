package com.terranullius.gitsearch.framework.datasource.cache.abstraction

import com.terranullius.gitsearch.business.domain.model.Repo


interface RepoDaoService {

    suspend fun insertRepo(repo: Repo): Long

    suspend fun deleteAllRepo(): Int

    suspend fun getRepos() : List<Repo>
}