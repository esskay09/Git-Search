package com.terranullius.gitsearch.framework.datasource.network.model.allrepos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class GitSearchRepositoryResponse(

    @Json(name = "items")
    val repos: List<RepoDto>,

    @Json(name = "total_count")
    val totalCountR: Int?
) {

    val totalCount = totalCountR
        get() = field ?: 0

}

