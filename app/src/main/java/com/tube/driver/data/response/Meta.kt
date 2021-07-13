package com.tube.driver.data.response


import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("same_name")
    val sameName: Any?,
    @SerializedName("pageable_count")
    val pageableCount: Int = -1,
    @SerializedName("total_count")
    val totalCount: Int = -1,
    @SerializedName("is_end")
    val isEnd: Boolean = false
)