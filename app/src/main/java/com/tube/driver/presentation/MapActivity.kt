package com.tube.driver.presentation

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tube.driver.DLog
import com.tube.driver.databinding.ActivityMapBinding
import dagger.hilt.android.AndroidEntryPoint
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


@AndroidEntryPoint
class MapActivity : AppCompatActivity(),
    MapView.CurrentLocationEventListener {

    private lateinit var binding: ActivityMapBinding
    private val viewModel by viewModels<MapViewModel>()

    private val mapView: MapView
        get() = binding.mapViewContainer

    private var latestCurrentMapPoint: MapPoint.GeoCoordinate? = null

    private val placeAdapter: PlaceAdapter by lazy {
        PlaceAdapter(
            clickPlaceItem = {

            },
            clickLoadMore = {

            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
    }

    private fun setupView() {
        with(binding) {
            mapView.run {
                setCurrentLocationEventListener(this@MapActivity)
                currentLocationTrackingMode =
                    MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
            }

            refreshButton.setOnClickListener {
                latestCurrentMapPoint?.let { mapPoint ->
                    viewModel.search(mapPoint.latitude, mapPoint.longitude)
                }
            }

            placeListView.adapter = placeAdapter

            BottomSheetBehavior.from(bottomSheet)
                .addBottomSheetCallback(createBottomSheetCallback(bottomSheetState))

            categoryLayout.setChangeCategoryListener { categoryType ->
                viewModel.changeCategory(categoryType)
            }
        }
    }

    private fun setupViewModel() {
        with(viewModel) {
            placeList.observe(this@MapActivity, { placeList ->
                placeAdapter.submitList(placeList)
                placeList.forEach { item: PlaceItem ->
                    if (item is PlaceItem.Item){
                        addMarker(item)
                    }
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        mapView.setShowCurrentLocationMarker(false)
    }


    fun addMarker(markerItem: PlaceItem.Item) {
        val marker = MapPOIItem().apply {
            itemName = markerItem.name
            tag = 0
            mapPoint = MapPoint.mapPointWithGeoCoord(
                markerItem.latLng.latitude,
                markerItem.latLng.longitude
            )
            markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.

            selectedMarkerType =
                MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양
        }
        mapView.addPOIItem(marker)
    }


    private var flag = false

    override fun onCurrentLocationUpdate(
        mapView: MapView?,
        currentLocation: MapPoint?,
        accuracyInMeters: Float
    ) {
        currentLocation?.mapPointGeoCoord?.let { mapPointGeo ->
            latestCurrentMapPoint = mapPointGeo

            DLog.d(
                String.format(
                    "MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)",
                    mapPointGeo.latitude,
                    mapPointGeo.longitude,
                    accuracyInMeters
                )
            )

            if (!flag) {
                flag = true
                viewModel.search(mapPointGeo.latitude, mapPointGeo.longitude)
            }
        }
    }

    override fun onCurrentLocationDeviceHeadingUpdate(p0: MapView?, p1: Float) {

    }

    override fun onCurrentLocationUpdateFailed(p0: MapView?) {

    }

    override fun onCurrentLocationUpdateCancelled(p0: MapView?) {

    }


    private fun createBottomSheetCallback(text: TextView): BottomSheetBehavior.BottomSheetCallback =
        object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                text.text = when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> "STATE DRAGGING"
                    BottomSheetBehavior.STATE_EXPANDED -> "STATE EXPANDED"
                    BottomSheetBehavior.STATE_COLLAPSED -> "STATE COLLAPSED"
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        String.format(
                            "STATE_HALF_EXPANDED\\nhalfExpandedRatio = %.2f",
                            BottomSheetBehavior.from(bottomSheet).halfExpandedRatio
                        )
                    }
                    else -> {
                        text.text.toString()
                    }
                }
            }

            override fun onSlide(
                bottomSheet: View,
                slideOffset: Float
            ) {
            }
        }
}

