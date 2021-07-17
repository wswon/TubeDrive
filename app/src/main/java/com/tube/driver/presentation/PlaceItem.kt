package com.tube.driver.presentation

import com.tube.driver.domain.entity.LatLng

sealed interface PlaceItem {

    data class Item(
        val id: String,
        val name: String,
        val placeUrl: String,
        val phoneNumber: String,
        val addressName: String,
        val category: String,
        val subCategory: String,
        val latLng: LatLng,
        var isSelected: Boolean = false
    ) : PlaceItem
}