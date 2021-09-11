package com.terranullius.gitsearch.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.terranullius.gitsearch.business.data.cache.abstraction.RepoCacheDataSource
import com.terranullius.gitsearch.business.data.cache.implementation.RepoCacheDataSourceImpl
import com.terranullius.gitsearch.business.data.network.abstraction.GitNetworkDataSource
import com.terranullius.gitsearch.business.data.network.implementation.GitNetworkDataSourceImpl
import com.terranullius.gitsearch.business.interactors.MainRepoInteractors
import com.terranullius.gitsearch.business.interactors.SearchRepos
import com.terranullius.gitsearch.framework.datasource.network.abstraction.GitNetworkService
import com.terranullius.gitsearch.framework.datasource.network.implementation.ApiService
import com.terranullius.gitsearch.framework.datasource.network.implementation.GitNetworkServiceImpl
import com.terranullius.gitsearch.framework.datasource.network.mappers.NetworkMapper
import com.terranullius.gitsearch.business.interactors.MainRepository
import com.terranullius.gitsearch.framework.datasource.cache.abstraction.RepoDaoService
import com.terranullius.gitsearch.framework.datasource.cache.database.RepoDao
import com.terranullius.gitsearch.framework.datasource.cache.database.RepoDatabase
import com.terranullius.gitsearch.framework.datasource.cache.implementation.RepoDaoServiceImpl
import com.terranullius.gitsearch.framework.datasource.cache.mappers.CacheMapper
import com.terranullius.gitsearch.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(
        moshi: Moshi
    ): ApiService {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesNetworkMapper(): NetworkMapper {
        return NetworkMapper()
    }

    @Singleton
    @Provides
    fun providesImageNetworkService(
        apiService: ApiService,
        networkMapper: NetworkMapper
    ): GitNetworkService {
        return GitNetworkServiceImpl(
            networkMapper,
            apiService
        )
    }

    @Singleton
    @Provides
    fun providesImageNetworkDataSource(gitNetworkService: GitNetworkService): GitNetworkDataSource {
        return GitNetworkDataSourceImpl(gitNetworkService)
    }

    @Singleton
    @Provides
    fun providesCacheMapper(): CacheMapper {
        return CacheMapper()
    }

    @Singleton
    @Provides
    fun providesRepoDao(
        @ApplicationContext context: Context
    ): RepoDao {
        return Room.databaseBuilder(
            context,
            RepoDatabase::class.java,
            "repo_db"
        ).build().getRepoDao()
    }

    @Singleton
    @Provides
    fun providesRepoDaoService(
        repoDao: RepoDao,
        cacheMapper: CacheMapper
    ): RepoDaoService {
        return RepoDaoServiceImpl(
            repoDao,
            cacheMapper
        )
    }

    @Singleton
    @Provides
    fun providesRepoCacheDataSource(repoDaoService: RepoDaoService): RepoCacheDataSource {
        return RepoCacheDataSourceImpl(
            repoDaoService
        )
    }

    @Singleton
    @Provides
    fun providesSearchReposUseCase(gitNetworkDataSource: GitNetworkDataSource): SearchRepos {
        return SearchRepos(
            gitNetworkDataSource
        )
    }

    @Singleton
    @Provides
    fun providesMainRepoInteractors(searchRepos: SearchRepos): MainRepoInteractors {
        return MainRepoInteractors(searchRepos)
    }

    @Singleton
    @Provides
    fun providesMainRepository(mainRepoInteractors: MainRepoInteractors): MainRepository {
        return MainRepository(mainRepoInteractors)
    }

}