package com.tube.driver.domain.usecase

import com.tube.driver.domain.GetPlaceListRequest
import com.tube.driver.domain.entity.Place
import com.tube.driver.domain.repository.PlaceRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetPlaceListByCategory @Inject constructor(
    private val repository: PlaceRepository
) {

    operator fun invoke(
        getPlaceListRequest: GetPlaceListRequest
    ): Single<List<Place>> {
        return repository.getPlaceListByCategory(getPlaceListRequest)
    }
}