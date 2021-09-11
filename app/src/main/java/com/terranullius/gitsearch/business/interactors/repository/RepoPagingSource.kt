package com.terranullius.gitsearch.business.interactors.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.terranullius.gitsearch.business.domain.model.Repo
import com.terranullius.gitsearch.business.domain.state.StateResource
import com.terranullius.gitsearch.business.interactors.SearchRepos

class RepoPagingSource(
    val searchRepos: SearchRepos,
    private val query: String
) : PagingSource<Int, Repo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {

        val nextPageNumber = params.key ?: 1

        try {
            val result = searchRepos.searchRepos(
                query = query,
                page = nextPageNumber
            )

            return if (result is StateResource.Success) {
                LoadResult.Page(
                    data = result.data,
                    prevKey = null,
                    nextKey = nextPageNumber + 1
                )
            } else LoadResult.Error(
                Throwable("Error")
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}