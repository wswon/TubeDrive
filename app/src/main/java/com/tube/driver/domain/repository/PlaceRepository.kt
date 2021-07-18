package com.tube.driver.domain.repository

import com.tube.driver.domain.model.dto.GetPlaceListRequest
import com.tube.driver.domain.model.dto.GetPlaceListResult
import io.reactivex.rxjava3.core.Single

interface PlaceRepository {
    fun getPlaceListByCategory(
        getPlaceListRequest: GetPlaceListRequest
    ): Single<GetPlaceListResult>
}