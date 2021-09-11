package com.terranullius.gitsearch.framework.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.terranullius.gitsearch.framework.datasource.cache.database.RepoDao
import com.terranullius.gitsearch.framework.datasource.cache.model.RepoCacheEntity

@Database(entities = [RepoCacheEntity::class], version = 1)
abstract class RepoDatabase: RoomDatabase() {

    abstract fun getRepoDao() : RepoDao
}