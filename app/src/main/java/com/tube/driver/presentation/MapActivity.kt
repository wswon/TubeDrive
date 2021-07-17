package com.tube.driver.presentation

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tube.driver.databinding.ActivityMapBinding
import com.tube.driver.domain.entity.LatLng
import dagger.hilt.android.AndroidEntryPoint
import net.daum.mf.map.api.MapView
import java.util.*

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapBinding
    private val viewModel by viewModels<MapViewModel>()

    private val mapView: MapView
        get() = binding.mapViewContainer

    private lateinit var mapMarkerManager: MapMarkerManager

    private val placeAdapter: PlaceAdapter by lazy {
        PlaceAdapter(
            clickPlaceItem = {

            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setMarkerManager()
    }

    private fun setMarkerManager() {
        mapMarkerManager = MapMarkerManager(this, mapView).apply {
            setEventListener(object : MapMarkerManager.EventListener {
                override fun onFirstCurrentLocation(latLng: LatLng) {
                    mapView.postDelayed({
                        viewModel.search(mapMarkerManager.getCurrentMapPoints())
                    }, 50)
                }

                override fun onCurrentLatLngUpdate(currentLatLng: LatLng) {
                    viewModel.setCurrentLatLng(currentLatLng)
                }
            })
        }

        lifecycle.addObserver(mapMarkerManager)
    }

    private fun setupView() {
        with(binding) {
            placeListView.adapter = placeAdapter

            refreshButton.setOnClickListener {
                val mapPoints = mapMarkerManager.getCurrentMapPoints()
                viewModel.search(mapPoints)
                mapMarkerManager.clearAllMarker()
            }

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

                placeList.forEach(mapMarkerManager::addMarker)
            })

            hasNextPage.observe(this@MapActivity, { hasNextPage ->

            })
        }
    }

    private fun createBottomSheetCallback(text: TextView): BottomSheetBehavior.BottomSheetCallback =
        object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                binding.selectedPlaceView.root.isVisible = newState == BottomSheetBehavior.STATE_COLLAPSED

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


