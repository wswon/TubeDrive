package com.tube.driver.presentation

import com.tube.driver.domain.entity.LatLng

sealed interface PlaceItem {

    data class Item(
        val id: String,
        val name: String,
        val distance: String,
        val phoneNumber: String,
        val addressName: String,
        val category: String,
        val subCategory: String,
        val latLng: LatLng
    ) : PlaceItem
}