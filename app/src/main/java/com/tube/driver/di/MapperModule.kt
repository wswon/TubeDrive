package com.tube.driver.di

import com.tube.driver.data.mapper.PlaceResponseMapper
import com.tube.driver.presentation.mapper.PlaceMapper
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

    @Singleton
    @Provides
    fun providePlaceMapper(): PlaceMapper {
        return PlaceMapper()
    }
}