package com.aar.tryhuawei.locationkit

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.aar.tryhuawei.R
import com.huawei.hms.common.ApiException
import com.huawei.hms.common.ResolvableApiException
import com.huawei.hms.location.LocationAvailability
import com.huawei.hms.location.LocationCallback
import com.huawei.hms.location.LocationResult
import com.huawei.hms.location.LocationSettingsStatusCodes
import com.huawei.hms.maps.MapFragment
import kotlinx.android.synthetic.main.activity_location_kit.*
import java.lang.Exception


class LocationKitActivity : AppCompatActivity() {

    private lateinit var locationApi: HuaweiLocationApi
    private val locationErrorListener = object: HuaweiLocationApi.ErrorListener {
        override fun onError(error: Exception) {
            if (error is ApiException) {
                if (error.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    (error as ResolvableApiException).startResolutionForResult(this@LocationKitActivity, RESOLVE_LOCATION_RESOLUTION)
                }
            }

            Toast.makeText(this@LocationKitActivity, "Something wrong with location service", Toast.LENGTH_SHORT).show()
        }
    }

    private val locationUpdateListener = object: LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?.lastLocation?.let { showLocation(it) }
        }

        override fun onLocationAvailability(locationAvailability: LocationAvailability?) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_kit)

        locationApi = HuaweiLocationApi(this, locationErrorListener, locationUpdateListener)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as MapFragment?
        mapFragment?.getMapAsync {
            Log.d("TestMe", "map view ready")
        }
    }

    override fun onStart() {
        super.onStart()

        val permissionAlreadyGranted = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            askForPermission()
        } else {
            askForPermissionAndroidQAbove()
        }

        if (permissionAlreadyGranted) {
            locationApi.startLocationUpdate()
        }
    }

    override fun onStop() {
        super.onStop()
        locationApi.stopLocationUpdate()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQ_CODE || requestCode == PERMISSION_REQ_CODE_Q) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                locationApi.startLocationUpdate()
            } else {
                Toast.makeText(this, "Please grant all permission", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == RESOLVE_LOCATION_RESOLUTION) {
            locationApi.startLocationUpdate()
        }
    }

    private fun showLocation(location: Location) {
        textLat.text = location.latitude.toString()
        textLng.text = location.longitude.toString()
        textTime.text = location.time.toString()
    }

    private fun askForPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            val strings = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            ActivityCompat.requestPermissions(this, strings, PERMISSION_REQ_CODE)
            return false
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun askForPermissionAndroidQAbove(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            val strings = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
            ActivityCompat.requestPermissions(this, strings, PERMISSION_REQ_CODE_Q)
            return false
        }
        return true
    }
    companion object {
        private const val PERMISSION_REQ_CODE = 1
        private const val PERMISSION_REQ_CODE_Q = 2
        private const val RESOLVE_LOCATION_RESOLUTION = 3
    }

}
