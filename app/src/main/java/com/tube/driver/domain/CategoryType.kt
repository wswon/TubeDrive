package com.tube.driver.domain

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.tube.driver.R

enum class CategoryType(
    val code: String,
    @IdRes
    val id: Int,
    @StringRes
    val descriptionResId: Int,
    @DrawableRes
    val drawableResId: Int
) {
    HOSPITAL(
        "HP8",
        R.id.category_hospital,
        R.string.hospital,
        R.drawable.ic_hospital_32
    ),
    PHARMACY(
        "PM9",
        R.id.category_pharmacy,
        R.string.pharmacy,
        R.drawable.ic_pharmacy_32
    ),
    GAS_STATION(
        "OL7",
        R.id.category_gas_station,
        R.string.gas_station,
        R.drawable.ic_gas_station_32
    );

    companion object {
        fun findCategoryType(@IdRes id: Int): Result<CategoryType> {
            val type = values().firstOrNull { it.id == id}
            return if(type != null) {
                Result.success(type)
            } else {
                Result.failure(Throwable("Not Found CategoryType request id is $id"))
            }
        }
    }
}