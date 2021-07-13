package com.tube.driver.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tube.driver.DLog
import com.tube.driver.databinding.ActivityMapBinding
import dagger.hilt.android.AndroidEntryPoint
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


@AndroidEntryPoint
class MapActivity : AppCompatActivity(),
    MapView.CurrentLocationEventListener {

    private lateinit var binding: ActivityMapBinding
    private val viewModel by viewModels<MapViewModel>()

    private val mapView: MapView
        get() = binding.mapViewContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapView.setCurrentLocationEventListener(this)
        mapView.currentLocationTrackingMode =
            MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        mapView.setShowCurrentLocationMarker(false)
    }


    private var flag = false

    override fun onCurrentLocationUpdate(
        mapView: MapView?,
        currentLocation: MapPoint?,
        accuracyInMeters: Float
    ) {
        currentLocation?.mapPointGeoCoord?.let { mapPointGeo ->
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
}

