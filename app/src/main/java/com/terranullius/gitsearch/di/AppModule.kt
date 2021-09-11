package com.terranullius.gitsearch.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.terranullius.gitsearch.business.data.network.abstraction.GitNetworkDataSource
import com.terranullius.gitsearch.business.data.network.implementation.GitNetworkDataSourceImpl
import com.terranullius.gitsearch.business.interactors.imagelist.MainRepoInteractors
import com.terranullius.gitsearch.business.interactors.imagelist.SearchRepos
import com.terranullius.gitsearch.framework.datasource.network.abstraction.GitNetworkService
import com.terranullius.gitsearch.framework.datasource.network.implementation.ApiService
import com.terranullius.gitsearch.framework.datasource.network.implementation.GitNetworkServiceImpl
import com.terranullius.gitsearch.framework.datasource.network.mappers.NetworkMapper
import com.terranullius.gitsearch.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun proviedsSearchReposUseCase(imageNetworkDataSource: GitNetworkDataSource): SearchRepos {
        return SearchRepos(
            imageNetworkDataSource
        )
    }

    @Singleton
    @Provides
    fun providesMainRepoInteractors(searchRepos: SearchRepos): MainRepoInteractors {
        return MainRepoInteractors(searchRepos)
    }

}