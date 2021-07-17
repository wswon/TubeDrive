package com.tube.driver.presentation.mapper

import com.tube.driver.domain.entity.LatLng
import net.daum.mf.map.api.MapPoint

fun MapPoint.GeoCoordinate.toLatLng(): LatLng {
    return LatLng(latitude, longitude)
}