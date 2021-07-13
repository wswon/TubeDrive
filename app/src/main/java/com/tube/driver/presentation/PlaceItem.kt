package com.tube.driver.presentation

sealed interface PlaceItem {
    val id: String

    data class Item(
        override val id: String,
        val name: String,
        val distance: String,
        val phoneNumber: String,
        val addressName: String,
        val category: String,
        val subCategory: String
    ) : PlaceItem

    object LoadMoreFooter : PlaceItem {
        override val id: String
            get() = "LoadMoreFooter"
    }
}
