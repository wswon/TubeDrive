package com.tube.driver.data.mapper

import com.tube.driver.data.response.PlaceResponse
import com.tube.driver.data.response.PlaceResultResponse
import com.tube.driver.domain.model.GetPlaceListResult
import com.tube.driver.domain.model.entity.Category
import com.tube.driver.domain.model.entity.LatLng
import com.tube.driver.domain.model.entity.Place

class PlaceResponseMapper {
    fun transform(placeResponse: PlaceResponse): Place {

        return placeResponse.run {

            val categories = categoryName.orEmpty()
                .replace(" ", "")
                .split(">")

            val category = Category(
                categories.getOrNull(0).orEmpty(),
                categories.getOrNull(1).orEmpty(),
                categories.getOrNull(2).orEmpty()
            )

            Place(
                id = id,
                placeName = placeName.orEmpty(),
                distance = distance.orEmpty(),
                placeUrl = placeUrl.orEmpty(),
                category = category,
                addressName = addressName.orEmpty(),
                roadAddressName = roadAddressName.orEmpty(),
                phone = phone.orEmpty(),
                categoryGroupCode = categoryGroupCode.orEmpty(),
                categoryGroupName = categoryGroupName.orEmpty(),
                latLng = LatLng(y?.toDoubleOrNull() ?: -1.0, x?.toDoubleOrNull() ?: -1.0)
            )
        }
    }

    fun transform(placeResponseList: List<PlaceResponse>): List<Place> {
        return placeResponseList.map(this::transform)
    }

    fun transform(placeResultResponse: PlaceResultResponse): GetPlaceListResult {
        return GetPlaceListResult(placeResultResponse.placeList.map(::transform), !placeResultResponse.meta.isEnd)
    }
}