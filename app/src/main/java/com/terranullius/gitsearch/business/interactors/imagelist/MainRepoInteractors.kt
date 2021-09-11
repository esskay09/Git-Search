package com.terranullius.gitsearch.business.interactors.imagelist

import javax.inject.Inject

// Use cases
data class MainRepoInteractors @Inject constructor(
    val searchRepos: SearchRepos
)
