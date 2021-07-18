package com.tube.driver.presentation.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tube.driver.domain.model.dto.GetPlaceListRequest
import com.tube.driver.domain.model.entity.LatLng
import com.tube.driver.domain.model.entity.MapPoints
import com.tube.driver.domain.usecase.GetPlaceListByCategory
import com.tube.driver.presentation.place.adapter.PlaceItem
import com.tube.driver.presentation.place.mapper.PlaceMapper
import com.tube.driver.util.DLog
import com.tube.driver.util.DistanceManager
import com.tube.driver.util.Event
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

    private val _emptyResultEvent = MutableLiveData<Event<Unit>>()
    val emptyResultEvent: LiveData<Event<Unit>>
        get() = _emptyResultEvent

    private var selectedCategoryType: CategoryType = CategoryType.HOSPITAL

    private var latestMapPoints: MapPoints? = null

    private var currentPage: Int = 1
    var currentLatLng: LatLng? = null

    fun searchPlaceByMapPoints(mapPoints: MapPoints) {
        latestMapPoints = mapPoints

        clearPlaceList()

        getSearchPlace(mapPoints, 1)
            .subscribe({ (placeList, hasNextPage) ->
                if (placeList.isNotEmpty()) {
                    setSelectedItem(placeList.first())
                    _placeList.value = placeList
                } else {
                    _emptyResultEvent.value = Event(Unit)
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
        selectedCategoryType = categoryType
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

    private fun setSelectedItem(placeItem: PlaceItem.Item) {
        placeItem.isSelected = true
        _selectedPlaceItem.value = placeItem
    }

    private fun clearPlaceList() {
        _placeList.value = emptyList()
        _selectedPlaceItem.value = PlaceItem.Item.EMPTY_ITEM
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun getSelectedPlaceId(): String {
        return selectedPlaceItem.value?.id.orEmpty()
    }

    fun getSelectedPlaceUrl(): String {
        return selectedPlaceItem.value?.placeUrl.orEmpty()
    }

    fun getSelectedPlacePhoneNumber(): String {
        return selectedPlaceItem.value?.phoneNumber.orEmpty()
    }

    fun setSelectedMarkerId(markerId: Int) {
        val copiedList = placeList.value.orEmpty().map { it.copy() }
        copiedList.forEach { it.isSelected = false }

        val placeItem = copiedList.find { it.id.toInt() == markerId }
        if (placeItem != null) {
            setSelectedItem(placeItem)
        }

        _placeList.value = copiedList
    }

    fun showRefreshButton(centerLatLng: LatLng) {
        latestMapPoints?.let {
            val distance = DistanceManager.getDistance(centerLatLng, it.center)
            DLog.d("$distance")
            if (distance > 300) {
                _isRefreshButtonVisible.value = true
            }
        }
    }
}