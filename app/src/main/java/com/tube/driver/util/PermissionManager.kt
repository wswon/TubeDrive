package com.tube.driver.util

import android.Manifest
import android.content.Context
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.tube.driver.R
import io.reactivex.rxjava3.core.Completable

object PermissionManager {
    fun checkCallPhonePermissions(context: Context): Completable {
        return Completable.create { completableEmitter ->
            TedPermission.with(context)
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        completableEmitter.onComplete()
                    }

                    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                        completableEmitter.onError(Throwable("${deniedPermissions.orEmpty()}"))
                    }
                })
                .setDeniedMessage(R.string.permission_denied_message_call)
                .setPermissions(Manifest.permission.CALL_PHONE)
                .check()
        }
    }

    fun checkLocationPermissions(context: Context): Completable {
        return Completable.create { completableEmitter ->
            TedPermission.with(context)
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        completableEmitter.onComplete()
                    }

                    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                        completableEmitter.onError(Throwable("${deniedPermissions.orEmpty()}"))
                    }
                })
                .setDeniedMessage(R.string.permission_denied_message_location)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check()
        }
    }

    fun isLocationPermissionGranted(context: Context) =
        TedPermission.isGranted(context, Manifest.permission.ACCESS_FINE_LOCATION)
}