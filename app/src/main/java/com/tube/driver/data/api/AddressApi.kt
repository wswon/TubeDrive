package com.tube.driver.data.api

import androidx.annotation.IntRange
import com.tube.driver.data.response.PlaceResultResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface AddressApi {

    @GET("/v2/local/search/category")
    fun getAddressByCategory(
        @Query("category_group_code") categoryGroupCode: String,
        @Query("y") latitude: String,
        @Query("x") longitude: String,
        @IntRange(from = 0, to = 20_000) @Query("radius") radius: Int,
        @Query("sort") sort: String = "distance",
        @Header("Authorization") authorization: String = "KakaoAK 44334e64fbcc5c81e8eb7104a666e00b",
    ): Single<PlaceResultResponse>
}