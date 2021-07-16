package com.tube.driver.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tube.driver.DLog
import com.tube.driver.domain.GetPlaceListRequest
import com.tube.driver.domain.entity.LatLng
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

    private var selectedCategoryType: CategoryType = CategoryType.HOSPITAL

    private var latestLatLng: LatLng? = null
    private var currentPage: Int = 1
    private var hasNextPage = false

    fun search(latLng: LatLng) {
        latestLatLng = latLng

        clearPlaceList()

        getSearchPlace(latLng, 1)
            .subscribe({
                _placeList.value = it
            }, {
                DLog.e("$it")
            })
    }

    fun loadMore() {
        latestLatLng?.let { latLng ->
            getSearchPlace(latLng, currentPage++)
                .subscribe({
                    _placeList.value = placeList.value.orEmpty() + it
                }, {
                    DLog.e("$it")
                })
        }
    }

    fun changeCategory(categoryType: CategoryType) {
        selectedCategoryType = categoryType
    }

    private fun getSearchPlace(latLng: LatLng, page: Int) =
        getPlaceListByCategory(
            GetPlaceListRequest(
                selectedCategoryType.code,
                latLng,
                page
            )
        )
            .map(placeMapper::transform)
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