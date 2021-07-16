package com.tube.driver.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tube.driver.DLog
import com.tube.driver.R
import com.tube.driver.databinding.ActivityMapBinding
import dagger.hilt.android.AndroidEntryPoint
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPOIItem.ImageOffset
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


@AndroidEntryPoint
class MapActivity : AppCompatActivity(),
    MapView.MapViewEventListener,
    MapView.CurrentLocationEventListener {

    private lateinit var binding: ActivityMapBinding
    private val viewModel by viewModels<MapViewModel>()

    private val mapView: MapView
        get() = binding.mapViewContainer

    private var latestCurrentMapPoint: MapPoint.GeoCoordinate? = null

    private val placeAdapter: PlaceAdapter by lazy {
        PlaceAdapter(
            clickPlaceItem = {

            }
        )
    }

    private val placeLoadMoreAdapter: PlaceLoadMoreAdapter by lazy {
        PlaceLoadMoreAdapter(
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
                    MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving
            }

            placeListView.adapter = ConcatAdapter(placeAdapter, placeLoadMoreAdapter)
            placeLoadMoreAdapter.submitList(listOf(PlaceItem.LoadMoreFooter))

            refreshButton.setOnClickListener {
                val centerPoint = mapView.mapCenterPoint.mapPointGeoCoord
                viewModel.search(centerPoint.latitude, centerPoint.longitude)
            }

            BottomSheetBehavior.from(bottomSheet)
                .addBottomSheetCallback(createBottomSheetCallback(bottomSheetState))

            categoryLayout.setChangeCategoryListener { categoryType ->
                viewModel.changeCategory(categoryType)
            }


            mapView.setCurrentLocationRadius(0)
            mapView.setDefaultCurrentLocationMarker()

            placeTitle.setOnClickListener {
                setEnableCustomMarker(isUsingCustomLocationMarker)
                isUsingCustomLocationMarker = !isUsingCustomLocationMarker
            }
        }
    }

    private var isUsingCustomLocationMarker = false

    private fun setEnableCustomMarker(enableCustomMarker: Boolean) {
        if (enableCustomMarker) {
            mapView.setCurrentLocationRadius(100) // meter
            mapView.setCurrentLocationRadiusFillColor(Color.argb(77, 255, 255, 0))
            mapView.setCurrentLocationRadiusStrokeColor(Color.argb(77, 255, 165, 0))
            val trackingImageAnchorPointOffset = ImageOffset(10, 10) // 좌하단(0,0) 기준 앵커포인트 오프셋
            val offImageAnchorPointOffset = ImageOffset(15, 15)
            mapView.setCustomCurrentLocationMarkerTrackingImage(
                R.drawable.ic_red_dot,
                trackingImageAnchorPointOffset
            )
            mapView.setCustomCurrentLocationMarkerImage(
                R.drawable.ic_red_dot,
                offImageAnchorPointOffset
            )
        } else {
            mapView.setCurrentLocationRadius(0)
            mapView.setDefaultCurrentLocationMarker()
        }
    }

    private fun setupViewModel() {
        with(viewModel) {
            placeList.observe(this@MapActivity, { placeList ->
                placeAdapter.submitList(placeList)
                placeList.forEach { item: PlaceItem ->
                    if (item is PlaceItem.Item) {
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


    private fun addMarker(markerItem: PlaceItem.Item) {
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

    override fun onMapViewInitialized(mapView: MapView?) {

    }

    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {

    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {

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
                mapView?.setMapCenterPoint(currentLocation, false)

                viewModel.search(mapPointGeo.latitude, mapPointGeo.longitude)
            }
            setEnableCustomMarker(true)
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

