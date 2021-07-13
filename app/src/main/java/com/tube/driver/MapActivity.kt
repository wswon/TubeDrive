package com.tube.driver

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tube.driver.databinding.ActivityMapBinding
import net.daum.mf.map.api.MapView

class MapActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapViewContainer.addView(MapView(this))
    }
}

