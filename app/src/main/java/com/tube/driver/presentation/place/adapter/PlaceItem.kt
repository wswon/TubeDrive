package com.tube.driver.presentation.place.adapter

import com.tube.driver.domain.model.entity.LatLng

sealed interface PlaceItem {

    class Item(
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

        fun copy(): Item {
            return Item(id, name, placeUrl, phoneNumber, addressName, category, subCategory, latLng, isSelected)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Item

            if (id != other.id) return false
            if (name != other.name) return false
            if (placeUrl != other.placeUrl) return false
            if (phoneNumber != other.phoneNumber) return false
            if (addressName != other.addressName) return false
            if (category != other.category) return false
            if (subCategory != other.subCategory) return false
            if (latLng != other.latLng) return false
            if (isSelected != other.isSelected) return false

            return true
        }

        override fun hashCode(): Int {
            return super.hashCode()
        }

        companion object {
            val EMPTY_ITEM =
                Item("", "", "", "", "", "", "", LatLng(-1.0, -1.0))
        }
    }
}