package com.tube.driver.domain

import com.tube.driver.domain.entity.MapPoints

data class GetPlaceListRequest(
    val categoryCode: String,
    val mapPoints: MapPoints,
    val page: Int
) {
    fun getRectRequest(): String {
        return mapPoints.leftBottom.longitude.toString() +
            "," + mapPoints.leftBottom.latitude.toString() +
            "," + mapPoints.rightTop.longitude.toString() +
            "," + mapPoints.rightTop.latitude.toString()
    }
}