package com.tube.driver.domain.model

import com.tube.driver.domain.model.entity.Place

data class GetPlaceListResult(
    val placeList: List<Place>,
    val hasNextPage: Boolean
)