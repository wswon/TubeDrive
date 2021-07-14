package com.tube.driver.di

import com.tube.driver.domain.repository.PlaceRepository
import com.tube.driver.domain.usecase.GetAddressByCategory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Singleton
    @Provides
    fun provideGetAddressByCategory(repository: PlaceRepository): GetAddressByCategory {
        return GetAddressByCategory(repository)
    }
}