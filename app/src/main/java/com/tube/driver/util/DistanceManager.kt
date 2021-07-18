package com.tube.driver.util

import com.tube.driver.domain.model.entity.LatLng
import kotlin.math.*

object DistanceManager {

    private const val R = 6372.8 * 1000

    /**
     * 두 좌표의 거리를 계산한다.
     *
     * @param lat1 위도1
     * @param lon1 경도1
     * @param lat2 위도2
     * @param lon2 경도2
     * @return 두 좌표의 거리(m)
     */
    fun getDistance(latLng: LatLng, latLng2: LatLng): Int {
        val dLat = Math.toRadians(latLng2.latitude - latLng.latitude)
        val dLon = Math.toRadians(latLng2.longitude - latLng.longitude)
        val a = sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) *
            cos(Math.toRadians(latLng.latitude)) *
            cos(Math.toRadians(latLng2.latitude))
        val c = 2 * asin(sqrt(a))
        return (R * c).toInt()
    }
}