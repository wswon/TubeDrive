package com.tube.driver.data.di

import com.tube.driver.data.repository.PlaceRepositoryImpl
import com.tube.driver.domain.repository.PlaceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindPlaceRepository(placeRepositoryImpl: PlaceRepositoryImpl): PlaceRepository
}