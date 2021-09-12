package com.terranullius.gitsearch.framework.datasource.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "saved_repos")
data class RepoCacheEntity(

    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val license: String,
    val contributorsUrl: String,
    val description: String,
    val forks: Int,
    val repoUrl: String,
    val ownerAvatarUrl: String,
    val ownerUserName: String,
    val watchers: Int,
    val language: String,
    val stargazers: Int,
    val name: String,
    val fullName: String,
    val openIssues: Int,
)

