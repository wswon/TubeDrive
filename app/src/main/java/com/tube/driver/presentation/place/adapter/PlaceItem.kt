package com.tube.driver.presentation.place.adapter

import com.tube.driver.domain.model.entity.LatLng

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
    ) : PlaceItem {
        companion object {
            val EMPTY_ITEM =
                Item("", "", "", "", "", "", "", LatLng(-1.0, -1.0))
        }
    }
}