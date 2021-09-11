package com.terranullius.gitsearch.business.data.cache

import com.terranullius.gitsearch.business.data.cache.CacheErrors.CACHE_DATA_NULL
import com.terranullius.gitsearch.business.domain.state.StateResource


abstract class CacheResponseHandler<Data>(
    private val response: CacheResult<Data?>,
) {
    suspend fun getResult(): StateResource<Data> {

        return when (response) {

            is CacheResult.GenericError -> {
                StateResource.Error(
                    message = "Reason: ${response.errorMessage}"
                )
            }

            is CacheResult.Success -> {
                if (response.value == null) {
                    StateResource.Error(
                        message = "Reason: ${CACHE_DATA_NULL}."
                    )
                } else {
                    handleSuccess(resultObj = response.value)
                }
            }

        }
    }

    abstract suspend fun handleSuccess(resultObj: Data): StateResource<Data>

}