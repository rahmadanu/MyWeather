package com.powerhouseai.myweather.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.powerhouseai.myweather.R
import com.powerhouseai.myweather.data.model.ui.WeatherUiModel
import com.powerhouseai.myweather.databinding.ActivityMainBinding
import com.powerhouseai.myweather.util.work.SyncInitializer
import com.powerhouseai.myweather.util.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUi()
        setupCurrentLocation()
        setupOnClick()
        observeWeather()
    }

    private fun setupUi() {
        SyncInitializer.isWorkRunning(this@MainActivity).observe(this) {
            if (it) {
                binding.pbLoading.visibility = View.VISIBLE
            } else {
                binding.pbLoading.visibility = View.GONE
            }
        }
    }

    private fun setupCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getWeatherByCurrentLocation()
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                getWeatherByCurrentLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                getWeatherByCurrentLocation()
            } else -> {
                // No location access granted.
                showGoToSettingsDialog()
            }
        }
    }

    private fun getWeatherByCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location: Location? ->
                    loadData(location?.latitude ?: 0.0, location?.longitude ?: 0.0)

                    if (location == null) {
                        showTurnOnGpsDialog()
                    }
                }
        } else {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun loadData(latitude: Double, longitude: Double) {
        viewModel.getCurrentLocationWeather(latitude, longitude)
    }

    private fun showTurnOnGpsDialog() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.txt_turn_on_gps))
            .setCancelable(false)
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showGoToSettingsDialog() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.txt_need_permission_description))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.txt_go_to_settings)) { dialog, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", this.packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .show()
    }

    private fun setupOnClick() {
        binding.btnRefresh.setOnClickListener {
            SyncInitializer.enqueueWork(this)

            lifecycleScope.launch {
                delay(5000)
                getWeatherByCurrentLocation()
            }
        }
    }

    private fun observeWeather() {
        viewModel.currentLocationWeather.observe(this) {
            binding.pbLoading.visibility = View.GONE

            when (it) {
                is Resource.Loading -> {
                    Log.d("weather", "is loading")
                }
                is Resource.Error -> {
                    Log.d("weather", "is error ${it.message}")
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Empty -> {
                    Log.d("weather", "is empty ${it.message}")
                    Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    Log.d("weather", "is success ${it.data}")
                    if (it.message != null) {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                    bindWeatherToUi(it.data)
                }
                else -> {}
            }
        }
    }

    private fun bindWeatherToUi(data: WeatherUiModel?) {
        data?.let {
            with(binding) {
                Glide.with(this@MainActivity)
                    .load(data.weatherIcon)
                    .centerCrop()
                    .into(ivWeather)
                ivWeather.contentDescription = it.weatherDescription

                tvLastUpdated.text = it.lastUpdatedOn
                tvCurrentLocation.text = it.currentCity
                tvWeatherDescription.text = it.weatherDescription
                tvTodayDate.text = it.todayDate
                tvTemperature.text = it.temperature
                tvFeelsLikeTemperature.text = it.temperatureFeelsLike
                tvTemperatureMinMax.text = it.temperatureMinMax
                tvHumidity.text = it.humidity
                tvClouds.text = it.clouds
                tvWind.text = it.wind
                tvPressure.text = it.pressure
                tvVisibility.text = it.visibility
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}