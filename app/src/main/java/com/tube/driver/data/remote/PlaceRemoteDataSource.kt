package com.tube.driver.data.remote

import com.tube.driver.data.response.PlaceResponse
import com.tube.driver.domain.GetPlaceListRequest
import io.reactivex.rxjava3.core.Single

interface PlaceRemoteDataSource {
    fun getPlaceListByCategory(
        getPlaceListRequest: GetPlaceListRequest
    ): Single<List<PlaceResponse>>
}