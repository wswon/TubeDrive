package com.tube.driver.data.remote

import com.tube.driver.data.response.PlaceResponse
import com.tube.driver.domain.CategoryCode
import com.tube.driver.domain.entity.LatLng
import io.reactivex.rxjava3.core.Single

interface PlaceRemoteDataSource {
    fun getAddressByCategory(
        categoryCode: CategoryCode,
        latLng: LatLng
    ): Single<List<PlaceResponse>>
}