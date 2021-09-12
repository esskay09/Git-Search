package com.terranullius.gitsearch.business.domain.model

/**
 * Domain model
 * */
data class Repo(
    val id: Int,
    val license: String,
    val contributorsUrl: String,
    val description: String,
    val forks: Int,
    val repoUrl: String,
    val owner: User,
    val watchers: Int,
    val language: String,
    val stargazers: Int,
    val name: String,
    val fullName: String,
    val openIssues: Int,
)