package com.mphasis.tanaji.weatherapp

import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.*
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.LocationServices
import com.mphasis.tanaji.weatherapp.adapter.WeatherAdapter
import com.mphasis.tanaji.weatherapp.base.Extensions.showSnackBar
import com.mphasis.tanaji.weatherapp.base.Extensions.showToast
import com.mphasis.tanaji.weatherapp.base.GpsLocationListener
import com.mphasis.tanaji.weatherapp.base.GpsLocationReceiver
import com.mphasis.tanaji.weatherapp.base.InternetConnectionListener
import com.mphasis.tanaji.weatherapp.base.Receiver
import com.mphasis.tanaji.weatherapp.databinding.ActivityMainBinding
import com.mphasis.tanaji.weatherapp.utilities.Utilities
import com.mphasis.tanaji.weatherapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity(), InternetConnectionListener, GpsLocationListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var context: Context
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private val MY_PERMISSIONS_REQUEST_LOCATION = 99
    private var internetConnectionReceiver = Receiver().apply {
        listener = this@MainActivity
    }
    private var gpsLocationReceiver = GpsLocationReceiver().apply {
        listener = this@MainActivity
    }
    private lateinit var location: Location
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        setEventHandler()
        initObserver()
    }

    private fun setEventHandler() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    binding.locationTv.text = query.toString()

                    getCityWiseData(
                        "",
                        "", query.toString()
                    )
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //Toast.makeText(context, newText,Toast.LENGTH_LONG).show()
                return true
            }

        })
    }

    private fun initObserver() {
        mainViewModel.isInternetConnected.observe(this) {
            if (it) {
                //  viewModel.getUsers("1", "12")
                checkPermissions(false)
            } else {
                showToast("No internet")
            }
        }
        mainViewModel.isGpsLocationConnected.observe(this) {
            it?.let {
                if (it) {
                    //  viewModel.getUsers("1", "12")
                    binding.mainLayout.showSnackBar("Gps Location is on")
                    checkPermissions(false)
                } else {
                    binding.mainLayout.showSnackBar("Gps Location is off")
                }
            }
        }
        mainViewModel.cityWiseResponse.observe(this) {
            it?.let {
                it.main.apply {
                    binding.tempTv.text = "$temp°"
                    binding.minTempTv.text = "L:$temp_min°"
                    binding.maxTempTv.text = "H:$temp_max°"
                    binding.pressureTv.text = "$pressure hpa"
                    binding.tvFeelLike.text = "$feels_like°"
                    binding.humidityTv.text = "$humidity%"
//                    binding.seaLevelTv.text = "Sea:" + sea_level.toString()
//                    binding.grandLevelTv.text = "Grand:" + grnd_level.toString()
                }
                it.wind.apply {
                    binding.speedTv.text = "$speed miles"
                    binding.degreeTv.text = "Direction: $deg°"
                }
                it.sys.apply {
                    binding.sunRiceTv.text = "Rise: ${Utilities.convertDate(sunrise.toLong())}"
                    binding.sunsetTv.text = "Set: ${Utilities.convertDate(sunset.toLong())}"
                }
            }
        }

        mainViewModel.weatherResponse.observe(this) {
            it?.let {
                it.data.current_condition.apply {
                    val current = get(0)
                    // binding.tempTv.text = "$temp°"
                    //

                    Glide.with(context).load(current.weatherIconUrl[0].value)
                        .placeholder(R.drawable.outline_wb_sunny_24)
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .dontAnimate()
                        .into(binding.weatherIconIv)

                    binding.minTempTv.text = "L:${current.temp_C}°"
                    binding.maxTempTv.text = "H:${current.temp_F}°"
                    binding.pressureTv.text = "${current.pressure} hpa"
                    binding.tvFeelLike.text = "${current.FeelsLikeC}°"
                    binding.humidityTv.text = "${current.humidity}%"
                    binding.degreeTv.text = "Direction: ${current.winddirDegree}°"
                    binding.speedTv.text = "${current.windspeedMiles} miles"
//                    binding.seaLevelTv.text = "Sea:" + sea_level.toString()
//                    binding.grandLevelTv.text = "Grand:" + grnd_level.toString()
                }

//                it.sys.apply {
//                    binding.sunRiceTv.text = "Rise: ${Utilities.convertDate(sunrise.toLong())}"
//                    binding.sunsetTv.text = "Set: ${Utilities.convertDate(sunset.toLong())}"
//                }
                binding.weatherDetailsRecyclerView.adapter =
                    WeatherAdapter(context, it.data.weather)
            }
        }
    }

    private fun init() {
        context = this@MainActivity
        binding.weatherDetailsRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        checkPermissions(true)


//        if (isOnline(context)) {
//            //viewModel.getUsers("1","12")
//        } else {
//            showToast("No internet")
//        }


    }

    private fun checkPermissions(b: Boolean) {

        if (ActivityCompat.checkSelfPermission(
                context,
                permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                context,
                permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (b)
                ActivityCompat.requestPermissions(
                    (context as Activity),
                    arrayOf(permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            // binding.locationTv.text = "On Location"
            // provideLocationAccess()
        } else {
            getAddressFromLatLng()
        }
    }

    private fun permissionPrompt() {
        val alertDialog = AlertDialog.Builder(context).apply {
            setTitle("Location Permission")
            setCancelable(false)
            setIcon(R.drawable.ic_alerted)
            setMessage("Location permissions mandatory for this app")
            setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
                //provideLocationAccess()
                context.startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                    )
                )
            }
            setNegativeButton(
                "Cancel"
            ) { dialog, _ ->
                dialog.dismiss()
                //provideLocationAccess()
                finish()
            }
        }

        alertDialog.create()
        alertDialog.show()
    }


    private fun provideLocationAccess() {
        val alertDialog = AlertDialog.Builder(context).apply {
            setTitle("Permission")
            setCancelable(false)
            setIcon(R.drawable.ic_alerted)
            setMessage("Please grant permission for location")

            setPositiveButton("Ok") { _, _ ->
                context.startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                    )
                )
            }
            setNegativeButton("cancel") { _, _ -> }
        }


        alertDialog.create()
        alertDialog.show()
    }

    private fun getCityWiseData(lat: String, lng: String, cityName: String) {
//        if (cityName.equals("", true))
//            mainViewModel.getCityWiseData(lat, lng, BaseUrl.appId)
//        else
//            mainViewModel.getCityWeather(cityName, BaseUrl.appId)

        mainViewModel.getCityWeatherNew(
            if (cityName.equals(
                    "",
                    true
                )
            ) "$lat,$lng" else cityName
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
                    getAddressFromLatLng()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                    // finish()
                    permissionPrompt()
                }
            }
        }
    }

    override fun onInternetConnectionChanged(isConnected: Boolean) {
        mainViewModel.isInternetConnected.value = isConnected
    }

    override fun onGpsLocationChanged(isConnected: Boolean) {
        mainViewModel.isGpsLocationConnected.value = isConnected
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(internetConnectionReceiver, filter)

        val filter2 = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        registerReceiver(gpsLocationReceiver, filter2)

        checkPermissions(false)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(internetConnectionReceiver)
        unregisterReceiver(gpsLocationReceiver)
    }

    private fun isOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }

    private fun isGpsOn(): Boolean {
        val service = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return service.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun getAddressFromLatLng()/*: List<Address> */ {
        try {
            if (isOnline()) {

                if (isGpsOn()) {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val client = LocationServices.getFusedLocationProviderClient(this)
                    client.lastLocation.addOnSuccessListener { loc: Location? ->

                        loc?.let {
                            location = loc

                            val addresses: List<Address>? = geocoder.getFromLocation(
                                location.latitude,
                                location.longitude,
                                1
                            )
                            addresses?.let {
                                val address: String =
                                    addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                val city: String = addresses[0].locality
                                val state: String = addresses[0].adminArea
                                val country: String = addresses[0].countryName
                                val postalCode: String = addresses[0].postalCode
                                val knownName: String =
                                    addresses[0].featureName // Only if available else return NULL
                                binding.locationTv.text = city

                                getCityWiseData(
                                    location.latitude.toString(),
                                    location.longitude.toString(), ""
                                )
                            }
                        }
                    }
                } else {
                    binding.mainLayout.showSnackBar("Gps Location is off, please on Gps location")
                }
            } else {
                binding.mainLayout.showSnackBar("No internet")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //return addresses
    }

}
