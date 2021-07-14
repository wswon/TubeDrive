package com.tube.driver.di

import com.tube.driver.data.remote.PlaceRemoteDataSource
import com.tube.driver.data.remote.PlaceRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindPlaceRemoteDataSource(placeRemoteDataSourceImpl: PlaceRemoteDataSourceImpl): PlaceRemoteDataSource
}