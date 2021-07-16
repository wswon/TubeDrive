package com.tube.driver.presentation

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.tube.driver.R
import com.tube.driver.domain.entity.LatLng
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapMarkerManager(
    private val lifecycleOwner: LifecycleOwner,
    private val mapView: MapView
) : LifecycleObserver {

    interface EventListener {
        fun onFirstCurrentLocation(latLng: LatLng)
    }

    private var eventListener: EventListener? = null

    fun setEventListener(eventListener: EventListener) {
        this.eventListener = eventListener
    }

    private val isAtLeast: Boolean
        get() = lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun created() {
        setupMapEventListener(mapView)
        mapView.currentLocationTrackingMode =
            MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun detachSelf() {
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        mapView.setShowCurrentLocationMarker(false)

        lifecycleOwner.lifecycle.removeObserver(this)
    }

    private fun setupMapEventListener(mapView: MapView) {
        val mapEventProvider = MapEventProvider().apply {
            setMapEventListener(object : MapEventProvider.MapEventListener {
                override fun onMapViewInitialized(mapView: MapView) {
                    setUseCustomMarker()
                }

                override fun onFirstCurrentLocation(latLng: LatLng) {
                    eventListener?.onFirstCurrentLocation(latLng)
                }
            })
        }
        mapView.setMapViewEventListener(mapEventProvider)
        mapView.setCurrentLocationEventListener(mapEventProvider)
    }

    private fun setUseCustomMarker() {
        mapView.setCustomCurrentLocationMarkerTrackingImage(
            R.drawable.ic_red_dot,
            MapPOIItem.ImageOffset(10, 10)
        )
        mapView.setCustomCurrentLocationMarkerImage(
            R.drawable.ic_red_dot,
            MapPOIItem.ImageOffset(15, 15)
        )
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

    fun getCenterPoint(): LatLng {
        val mapPointGeoCoord = mapView.mapCenterPoint.mapPointGeoCoord
        return LatLng(mapPointGeoCoord.latitude, mapPointGeoCoord.longitude)
    }
}