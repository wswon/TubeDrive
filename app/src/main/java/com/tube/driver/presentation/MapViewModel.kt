package com.tube.driver.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tube.driver.DLog
import com.tube.driver.domain.CategoryCode
import com.tube.driver.domain.entity.LatLng
import com.tube.driver.domain.usecase.GetAddressByCategory
import com.tube.driver.presentation.mapper.PlaceMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getAddressByCategory: GetAddressByCategory,
    private val placeMapper: PlaceMapper
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _placeList = MutableLiveData<List<PlaceItem>>()
    val placeList: LiveData<List<PlaceItem>>
        get() = _placeList

    private var selectedCategoryCode: CategoryCode = CategoryCode.HOSPITAL

    fun search(latitude: Double, longitude: Double) {
        getAddressByCategory(
            selectedCategoryCode,
            LatLng(latitude, longitude)
        )
            .map(placeMapper::transform)
            .map {
                it + PlaceItem.LoadMoreFooter
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _placeList.value = it
            }, {
                DLog.e("$it")
            })
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}