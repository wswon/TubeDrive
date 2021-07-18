package com.tube.driver.presentation.place.di

import com.tube.driver.presentation.place.mapper.PlaceMapper
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
    fun providePlaceMapper(): PlaceMapper {
        return PlaceMapper()
    }
}