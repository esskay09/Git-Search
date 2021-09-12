package com.terranullius.gitsearch.business.domain.model

import com.squareup.moshi.Json

data class User(
    val id: Int,
    val avatarUrl: String,
    val username: String,
    val reposUrl: String
)