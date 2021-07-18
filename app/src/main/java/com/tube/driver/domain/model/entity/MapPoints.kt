package com.tube.driver.domain.model.entity

data class MapPoints(
    val leftBottom: LatLng,
    val rightTop: LatLng,
    val center: LatLng
)