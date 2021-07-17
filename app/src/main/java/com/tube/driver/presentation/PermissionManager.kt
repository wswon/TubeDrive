package com.tube.driver.presentation

import android.Manifest
import android.content.Context
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import io.reactivex.rxjava3.core.Completable

object PermissionManager {
    fun checkCallPhonePermissions(context: Context): Completable {
        return Completable.create { completableEmitter ->
            TedPermission.with(context)
                .setPermissionListener(object :PermissionListener {
                    override fun onPermissionGranted() {
                        completableEmitter.onComplete()
                    }

                    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                        completableEmitter.onError(Throwable("${deniedPermissions.orEmpty()}"))
                    }
                })
                .setDeniedMessage("사용 권한을 거부할 경우 전화걸기 기능을 사용할 수 없어요.\n[Setting](설정) > [Permission]에서 권한을 켜주세요.")
                .setPermissions(Manifest.permission.CALL_PHONE)
                .check()
        }
    }

    fun checkLocationPermissions(context: Context): Completable {
        return Completable.create { completableEmitter ->
            TedPermission.with(context)
                .setPermissionListener(object :PermissionListener {
                    override fun onPermissionGranted() {
                        completableEmitter.onComplete()
                    }

                    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                        completableEmitter.onError(Throwable("${deniedPermissions.orEmpty()}"))
                    }
                })
                .setDeniedMessage("위치권한 거부할 경우 현 위치 기반 검색 기능을 사용할 수 없어요.\n[Setting](설정) > [Permission]에서 권한을 켜주세요.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check()
        }
    }

    fun isLocationPermissionGranted(context: Context) =
        TedPermission.isGranted(context, Manifest.permission.ACCESS_FINE_LOCATION)
}