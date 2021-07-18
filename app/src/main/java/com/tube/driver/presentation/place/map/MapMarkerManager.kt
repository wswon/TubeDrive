package com.tube.driver.presentation.place.map

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tube.driver.R
import com.tube.driver.domain.model.entity.LatLng
import com.tube.driver.domain.model.entity.MapPoints
import com.tube.driver.presentation.place.adapter.PlaceItem
import com.tube.driver.presentation.place.mapper.toLatLng
import com.tube.driver.util.DLog
import com.tube.driver.util.DistanceManager
import com.tube.driver.util.PermissionManager
import net.daum.mf.map.api.CameraUpdateFactory
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPOIItem.ImageOffset
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapMarkerManager(
    private val lifecycleOwner: AppCompatActivity,
    private val mapView: MapView
) : LifecycleObserver {

    interface EventListener {
        fun onFirstCurrentLocation(latLng: LatLng)
        fun onCurrentLatLngUpdate(currentLatLng: LatLng)
        fun onSelectedMarker(markerId: Int)
        fun onMapViewDragEnded()
    }

    private var eventListener: EventListener? = null
    private var isExpanded = false
    private var latestCollapsedLatLng: LatLng? = null
    private var latestCollapsedZoomLevel: Int? = null

    private val mapEventProvider by lazy {
        MapEventProvider(
            object : MapEventProvider.MapEventListener {
                override fun onMapViewInitialized(mapView: MapView) {
                    setUseCustomMarker()
                }

                override fun onFirstCurrentLocation(mapPoint: MapPoint) {
                    mapView.setMapCenterPoint(mapPoint, false)
                    eventListener?.onFirstCurrentLocation(mapPoint.mapPointGeoCoord.toLatLng())
                }

                override fun onCurrentLocationUpdate(currentLocationPoint: MapPoint) {
                    eventListener?.onCurrentLatLngUpdate(currentLocationPoint.mapPointGeoCoord.toLatLng())
                }

                override fun onMarkerSelected(selectedMapItem: MapPOIItem) {
                    mapView.setMapCenterPoint(selectedMapItem.mapPoint, true)
                    eventListener?.onSelectedMarker(selectedMapItem.tag)
                }

                override fun onMapViewDragEnded() {
                    eventListener?.onMapViewDragEnded()
                }
            }
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun created() {
        connectMapEventListener()

        PermissionManager.checkLocationPermissions(lifecycleOwner)
            .subscribe({
                enableTrackingMode()
            }, {
                DLog.e("$it")
            })

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        if (!isTrackingModeEnabled() && PermissionManager.isLocationPermissionGranted(lifecycleOwner)) {
            enableTrackingMode()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun detachSelf() {
        if (PermissionManager.isLocationPermissionGranted(lifecycleOwner)) {
            disableTrackingMode()
        }
        mapView.setShowCurrentLocationMarker(false)

        lifecycleOwner.lifecycle.removeObserver(this)
    }

    fun setEventListener(eventListener: EventListener) {
        this.eventListener = eventListener
    }

    private fun isTrackingModeEnabled() =
        mapView.currentLocationTrackingMode != MapView.CurrentLocationTrackingMode.TrackingModeOff

    fun enableTrackingMode() {
        mapView.currentLocationTrackingMode =
            MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving
    }

    private fun disableTrackingMode() {
        mapView.currentLocationTrackingMode =
            MapView.CurrentLocationTrackingMode.TrackingModeOff
    }

    private fun connectMapEventListener() {
        mapView.setMapViewEventListener(mapEventProvider)
        mapView.setCurrentLocationEventListener(mapEventProvider)
        mapView.setPOIItemEventListener(mapEventProvider)
    }

    private fun setUseCustomMarker() {
        mapView.setCustomCurrentLocationMarkerTrackingImage(
            R.drawable.ic_red_dot,
            ImageOffset(10, 10)
        )

        mapView.setCustomCurrentLocationMarkerDirectionImage(
            R.drawable.custom_map_present_direction,
            ImageOffset(15, 15)
        )
    }

    fun addMarker(markerItem: PlaceItem.Item) {
        if (containsMarker(markerItem)) return

        val marker = MapPOIItem().apply {
            itemName = markerItem.name
            tag = markerItem.id.toInt()
            mapPoint = MapPoint.mapPointWithGeoCoord(
                markerItem.latLng.latitude,
                markerItem.latLng.longitude
            )

            markerType = MapPOIItem.MarkerType.CustomImage
            selectedMarkerType =
                MapPOIItem.MarkerType.CustomImage
            customImageBitmap =
                BitmapFactory.decodeResource(lifecycleOwner.resources, R.drawable.ic_marker)
            customSelectedImageBitmap = BitmapFactory.decodeResource(
                lifecycleOwner.resources,
                R.drawable.ic_marker_selected
            )
            showAnimationType = MapPOIItem.ShowAnimationType.SpringFromGround
            isShowCalloutBalloonOnTouch = false
        }

        mapView.addPOIItem(marker)
        if (markerItem.isSelected) {
            mapView.selectPOIItem(marker, true)
        }
    }

    private fun containsMarker(markerItem: PlaceItem.Item): Boolean {
        return mapView.poiItems.any { it.tag == markerItem.id.toInt() }
    }

    fun getCurrentMapPoints(): MapPoints {
        return MapPoints(
            mapView.mapPointBounds.bottomLeft.mapPointGeoCoord.toLatLng(),
            mapView.mapPointBounds.topRight.mapPointGeoCoord.toLatLng(),
            mapView.mapPointBounds.center.mapPointGeoCoord.toLatLng()
        )
    }

    fun clearAllMarker() {
        mapView.removeAllPOIItems()
    }

    fun setFocusSelectedMarkerById(id: Int) {
        val marker = mapView.poiItems.find { it.tag == id }
        if (marker != null) {
            mapView.selectPOIItem(marker, true)
            if (isExpanded) {
                setExpandedCenterPoint(marker.mapPoint.mapPointGeoCoord.toLatLng())
            } else {
                setCenterPoint(marker.mapPoint.mapPointGeoCoord.toLatLng())
            }
        }
    }

    private fun setCenterPoint(latLng: LatLng) {
        mapView.setMapCenterPoint(
            MapPoint.mapPointWithGeoCoord(latLng.latitude, latLng.longitude),
            true
        )
    }

    fun setExpanded(latLng: LatLng) {
        isExpanded = true
        latestCollapsedLatLng = mapView.mapCenterPoint.mapPointGeoCoord.toLatLng()
        latestCollapsedZoomLevel = mapView.zoomLevel

        setExpandedCenterPoint(latLng)
    }

    fun setCollapsed() {
        isExpanded = false
        val latLng = latestCollapsedLatLng ?: return
        val zoomLevel = latestCollapsedZoomLevel ?: return
        mapView.setMapCenterPoint(
            MapPoint.mapPointWithGeoCoord(
                latLng.latitude,
                latLng.longitude
            ),
            true
        )
        mapView.setZoomLevel(zoomLevel, true)
    }

    private fun setExpandedCenterPoint(latLng: LatLng) {
        mapView.setMapCenterPoint(
            MapPoint.mapPointWithGeoCoord(
                latLng.latitude,
                latLng.longitude
            ),
            false
        )
        mapView.setZoomLevel(1, false)
        mapView.postDelayed({
            val midLatLng = DistanceManager.midPoint(
                latLng,
                LatLng(
                    getCurrentMapPoints().leftBottom.latitude,
                    latLng.longitude,
                )
            )
            mapView.setMapCenterPoint(
                MapPoint.mapPointWithGeoCoord(midLatLng.latitude, midLatLng.longitude),
                true
            )
        }, 50)
    }

    fun moveLatLng(latLng: LatLng) {
        mapView.moveCamera(
            CameraUpdateFactory.newMapPoint(
                MapPoint.mapPointWithGeoCoord(
                    latLng.latitude,
                    latLng.longitude
                )
            )
        )
    }
}