package com.terranullius.gitsearch.business.data.network

import android.util.Log
import com.terranullius.task.business.data.network.ApiResult
import com.terranullius.gitsearch.business.data.network.NetworkErrors.NETWORK_DATA_NULL
import com.terranullius.gitsearch.business.data.network.NetworkErrors.NETWORK_ERROR
import com.terranullius.gitsearch.business.domain.state.StateResource

/**
 * Helper class to handle all api responses and convert them into StateResource
* */

abstract class ApiResponseHandler <Data>(
    private val response: ApiResult<Data?>
){

    suspend fun getResult(): StateResource<Data>? {

        return when(response){

            is ApiResult.GenericError -> {
                Log.d("AppDebug","${response.errorMessage}")
                StateResource.Error(
                    message = "Reason: ${response.errorMessage.toString()}"
                )
            }

            is ApiResult.NetworkError -> {
                Log.d("AppDebug","${response}")
                StateResource.Error(
                    message = "Reason: ${NETWORK_ERROR}"
                )
            }

            is ApiResult.Success -> {
                if(response.value == null){
                    StateResource.Error(
                        message = "Reason: ${NETWORK_DATA_NULL}"
                    )
                }
                else{
                    handleSuccess(resultObj = response.value)
                }
            }

        }
    }

    abstract suspend fun handleSuccess(resultObj: Data): StateResource<Data>?

}