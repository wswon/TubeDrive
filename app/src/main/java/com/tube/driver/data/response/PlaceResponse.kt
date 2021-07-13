package com.tube.driver.data.response


import com.google.gson.annotations.SerializedName

data class PlaceResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("place_name")
    val placeName: String?,
    @SerializedName("distance")
    val distance: String?,
    @SerializedName("place_url")
    val placeUrl: String?,
    @SerializedName("category_name")
    val categoryName: String?,
    @SerializedName("address_name")
    val addressName: String?,
    @SerializedName("road_address_name")
    val roadAddressName: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("category_group_code")
    val categoryGroupCode: String?,
    @SerializedName("category_group_name")
    val categoryGroupName: String?,
    @SerializedName("x")
    val x: String?,
    @SerializedName("y")
    val y: String?
)