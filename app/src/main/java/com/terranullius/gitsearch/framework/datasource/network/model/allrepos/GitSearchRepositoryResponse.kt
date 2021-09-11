package com.terranullius.gitsearch.framework.datasource.network.model.allrepos

import com.squareup.moshi.Json

data class GitSearchRepositoryResponse(

    @Json(name = "items")
    val repos: List<RepoDto>,

    @Json(name = "total_count")
    val totalCount: Int
)