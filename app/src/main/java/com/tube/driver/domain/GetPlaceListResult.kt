package com.tube.driver.domain

import com.tube.driver.domain.entity.Place

data class GetPlaceListResult(
    val placeList: List<Place>,
    val hasNextPage: Boolean
)