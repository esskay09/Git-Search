package com.terranullius.gitsearch.business.interactors

import com.terranullius.gitsearch.business.data.network.ApiResponseHandler
import com.terranullius.gitsearch.business.data.network.NetworkErrors
import com.terranullius.gitsearch.business.data.network.abstraction.GitNetworkDataSource
import com.terranullius.gitsearch.business.data.util.safeApiCall
import com.terranullius.gitsearch.business.domain.model.Repo
import com.terranullius.gitsearch.business.domain.model.User
import com.terranullius.gitsearch.business.domain.state.StateResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetContributors @Inject constructor(private val gitNetworkDataSource: GitNetworkDataSource) {

    fun getContributors(
        repoName: String
    ): Flow<StateResource<List<User>>> = flow {

        emit(StateResource.Loading)

        val apiResult = safeApiCall(Dispatchers.IO) {
            gitNetworkDataSource.getContributors(repoName)
        }

        val apiResponse = object : ApiResponseHandler<List<User>>(
            apiResult
        ) {
            override suspend fun handleSuccess(resultObj: List<User>): StateResource<List<User>> {
                return if (resultObj.isNotEmpty()) StateResource.Success(resultObj) else StateResource.Error(
                    message = NetworkErrors.NETWORK_DATA_NULL
                )
            }
        }.getResult()

        apiResponse?.let {
            emit(it)
        } ?: emit(StateResource.Error(message = NetworkErrors.NETWORK_ERROR_UNKNOWN))

    }
}
