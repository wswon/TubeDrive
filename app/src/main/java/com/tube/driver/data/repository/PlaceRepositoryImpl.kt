package com.tube.driver.data.repository

import com.tube.driver.data.mapper.PlaceResponseMapper
import com.tube.driver.data.remote.PlaceRemoteDataSource
import com.tube.driver.domain.model.dto.GetPlaceListRequest
import com.tube.driver.domain.model.dto.GetPlaceListResult
import com.tube.driver.domain.repository.PlaceRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val remoteDataSource: PlaceRemoteDataSource,
    private val placeResponseMapper: PlaceResponseMapper
) : PlaceRepository {

    override fun getPlaceListByCategory(
        getPlaceListRequest: GetPlaceListRequest
    ): Single<GetPlaceListResult> {
        return remoteDataSource.getPlaceListByCategory(getPlaceListRequest)
            .map(placeResponseMapper::transform)
    }
}