package com.tube.driver.data.remote

import com.tube.driver.data.api.AddressApi
import com.tube.driver.data.response.PlaceResponse
import com.tube.driver.domain.CategoryCode
import com.tube.driver.domain.entity.LatLng
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class PlaceRemoteDataSourceImpl @Inject constructor(
    private val addressApi: AddressApi
) : PlaceRemoteDataSource {

    override fun getAddressByCategory(
        categoryCode: CategoryCode,
        latLng: LatLng
    ): Single<List<PlaceResponse>> {
        return addressApi.getAddressByCategory(
            CategoryCode.HOSPITAL.code,
            latLng.latitude.toString(),
            latLng.longitude.toString(),
            radius = 5000
        )
            .map {
                it.placeList
            }
    }
}