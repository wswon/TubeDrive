package com.tube.driver.presentation.mapper

import com.tube.driver.domain.entity.Place
import com.tube.driver.presentation.PlaceItem
import java.text.DecimalFormat

class PlaceMapper {

    fun transform(place: Place): PlaceItem.Item {
        return place.run {
            PlaceItem.Item(
                id = id,
                name = placeName,
                distance = getDisplayDistance(distance),
                placeUrl = placeUrl,
                phoneNumber = phone,
                addressName = if (roadAddressName.isNotEmpty()) roadAddressName else addressName,
                category = category.middleCategory,
                subCategory = category.subCategory,
                latLng = latLng
            )
        }
    }

    fun transform(placeList: List<Place>): List<PlaceItem.Item> {
        return placeList.map(this::transform)
    }

    private fun getDisplayDistance(distance: String): String =
        when {
            distance.length > 3 -> DecimalFormat("###,###.#").format(distance.toInt() / 1000f) + KILO_METER
            else -> distance + METER
        }

    companion object {
        private const val KILO_METER = "km"
        private const val METER = "m"
    }
}