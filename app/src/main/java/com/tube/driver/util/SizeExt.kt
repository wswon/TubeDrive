package com.tube.driver

import android.content.res.Resources

val Int.dp: Float
    get() = this * (densityDpi / 160f)

val Float.dp: Float
    get() = this * (densityDpi / 160f)

private val densityDpi: Int
    get() = Resources.getSystem().displayMetrics.densityDpi