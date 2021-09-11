package com.terranullius.gitsearch.framework.datasource.network.implementation

import com.terranullius.gitsearch.framework.datasource.network.model.allrepos.GitSearchRepositoryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("repositories")
    suspend fun searchRepos(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") itemCountPerPage: Int = 10
    ) : GitSearchRepositoryResponse

}