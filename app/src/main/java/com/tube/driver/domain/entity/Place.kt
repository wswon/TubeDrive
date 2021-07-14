package com.tube.driver.domain.entity

class Place(
    val id: String,
    val placeName: String,
    val distance: String,
    val placeUrl: String,
    val category: Category,
    val addressName: String,
    val roadAddressName: String,
    val phone: String,
    val categoryGroupCode: String,
    val categoryGroupName: String,
    val latitude: Double,
    val longitude: Double
)