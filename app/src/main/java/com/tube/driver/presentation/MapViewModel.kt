package com.tube.driver.presentation

import androidx.lifecycle.ViewModel
import com.tube.driver.DLog
import com.tube.driver.data.api.AddressApi
import com.tube.driver.domain.CategoryCode
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val addressApi: AddressApi
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun search(latitude: Double, longitude: Double) {
        addressApi.getAddressByCategory(
            CategoryCode.HOSPITAL.code,
            latitude.toString(), longitude.toString(),
            5000
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.placeList.forEach {
                    DLog.d("$it")
                }
            }, {
                DLog.e("$it")
            })
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}