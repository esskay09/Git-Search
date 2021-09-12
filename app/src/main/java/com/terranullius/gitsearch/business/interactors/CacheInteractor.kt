package com.terranullius.gitsearch.business.interactors

import android.util.Log
import com.terranullius.gitsearch.business.data.cache.CacheErrors
import com.terranullius.gitsearch.business.data.cache.CacheResponseHandler
import com.terranullius.gitsearch.business.data.cache.abstraction.RepoCacheDataSource
import com.terranullius.gitsearch.business.data.util.safeCacheCall
import com.terranullius.gitsearch.business.domain.model.Repo
import com.terranullius.gitsearch.business.domain.state.StateResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CacheInteractor @Inject constructor(
    private val cacheDataSource: RepoCacheDataSource
) {
    suspend fun insertRepo(repo: Repo): Flow<StateResource<Long>> = flow {
        emit(StateResource.Loading)

        val cacheResult = safeCacheCall(Dispatchers.IO) {
            cacheDataSource.insertRepo(repo)
        }

        val result = object : CacheResponseHandler<Long>(cacheResult) {

            override suspend fun handleSuccess(resultObj: Long): StateResource<Long> {
                return if (resultObj > 0) StateResource.Success(resultObj)
                else StateResource.Error(message = CacheErrors.CACHE_ERROR_UNKNOWN)
            }
        }.getResult()

        emit(result)
    }

    suspend fun deleteAllRepo() = flow {
        emit(StateResource.Loading)

        val cacheResult = safeCacheCall(Dispatchers.IO) {
            cacheDataSource.deleteAllRepo()
        }

        val result = object : CacheResponseHandler<Int>(cacheResult) {
            override suspend fun handleSuccess(resultObj: Int) = StateResource.Success(resultObj)
        }.getResult()

        emit(result)
    }

    suspend fun getSavedRepos() = flow {
        emit(StateResource.Loading)
        val cacheResult = safeCacheCall(Dispatchers.IO) {
            cacheDataSource.getRepos()
        }
        val result = object : CacheResponseHandler<List<Repo>>(cacheResult) {
            override suspend fun handleSuccess(resultObj: List<Repo>) =
                StateResource.Success(resultObj)
        }.getResult()

        emit(result)
    }
}