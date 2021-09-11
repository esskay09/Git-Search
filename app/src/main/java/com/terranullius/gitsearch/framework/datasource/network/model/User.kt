package com.terranullius.gitsearch.framework.datasource.network.model

data class User(
    val id: Int,
    val avatar_url: String,
    val html_url: String,
    val login: String,
    val repos_url: String,
    val site_admin: Boolean,
    val starred_url: String,
    val subscriptions_url: String,
    val type: String,
    val url: String
)