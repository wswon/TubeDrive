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
            getPlaceListRequest.latLng.latitude.toString(),
            getPlaceListRequest.latLng.longitude.toString(),
            radius = 5000,
            page = getPlaceListRequest.page
        )
    }
}