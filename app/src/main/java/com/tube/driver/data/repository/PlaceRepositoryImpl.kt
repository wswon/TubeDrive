package com.tube.driver.data.repository

import com.tube.driver.data.mapper.PlaceResponseMapper
import com.tube.driver.data.remote.PlaceRemoteDataSource
import com.tube.driver.domain.CategoryCode
import com.tube.driver.domain.entity.LatLng
import com.tube.driver.domain.entity.Place
import com.tube.driver.domain.repository.PlaceRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val remoteDataSource: PlaceRemoteDataSource,
    private val placeResponseMapper: PlaceResponseMapper
) : PlaceRepository {

    override fun getAddressByCategory(
        categoryCode: CategoryCode,
        latLng: LatLng
    ): Single<List<Place>> {
        return remoteDataSource.getAddressByCategory(categoryCode, latLng)
            .map(placeResponseMapper::transform)
    }
}