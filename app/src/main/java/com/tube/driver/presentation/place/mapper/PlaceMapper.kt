package com.tube.driver.presentation.place.mapper

import com.tube.driver.domain.model.entity.Place
import com.tube.driver.presentation.place.adapter.PlaceItem

class PlaceMapper {

    fun transform(place: Place): PlaceItem.Item {
        return place.run {
            PlaceItem.Item(
                id = id,
                name = placeName,
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
}