package com.tube.driver.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tube.driver.DLog
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

    private val _placeList = MutableLiveData<List<PlaceItem.Item>>()
    val placeList: LiveData<List<PlaceItem.Item>>
        get() = _placeList

    private var selectedCategoryType: CategoryType = CategoryType.HOSPITAL

    fun search(latLng: LatLng) {
        _placeList.value = emptyList()
        getAddressByCategory(
            selectedCategoryType.code,
            latLng
        )
            .map(placeMapper::transform)
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

    fun changeCategory(categoryType: CategoryType) {
        selectedCategoryType = categoryType
    }

}