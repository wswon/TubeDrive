package com.tube.driver.data.response


import com.google.gson.annotations.SerializedName

data class PlaceResultResponse(
    @SerializedName("meta")
    val meta: Meta,
    @SerializedName("documents")
    val placeList: List<PlaceResponse>
)