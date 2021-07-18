package com.tube.driver.domain.repository

import com.tube.driver.domain.model.GetPlaceListRequest
import com.tube.driver.domain.model.GetPlaceListResult
import io.reactivex.rxjava3.core.Single

interface PlaceRepository {
    fun getPlaceListByCategory(
        getPlaceListRequest: GetPlaceListRequest
    ): Single<GetPlaceListResult>
}