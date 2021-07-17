package com.tube.driver.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tube.driver.domain.GetPlaceListRequest
import com.tube.driver.domain.entity.LatLng
import com.tube.driver.domain.entity.MapPoints
import com.tube.driver.domain.usecase.GetPlaceListByCategory
import com.tube.driver.presentation.mapper.PlaceMapper
import com.tube.driver.util.DLog
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

    private val _selectedPlaceItem = MutableLiveData<PlaceItem.Item>()
    val selectedPlaceItem: LiveData<PlaceItem.Item>
        get() = _selectedPlaceItem

    private val _isRefreshButtonVisible = MutableLiveData(false)
    val isRefreshButtonVisible: LiveData<Boolean>
        get() = _isRefreshButtonVisible

    private var selectedCategoryType: CategoryType = CategoryType.HOSPITAL

    private var latestMapPoints: MapPoints? = null

    private var currentPage: Int = 1

    fun search(mapPoints: MapPoints) {
        latestMapPoints = mapPoints

        clearPlaceList()

        getSearchPlace(mapPoints, 1)
            .subscribe({ (placeList, hasNextPage) ->
                if (placeList.isNotEmpty()) {
                    placeList[0].isSelected = true
                    _selectedPlaceItem.value = placeList[0]
                    _placeList.value = placeList
                }

                _hasNextPage.value = hasNextPage
            }, {
                DLog.e("$it")
            })
    }

    fun loadMore() {
        val mapPoints = latestMapPoints
        val hasNextPage = hasNextPage.value
        if (mapPoints != null
            && hasNextPage == true
        ) {
            getSearchPlace(mapPoints, ++currentPage)
                .subscribe({ (placeList, hasNextPage) ->
                    _placeList.value = _placeList.value.orEmpty() + placeList
                    _hasNextPage.value = hasNextPage
                }, {
                    DLog.e("$it")
                })
        }
    }

    fun changeCategory(categoryType: CategoryType) {
        if (categoryType != selectedCategoryType) {
            _isRefreshButtonVisible.value = true
            _hasNextPage.value = false
            selectedCategoryType = categoryType
        }
    }

    private var currentLatLng: LatLng? = null

    fun setCurrentLatLng(currentLatLng: LatLng) {
        this.currentLatLng = currentLatLng
    }

    private fun getSearchPlace(mapPoints: MapPoints, page: Int) =
        getPlaceListByCategory(
            GetPlaceListRequest(
                selectedCategoryType.code,
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
        _selectedPlaceItem.value = PlaceItem.Item.EMPTY_ITEM
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun getSelectedPlaceUrl(): String {
        return selectedPlaceItem.value?.placeUrl.orEmpty()
    }

    fun getSelectedPlacePhoneNumber(): String {
        return selectedPlaceItem.value?.phoneNumber.orEmpty()
    }

    fun setSelectedMarkerId(markerId: Int) {
        _selectedPlaceItem.value = placeList.value?.first { it.id.toInt() == markerId }
    }

    fun showRefreshButton() {
        _isRefreshButtonVisible.value = true
    }
}