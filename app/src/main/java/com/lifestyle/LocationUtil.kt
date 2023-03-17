package com.lifestyle

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class LocationUtil(): Service() {

    private var COARSE_LOCATION_REQUEST: Int = 100

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
        // https://developer.android.com/training/location/retrieve-current
        // https://developer.android.com/training/location/permissions


    }

//    private fun isLocationPermissionGranted(): Boolean {
//        return if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(
//                    ACCESS_COARSE_LOCATION
//                ),
//                COARSE_LOCATION_REQUEST
//            )
//            false
//        } else {
//            true
//        }
//    }
}