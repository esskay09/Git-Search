package com.terranullius.gitsearch.framework.datasource.network.model

import com.squareup.moshi.Json

data class User(

    @Json(name = "id")
    val id: Int,

    @Json(name = "avatar_url")
    val avatarUrl: String,

    @Json(name = "login")
    val username: String,

    @Json(name = "repos_url")
    val reposUrl: String,
)