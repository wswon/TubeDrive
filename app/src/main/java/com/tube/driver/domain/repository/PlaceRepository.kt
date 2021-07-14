package com.tube.driver.domain.repository

import com.tube.driver.domain.entity.LatLng
import com.tube.driver.domain.entity.Place
import io.reactivex.rxjava3.core.Single

interface PlaceRepository {
    fun getAddressByCategory(
        categoryCode: String,
        latLng: LatLng
    ): Single<List<Place>>
}