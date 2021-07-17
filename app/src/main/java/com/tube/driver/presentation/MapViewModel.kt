package com.tube.driver.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tube.driver.DLog
import com.tube.driver.domain.GetPlaceListRequest
import com.tube.driver.domain.entity.LatLng
import com.tube.driver.domain.entity.MapPoints
import com.tube.driver.domain.usecase.GetPlaceListByCategory
import com.tube.driver.presentation.mapper.PlaceMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getPlaceListByCategory: GetPlaceListByCategory,
    private val placeMapper: PlaceMapper
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _placeList = MutableLiveData<List<PlaceItem.Item>>()
    val placeList: LiveData<List<PlaceItem.Item>>
        get() = _placeList

    private val _hasNextPage = MutableLiveData(false)
    val hasNextPage: LiveData<Boolean>
        get() = _hasNextPage

    private var selectedCategoryType: CategoryType = CategoryType.HOSPITAL
    private var latestMapPoints: MapPoints? = null


    private var currentPage: Int = 1

    fun search(mapPoints: MapPoints) {
        latestMapPoints = mapPoints

        clearPlaceList()

        val currentLatLng = currentLatLng

        if (currentLatLng != null) {
            getSearchPlace(currentLatLng, mapPoints, 1)
                .subscribe({ (placeList, hasNextPage) ->
                    _placeList.value = placeList
                    _hasNextPage.value = hasNextPage
                }, {
                    DLog.e("$it")
                })
        }
    }

    fun loadMore() {
        val currentLatLng = currentLatLng
        val mapPoints = latestMapPoints
        val hasNextPage = hasNextPage.value
        if (currentLatLng != null
            && mapPoints != null
            && hasNextPage == true
        ) {
            getSearchPlace(currentLatLng, mapPoints, ++currentPage)
                .subscribe({ (placeList, hasNextPage) ->
                    _placeList.value = _placeList.value.orEmpty() + placeList
                    _hasNextPage.value = hasNextPage
                }, {
                    DLog.e("$it")
                })
        }
    }

    fun changeCategory(categoryType: CategoryType) {
        selectedCategoryType = categoryType
    }

    private var currentLatLng: LatLng? = null
    fun setCurrentLatLng(currentLatLng: LatLng) {
        this.currentLatLng = currentLatLng
    }

    private fun getSearchPlace(currentLatLng: LatLng, mapPoints: MapPoints, page: Int) =
        getPlaceListByCategory(
            GetPlaceListRequest(
                selectedCategoryType.code,
                currentLatLng,
                mapPoints,
                page
            )
        )
            .map {
                placeMapper.transform(it.placeList) to it.hasNextPage
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private fun clearPlaceList() {
        _placeList.value = emptyList()
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}