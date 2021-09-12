package com.terranullius.gitsearch.framework.datasource.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


data class UserDto(

    @Json(name = "id")
    val idR: Int? = null,

    @Json(name = "avatar_url")
    val avatarUrlR: String? = null,

    @Json(name = "login")
    val usernameR: String? = null,

    @Json(name = "repos_url")
    val reposUrlR: String? = null,
) {

    val id = idR
        get() = field ?: 0


    val avatarUrl = avatarUrlR
        get() = field ?: ""


    val username = usernameR
        get() = field ?: ""


    val reposUrl = reposUrlR
        get() = field ?: ""

}