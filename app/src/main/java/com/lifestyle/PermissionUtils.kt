package com.lifestyle

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment

/**
 * An object class that provides runtime permission utilities
 * Based on code found here:
 * https://github.com/googlemaps/android-samples/blob/main/ApiDemos/kotlin/app/src/gms/java/com/example/kotlindemos/PermissionUtils.kt
 */
object PermissionUtils {
    class RequestDialog : DialogFragment() {
        private var finished = false
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // TODO: complete this method
            /*val requestCode = arguments?.getInt(ARGUMENT_PERMISSION_REQUEST_CODE) ?: 0
            finished = arguments?.getBoolean(ARGUMENT_FINISH_ACTIVITY) ?: false
            return AlertDialog.Builder(activity)
                .setMessage(R.string.permission_location_rationale)
                .setPositiveButton(android.R.string.ok)
                {
                    dialog, which ->
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
                    )
                }*/
            return super.onCreateDialog(savedInstanceState)
        }
    }
}