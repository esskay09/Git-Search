package com.terranullius.gitsearch.framework.datasource.cache.database

import androidx.room.*
import com.terranullius.gitsearch.framework.datasource.cache.model.RepoCacheEntity

@Dao
interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRepo(repo: RepoCacheEntity): Long

    @Query("DELETE FROM saved_repos WHERE id < -1")
    suspend fun deleteAllRepo(): Int

    @Query("SELECT * FROM saved_repos")
    suspend fun getRepos(): List<RepoCacheEntity>

}