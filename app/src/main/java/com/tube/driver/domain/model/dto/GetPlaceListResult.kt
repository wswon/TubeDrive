package com.tube.driver.domain.model.dto

import com.tube.driver.domain.model.entity.Place

data class GetPlaceListResult(
    val placeList: List<Place>,
    val hasNextPage: Boolean
)