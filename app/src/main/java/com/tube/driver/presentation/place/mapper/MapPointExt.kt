package com.tube.driver.presentation.place.mapper

import com.tube.driver.domain.model.entity.LatLng
import net.daum.mf.map.api.MapPoint

fun MapPoint.GeoCoordinate.toLatLng(): LatLng {
    return LatLng(latitude, longitude)
}