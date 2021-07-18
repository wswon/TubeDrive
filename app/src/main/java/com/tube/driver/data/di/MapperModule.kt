package com.tube.driver.data.di

import com.tube.driver.data.mapper.PlaceResponseMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {

    @Singleton
    @Provides
    fun providePlaceResponseMapper(): PlaceResponseMapper {
        return PlaceResponseMapper()
    }
}