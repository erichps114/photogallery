package me.erichps.photogallery.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.erichps.photogallery.data.RepositoryImpl
import me.erichps.photogallery.data.remote.RemoteDataSource
import me.erichps.photogallery.domain.provider.Repository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: RemoteDataSource): Repository = RepositoryImpl(remoteDataSource)
}