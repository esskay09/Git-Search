package com.terranullius.gitsearch.framework.datasource.network.model.allrepos

import com.squareup.moshi.Json

data class GitSearchAllRepositoryResponse(

    @Json(name = "items")
    val repos: List<RepoDto>,
    val total_count: Int
)