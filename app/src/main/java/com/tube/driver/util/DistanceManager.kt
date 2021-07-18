package com.tube.driver.util

import com.tube.driver.domain.model.entity.LatLng
import kotlin.math.*

object DistanceManager {

    private const val R = 6372.8 * 1000

    /* 두 좌표사이 거리 */
    fun getDistance(latLng1: LatLng, latLng2: LatLng): Int {
        val dLat = Math.toRadians(latLng2.latitude - latLng1.latitude)
        val dLon = Math.toRadians(latLng2.longitude - latLng1.longitude)
        val a = sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) *
            cos(Math.toRadians(latLng1.latitude)) *
            cos(Math.toRadians(latLng2.latitude))
        val c = 2 * asin(sqrt(a))
        return (R * c).toInt()
    }

    /* 두 좌표 중간 좌표 */
    fun midPoint(latLng1: LatLng, latLng2: LatLng): LatLng {
        var lat1 = latLng1.latitude
        var lon1 = latLng1.longitude
        var lat2 = latLng2.latitude
        val dLon = Math.toRadians(latLng2.longitude - lon1)

        lat1 = Math.toRadians(lat1)
        lat2 = Math.toRadians(lat2)
        lon1 = Math.toRadians(lon1)
        val bX = cos(lat2) * cos(dLon)
        val bY = cos(lat2) * sin(dLon)
        val lat3 = atan2(
            sin(lat1) + sin(lat2),
            sqrt((cos(lat1) + bX) * (cos(lat1) + bX) + bY * bY)
        )
        val lon3 = lon1 + atan2(bY, cos(lat1) + bX)

        return LatLng(Math.toDegrees(lat3), Math.toDegrees(lon3))
    }
}