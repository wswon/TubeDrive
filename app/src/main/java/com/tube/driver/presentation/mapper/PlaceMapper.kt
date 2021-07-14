package com.tube.driver.presentation.mapper

import com.tube.driver.domain.entity.Place
import com.tube.driver.presentation.PlaceItem

class PlaceMapper {

    fun transform(place: Place): PlaceItem.Item {
        return place.run {
            PlaceItem.Item(
                id = id,
                name = placeName,
                distance = distance,
                phoneNumber = phone,
                addressName = if (roadAddressName.isNotEmpty()) roadAddressName else addressName,
                category = category.middleCategory,
                subCategory = category.subCategory
            )
        }
    }

    fun transform(placeList: List<Place>): List<PlaceItem.Item> {
        return placeList.map(this::transform)
    }
}