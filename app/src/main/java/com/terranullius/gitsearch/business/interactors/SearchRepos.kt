package com.terranullius.gitsearch.business.interactors

import com.terranullius.gitsearch.business.data.network.ApiResponseHandler
import com.terranullius.gitsearch.business.data.network.NetworkErrors
import com.terranullius.gitsearch.business.data.network.abstraction.GitNetworkDataSource
import com.terranullius.gitsearch.business.data.util.safeApiCall
import com.terranullius.gitsearch.business.domain.model.Repo
import com.terranullius.gitsearch.business.domain.state.StateResource
import kotlinx.coroutines.Dispatchers.IO
import javax.inject.Inject

/**
 * Use case for getting all repos from API
 * */

class SearchRepos @Inject constructor(private val gitNetworkDataSource: GitNetworkDataSource) {

    suspend fun searchRepos(
        query: String,
        page: Int
    ): StateResource<List<Repo>> {

        val apiResult = safeApiCall(IO) {
            gitNetworkDataSource.searchRepos(
                query = query,
                page = page
            )
        }

        val apiResponse = object : ApiResponseHandler<List<Repo>>(
            apiResult
        ) {
            override suspend fun handleSuccess(resultObj: List<Repo>): StateResource<List<Repo>> {
                return if (resultObj.isNotEmpty()) StateResource.Success(resultObj) else StateResource.Error(
                    message = NetworkErrors.NETWORK_DATA_NULL
                )
            }
        }.getResult()

        return apiResponse?.let {
            return@let it
        } ?: StateResource.Error(message = NetworkErrors.NETWORK_ERROR_UNKNOWN)

    }
}
