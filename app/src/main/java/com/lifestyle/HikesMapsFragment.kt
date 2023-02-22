package com.lifestyle

import android.Manifest
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import com.lifestyle.PermissionUtils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class HikesMapsFragment : Fragment(), OnRequestPermissionsResultCallback {

    private var permissionDenied = false
    private lateinit var map: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hikes_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    /**
     * Enables the My Location layer if the user has granted permission for coarse location data
     * Otherwise, requests it
     * Based on example code found here:
     * https://developers.google.com/maps/documentation/android-sdk/location#kotlin
     */
    private fun enableMyLocation() {

        // If the user granted permission for coarse location data, then enable the
        // my location layer
        if (ActivityCompat.checkSelfPermission(
                activity!!.applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            return
        }

        // TODO: If they didn't grant access, it would be good to tell them why we want it
        /**
         * Example code can be found here:
         * https://developers.google.com/maps/documentation/android-sdk/location#kotlin
         *
         * This is based on "2. If a permission rationale dialog should be shown"
         */
        /*if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            PermissionUtils.RequestDialog.newInstance(
                LOCATION_PERMISSION_REQUEST_CODE, true
            ).show(supportFragmentManager, "dialog")
            return
        }

        // If the user didn't grant permission for coarse location data, request it
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION
            ), LOCATION_PERMISSION_REQUEST_CODE
        )*/

    }
}