package com.tube.driver.domain.usecase

import com.tube.driver.domain.model.GetPlaceListRequest
import com.tube.driver.domain.model.GetPlaceListResult
import com.tube.driver.domain.repository.PlaceRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetPlaceListByCategory @Inject constructor(
    private val repository: PlaceRepository
) {

    operator fun invoke(
        getPlaceListRequest: GetPlaceListRequest
    ): Single<GetPlaceListResult> {
        return repository.getPlaceListByCategory(getPlaceListRequest)
    }
}