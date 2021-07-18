package com.tube.driver.presentation.place.map

import com.tube.driver.util.DLog
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapEventProvider(
    private val mapEventListener: MapEventListener
) :
    MapView.MapViewEventListener,
    MapView.CurrentLocationEventListener,
    MapView.POIItemEventListener {

    interface MapEventListener {
        fun onMapViewInitialized(mapView: MapView)
        fun onFirstCurrentLocation(mapPoint: MapPoint)
        fun onMarkerSelected(selectedMapItem: MapPOIItem)
        fun onCurrentLocationUpdate(currentLocationPoint: MapPoint)
        fun onMapViewDragEnded()
    }

    private var firstLocation = false

    override fun onMapViewInitialized(mapView: MapView?) {
        if (mapView != null) {
            mapEventListener.onMapViewInitialized(mapView)
        }
    }

    override fun onMapViewCenterPointMoved(mapView: MapView?, mapPoint: MapPoint?) {}

    override fun onMapViewZoomLevelChanged(mapView: MapView?, p1: Int) {}

    override fun onMapViewSingleTapped(mapView: MapView?, mapPoint: MapPoint?) {}

    override fun onMapViewDoubleTapped(mapView: MapView?, mapPoint: MapPoint?) {}

    override fun onMapViewLongPressed(mapView: MapView?, mapPoint: MapPoint?) {}

    override fun onMapViewDragStarted(mapView: MapView?, mapPoint: MapPoint?) {}

    override fun onMapViewDragEnded(mapView: MapView?, mapPoint: MapPoint?) {
        mapEventListener.onMapViewDragEnded()
    }

    override fun onMapViewMoveFinished(mapView: MapView?, mapPoint: MapPoint?) {}

    override fun onCurrentLocationUpdate(
        mapView: MapView?,
        currentLocation: MapPoint?,
        accuracyInMeters: Float
    ) {
        currentLocation?.mapPointGeoCoord?.let { mapPointGeo ->
            if (!firstLocation) {
                firstLocation = true
                mapEventListener.onFirstCurrentLocation(currentLocation)
            }

            mapEventListener.onCurrentLocationUpdate(currentLocation)

            logCurrentLocation(mapPointGeo, accuracyInMeters)
        }
    }

    override fun onCurrentLocationDeviceHeadingUpdate(mapView: MapView?, p1: Float) {}

    override fun onCurrentLocationUpdateFailed(mapView: MapView?) {}

    override fun onCurrentLocationUpdateCancelled(mapView: MapView?) {}

    override fun onPOIItemSelected(mapView: MapView?, mapItem: MapPOIItem?) {
        if (mapItem != null) {
            mapEventListener.onMarkerSelected(mapItem)
        }
    }

    override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView?, mapItem: MapPOIItem?) {}

    override fun onCalloutBalloonOfPOIItemTouched(
        mapView: MapView?,
        mapItem: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {
    }

    override fun onDraggablePOIItemMoved(
        mapView: MapView?,
        mapItem: MapPOIItem?,
        mapPoint: MapPoint?
    ) {
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
