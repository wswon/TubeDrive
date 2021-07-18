package com.tube.driver.data.remote

import com.tube.driver.data.api.AddressApi
import com.tube.driver.data.response.PlaceResultResponse
import com.tube.driver.domain.model.GetPlaceListRequest
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
            latitude = getPlaceListRequest.mapPoints.center.latitude.toString(),
            longitude = getPlaceListRequest.mapPoints.center.longitude.toString(),
            rect = getPlaceListRequest.getRectRequest(),
            page = getPlaceListRequest.page
        )
    }
}