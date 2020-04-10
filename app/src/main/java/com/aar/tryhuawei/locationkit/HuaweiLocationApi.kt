package com.aar.tryhuawei.locationkit

import android.content.Context
import android.os.Looper
import android.util.Log
import com.huawei.hms.location.*
import java.lang.Exception

class HuaweiLocationApi(context: Context,
                        private val errorListener: ErrorListener,
                        private val locationCallback: LocationCallback) {

    private val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val settingsClient = LocationServices.getSettingsClient(context)

    public fun startLocationUpdate() {
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 10_000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()

        settingsClient.checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener {
                Log.d(TAG, "check location settings success")
                requestFusedLocation(locationRequest)
            }
            .addOnFailureListener {
                Log.d(TAG, "check location settings failed: $it")
                errorListener.onError(it)
            }
    }

    public fun stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            .addOnSuccessListener {
                Log.d(TAG, "stop location update success")
            }
            .addOnFailureListener {
                Log.d(TAG, "stop location update failed")
            }
    }

    private fun requestFusedLocation(locationRequest: LocationRequest) {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            .addOnSuccessListener {
                Log.d(TAG, "request location update success")
            }
            .addOnFailureListener {
                Log.d(TAG, "request location update failed")
                errorListener.onError(it)
            }
    }

    public interface ErrorListener {
        fun onError(error: Exception)
    }

    companion object {
        private const val TAG = "HuaweiLocationApi"
    }
}