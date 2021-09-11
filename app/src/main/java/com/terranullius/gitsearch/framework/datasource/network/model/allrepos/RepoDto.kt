package com.terranullius.gitsearch.framework.datasource.network.model.allrepos

import com.squareup.moshi.Json
import com.terranullius.gitsearch.framework.datasource.network.model.User

data class RepoDto(
    val id: Int,
    val contributors_url: String,
    val name: String,
    val description: String,
    val forks_count: Int,
    val html_url: String,
    val language: String,
    val license: License,
    val open_issues_count: Int,
    @Json(name = "owner")
    val owner: User,
    val stargazers_count: Int,
    val watchers_count: Int,
)