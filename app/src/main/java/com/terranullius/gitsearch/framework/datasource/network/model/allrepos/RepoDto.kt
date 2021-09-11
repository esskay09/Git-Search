package com.terranullius.gitsearch.framework.datasource.network.model.allrepos

import com.squareup.moshi.Json
import com.terranullius.gitsearch.framework.datasource.network.model.User

data class RepoDto(

    @Json(name = "id")
    val id: Int,

    @Json(name = "contributors_url")
    val contributorsUrl: String,

    @Json(name = "name")
    val username: String,

    @Json(name = "description")
    val description: String,

    @Json(name = "forks_count")
    val forks: Int,

    @Json(name = "html_url")
    val repoUrl: String,

    @Json(name = "language")
    val language: String,

    @Json(name = "license")
    val license: License,

    @Json(name = "open_issues_count")
    val openIssues: Int,

    @Json(name = "owner")
    val owner: User,

    @Json(name = "stargazers_count")
    val stargazers: Int,

    @Json(name = "watchers_count")
    val watchers: Int,
)