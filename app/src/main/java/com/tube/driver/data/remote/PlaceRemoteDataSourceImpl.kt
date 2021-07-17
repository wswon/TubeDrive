package com.tube.driver.data.remote

import com.tube.driver.data.api.AddressApi
import com.tube.driver.data.response.PlaceResultResponse
import com.tube.driver.domain.GetPlaceListRequest
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class PlaceRemoteDataSourceImpl @Inject constructor(
    private val addressApi: AddressApi
) : PlaceRemoteDataSource {

    override fun getPlaceListByCategory(
        getPlaceListRequest: GetPlaceListRequest
    ): Single<PlaceResultResponse> {
        return addressApi.getAddressByCategory(
            getPlaceListRequest.categoryCode,
            latitude = getPlaceListRequest.currentLatLng.latitude.toString(),
            longitude = getPlaceListRequest.currentLatLng.longitude.toString(),
            rect = getPlaceListRequest.getRectRequest(),
            page = getPlaceListRequest.page
        )
    }
}