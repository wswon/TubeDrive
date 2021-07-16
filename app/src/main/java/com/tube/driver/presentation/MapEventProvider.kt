package com.tube.driver.presentation

import com.tube.driver.DLog
import com.tube.driver.domain.entity.LatLng
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapEventProvider :
    MapView.MapViewEventListener,
    MapView.CurrentLocationEventListener {

    interface MapEventListener {
        fun onMapViewInitialized(mapView: MapView)
        fun onFirstCurrentLocation(latLng: LatLng)
    }

    private var firstLocation = false

    private var mapEventListener: MapEventListener? = null

    fun setMapEventListener(mapEventListener: MapEventListener) {
        this.mapEventListener = mapEventListener
    }

    override fun onMapViewInitialized(mapView: MapView?) {
        if (mapView != null) {
            mapEventListener?.onMapViewInitialized(mapView)
        }
    }

    override fun onMapViewCenterPointMoved(mapView: MapView?, mapPoint: MapPoint?) {

    }

    override fun onMapViewZoomLevelChanged(mapView: MapView?, p1: Int) {

    }

    override fun onMapViewSingleTapped(mapView: MapView?, mapPoint: MapPoint?) {

    }

    override fun onMapViewDoubleTapped(mapView: MapView?, mapPoint: MapPoint?) {

    }

    override fun onMapViewLongPressed(mapView: MapView?, mapPoint: MapPoint?) {

    }

    override fun onMapViewDragStarted(mapView: MapView?, mapPoint: MapPoint?) {

    }

    override fun onMapViewDragEnded(mapView: MapView?, mapPoint: MapPoint?) {

    }

    override fun onMapViewMoveFinished(mapView: MapView?, mapPoint: MapPoint?) {

    }

    override fun onCurrentLocationUpdate(
        mapView: MapView?,
        currentLocation: MapPoint?,
        accuracyInMeters: Float
    ) {
        currentLocation?.mapPointGeoCoord?.let { mapPointGeo ->
            if (!firstLocation) {
                firstLocation = true
                mapView?.setMapCenterPoint(currentLocation, false)

                val latLng = LatLng(mapPointGeo.latitude, mapPointGeo.longitude)
                mapEventListener?.onFirstCurrentLocation(latLng)
            }

            logCurrentLocation(mapPointGeo, accuracyInMeters)
        }
    }

    override fun onCurrentLocationDeviceHeadingUpdate(mapView: MapView?, p1: Float) {

    }

    override fun onCurrentLocationUpdateFailed(mapView: MapView?) {

    }

    override fun onCurrentLocationUpdateCancelled(mapView: MapView?) {

    }

    private fun logCurrentLocation(
        mapPointGeo: MapPoint.GeoCoordinate,
        accuracyInMeters: Float
    ) {
        DLog.d(
            String.format(
                "MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)",
                mapPointGeo.latitude,
                mapPointGeo.longitude,
                accuracyInMeters
            )
        )
    }
}
