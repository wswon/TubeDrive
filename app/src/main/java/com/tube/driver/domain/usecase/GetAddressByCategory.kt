package com.tube.driver.domain.usecase

import com.tube.driver.domain.entity.LatLng
import com.tube.driver.domain.entity.Place
import com.tube.driver.domain.repository.PlaceRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetAddressByCategory @Inject constructor(
    private val repository: PlaceRepository
) {

    operator fun invoke(
        categoryCode: String,
        latLng: LatLng
    ): Single<List<Place>> {
        return repository.getAddressByCategory(categoryCode, latLng)
    }
}