package com.tube.driver.data.repository

import com.tube.driver.data.mapper.PlaceResponseMapper
import com.tube.driver.data.remote.PlaceRemoteDataSource
import com.tube.driver.domain.GetPlaceListRequest
import com.tube.driver.domain.entity.Place
import com.tube.driver.domain.repository.PlaceRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val remoteDataSource: PlaceRemoteDataSource,
    private val placeResponseMapper: PlaceResponseMapper
) : PlaceRepository {

    override fun getPlaceListByCategory(
        getPlaceListRequest: GetPlaceListRequest
    ): Single<List<Place>> {
        return remoteDataSource.getPlaceListByCategory(getPlaceListRequest)
            .map(placeResponseMapper::transform)
    }
}