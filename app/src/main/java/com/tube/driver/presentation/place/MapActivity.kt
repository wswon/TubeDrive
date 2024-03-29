package com.tube.driver.presentation.place

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tube.driver.R
import com.tube.driver.databinding.ActivityMapBinding
import com.tube.driver.domain.model.entity.LatLng
import com.tube.driver.presentation.place.adapter.PlaceAdapter
import com.tube.driver.presentation.place.adapter.PlaceItem
import com.tube.driver.presentation.place.map.MapMarkerManager
import com.tube.driver.util.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding
    private val viewModel by viewModels<MapViewModel>()

    private lateinit var mapMarkerManager: MapMarkerManager

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    private val placeAdapter: PlaceAdapter by lazy {
        PlaceAdapter(
            clickPlaceItem = { item ->
                mapMarkerManager.setFocusSelectedMarkerById(item.id.toInt())
                viewModel.setSelectedMarkerId(item.id.toInt())
            }
        )
    }

    private var latestBottomSheetState: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        ViewUtil.setStatusBarTransparent(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setMarkerManager()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupView() {
        with(binding) {
            placeListView.adapter = placeAdapter

            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet).apply {
                addBottomSheetCallback(createBottomSheetCallback())
                isDraggable = false
            }

            categoryLayout.setChangeCategoryListener { categoryType ->
                viewModel.changeCategory(categoryType)
                viewModel.searchPlaceByMapPoints(mapMarkerManager.getCurrentMapPoints())
                mapMarkerManager.clearAllMarker()
            }

            refreshButton.setOnClickListener {
                AnimationUtil.fadeOut(it)

                viewModel.searchPlaceByMapPoints(mapMarkerManager.getCurrentMapPoints())
                mapMarkerManager.clearAllMarker()
            }

            loadMoreButton.root.setOnClickListener {
                viewModel.loadMore()
            }

            currentLocationButton.setOnClickListener {
                PermissionManager.checkLocationPermissions(this@MapActivity)
                    .subscribe({
                        mapMarkerManager.enableTrackingMode()
                        val currentLatLng = viewModel.currentLatLng
                        if (currentLatLng != null) {
                            mapMarkerManager.moveLatLng(currentLatLng)
                        }
                    }, {
                        DLog.e("$it")
                    })
            }

            selectedPlaceView.root.setOnClickListener {
                val selectedPlaceId = viewModel.getSelectedPlaceId()
                mapMarkerManager.setFocusSelectedMarkerById(selectedPlaceId.toInt())
            }

            selectedPlaceView.callButton.setOnClickListener {
                val phoneNumber = viewModel.getSelectedPlacePhoneNumber()
                if (phoneNumber.isNotEmpty()) {
                    callPhoneNumber(phoneNumber)
                }
            }

            selectedPlaceView.webSiteButton.setOnClickListener {
                openWebView(viewModel.getSelectedPlaceUrl())
            }

            mapViewContainer.setOnTouchListener { _, event ->
                if (isExpandedBottomSheet()
                    && event.action == MotionEvent.ACTION_UP
                ) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
                isExpandedBottomSheet()
            }
        }
    }

    private fun setupViewModel() {
        with(viewModel) {
            placeList.observe(this@MapActivity, { placeList ->
                placeAdapter.submitList(placeList)

                placeList.forEach(mapMarkerManager::addMarker)

                bottomSheetBehavior.isDraggable = placeList.isNotEmpty()
            })

            emptyResultEvent.observe(this@MapActivity, EventObserver {
                Toast.makeText(this@MapActivity, R.string.empty_result_message, Toast.LENGTH_SHORT)
                    .show()
            })

            hasNextPage.observe(this@MapActivity, { hasNextPage ->
                binding.loadMoreButton.loadMoreText.alpha = if (hasNextPage) 1f else 0.3f
            })

            selectedPlaceItem.observe(this@MapActivity, { selectedPlaceItem ->
                setSelectedPlaceInfo(selectedPlaceItem)
            })

            isRefreshButtonVisible.observe(this@MapActivity, { isVisible ->
                if (isVisible) {
                    AnimationUtil.fadeIn(binding.refreshButton)
                } else {
                    AnimationUtil.fadeOut(binding.refreshButton)
                }
            })
        }
    }

    private fun setMarkerManager() {
        mapMarkerManager = MapMarkerManager(this, binding.mapViewContainer).apply {
            setEventListener(object : MapMarkerManager.EventListener {
                override fun onFirstCurrentLocation(latLng: LatLng) {
                    Handler().postDelayed({
                        viewModel.searchPlaceByMapPoints(mapMarkerManager.getCurrentMapPoints())
                    }, 50)
                }

                override fun onCurrentLatLngUpdate(currentLatLng: LatLng) {
                    viewModel.currentLatLng = currentLatLng
                }

                override fun onSelectedMarker(markerId: Int) {
                    mapMarkerManager.setFocusSelectedMarkerById(markerId)
                    viewModel.setSelectedMarkerId(markerId)
                }

                override fun onMapViewDragEnded() {
                    viewModel.showRefreshButton(mapMarkerManager.getCurrentMapPoints().center)
                }
            })
        }

        lifecycle.addObserver(mapMarkerManager)
    }

    private fun callPhoneNumber(phoneNumber: String) {
        PermissionManager.checkCallPhonePermissions(this@MapActivity)
            .subscribe({
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber")))
            }, {
                DLog.e("$it")
            })
    }

    private fun openWebView(url: String) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            CustomTabsIntent.Builder()
                .build()
                .launchUrl(this@MapActivity, Uri.parse(url))
        }
    }

    private fun setSelectedPlaceInfo(selectedPlaceItem: PlaceItem.Item) {
        binding.selectedPlaceView.run {
            name.text = selectedPlaceItem.name
            subCategory.text =
                if (selectedPlaceItem.subCategory.isNotEmpty()) selectedPlaceItem.subCategory else selectedPlaceItem.category
            address.text = selectedPlaceItem.addressName

            callButton.isVisible = selectedPlaceItem.phoneNumber.isNotEmpty()
            webSiteButton.isVisible = selectedPlaceItem.placeUrl.isNotEmpty()
        }
    }

    private fun isExpandedBottomSheet() =
        latestBottomSheetState == BottomSheetBehavior.STATE_EXPANDED

    private fun createBottomSheetCallback(): BottomSheetBehavior.BottomSheetCallback =
        object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                latestBottomSheetState = newState

                binding.selectedPlaceView.root.isVisible =
                    newState == BottomSheetBehavior.STATE_COLLAPSED
                if (isExpandedBottomSheet()) {
                    val selectedItem = viewModel.selectedPlaceItem.value
                    if (selectedItem != null) {
                        mapMarkerManager.setExpanded(selectedItem.latLng)
                    }
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mapMarkerManager.setCollapsed()
                }
            }

            override fun onSlide(
                bottomSheet: View,
                slideOffset: Float
            ) {
            }
        }
}


