package com.tube.driver.data.remote

import com.tube.driver.data.response.PlaceResultResponse
import com.tube.driver.domain.model.dto.GetPlaceListRequest
import io.reactivex.rxjava3.core.Single

interface PlaceRemoteDataSource {
    fun getPlaceListByCategory(
        getPlaceListRequest: GetPlaceListRequest
    ): Single<PlaceResultResponse>
}