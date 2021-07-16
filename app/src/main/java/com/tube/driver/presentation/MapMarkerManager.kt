package com.tube.driver.presentation

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tube.driver.R
import com.tube.driver.domain.entity.LatLng
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
    }

    private var eventListener: EventListener? = null

    fun setEventListener(eventListener: EventListener) {
        this.eventListener = eventListener
    }

    private val isAtLeast: Boolean
        get() = lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun created() {
        connectMapEventListener()
        mapView.currentLocationTrackingMode =
            MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun detachSelf() {
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        mapView.setShowCurrentLocationMarker(false)

        lifecycleOwner.lifecycle.removeObserver(this)
    }

    private val mapEventProvider by lazy {
        MapEventProvider(
            object : MapEventProvider.MapEventListener {
                override fun onMapViewInitialized(mapView: MapView) {
                    setUseCustomMarker()
                }

                override fun onFirstCurrentLocation(mapPoint: MapPoint) {
                    mapView.setMapCenterPoint(mapPoint, false)
                    val latLng =
                        LatLng(
                            mapPoint.mapPointGeoCoord.latitude,
                            mapPoint.mapPointGeoCoord.longitude
                        )
                    eventListener?.onFirstCurrentLocation(latLng)
                }

                override fun onMarkerSelected(selectedPoint: MapPoint) {
                    mapView.setMapCenterPoint(selectedPoint, true)
                }
            }
        )
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
        }

        mapView.addPOIItem(marker)
    }

    fun getCenterPoint(): LatLng {
        val mapPointGeoCoord = mapView.mapCenterPoint.mapPointGeoCoord
        return LatLng(mapPointGeoCoord.latitude, mapPointGeoCoord.longitude)
    }

    fun clearAllMarker() {
        mapView.removeAllPOIItems()
    }
}