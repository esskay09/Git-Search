package com.terranullius.gitsearch.business.interactors

import javax.inject.Inject

// Use cases
data class MainRepoInteractors @Inject constructor(
    val searchRepos: SearchRepos,
    val cacheInteractor: CacheInteractor
)
