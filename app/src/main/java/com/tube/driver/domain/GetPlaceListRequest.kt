package com.tube.driver.domain

import com.tube.driver.domain.entity.LatLng

data class GetPlaceListRequest(
    val categoryCode: String,
    val latLng: LatLng,
    val page: Int
)