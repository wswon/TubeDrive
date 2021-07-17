package com.tube.driver.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tube.driver.databinding.ActivityMapBinding
import com.tube.driver.domain.entity.LatLng
import com.tube.driver.util.AnimationUtil
import com.tube.driver.util.DLog
import com.tube.driver.util.PermissionManager
import com.tube.driver.util.ViewUtil
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

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    private val placeAdapter: PlaceAdapter by lazy {
        PlaceAdapter(
            clickPlaceItem = {

            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ViewUtil.setStatusBarTransparent(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setMarkerManager()
    }

    private fun setupView() {
        with(binding) {
            placeListView.adapter = placeAdapter

            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet).apply {
                addBottomSheetCallback(createBottomSheetCallback())
                isDraggable = true
            }

            categoryLayout.setChangeCategoryListener { categoryType ->
                viewModel.changeCategory(categoryType)
            }

            refreshButton.setOnClickListener {
                AnimationUtil.fadeOut(it)

                val mapPoints = mapMarkerManager.getCurrentMapPoints()
                viewModel.search(mapPoints)
                mapMarkerManager.clearAllMarker()
            }

            loadMoreButton.root.setOnClickListener {
                viewModel.loadMore()
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
        }
    }

    private fun setupViewModel() {
        with(viewModel) {
            placeList.observe(this@MapActivity, { placeList ->
                placeAdapter.submitList(placeList)

                placeList.forEach(mapMarkerManager::addMarker)

                bottomSheetBehavior.isDraggable = placeList.isNotEmpty()
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

                override fun onSelectedMarker(markerId: Int) {
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
            root.isVisible = true
            name.text = selectedPlaceItem.name
            subCategory.text =
                if (selectedPlaceItem.subCategory.isNotEmpty()) selectedPlaceItem.subCategory else selectedPlaceItem.category
            address.text = selectedPlaceItem.addressName

            callButton.isVisible = selectedPlaceItem.phoneNumber.isNotEmpty()
            webSiteButton.isVisible = selectedPlaceItem.placeUrl.isNotEmpty()
        }
    }

    private fun createBottomSheetCallback(): BottomSheetBehavior.BottomSheetCallback =
        object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.selectedPlaceView.root.isVisible =
                    newState == BottomSheetBehavior.STATE_COLLAPSED
            }

            override fun onSlide(
                bottomSheet: View,
                slideOffset: Float
            ) {
            }
        }
}


