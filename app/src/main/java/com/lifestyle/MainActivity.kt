package com.lifestyle

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.lifestyle.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var findHikesNearMe: String? = "hikes near me"
    //private var mButtonRegister: Button? = null

    private var COARSE_LOCATION_REQUEST: Int = 100

    // FusedLocationProviderClient - Main class for receiving location updates.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
//    private var locationData: LocationData? = null
    private var longitude: Double? = null
    private var latitude: Double? = null

    private var city: String? = null
    private var country: String? = null

    private lateinit var geocoder: Geocoder

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
            } else {
                Log.i("Permission: ", "Denied")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fabLocation.setOnClickListener { view ->
            when (view.id) {
                R.id.fab_location -> {
                    requestLocationPermission(view, "rGeocode")
                }
            }
        }

        binding.fabWeather.setOnClickListener { view ->
            Snackbar.make(view, "Missing location info", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        // When the user clicks on the fabHikesNearby object, Google Maps opens and searches for
        // "hikes nearby" near the users location
        binding.fabHikesNearby.setOnClickListener { view ->
            when (view.id) {
                R.id.fab_hikes_nearby -> {

                    /** This block is for testing... */
//                    // hard coded to NYC for testing purposes
//                    val searchUri = Uri.parse("geo:40.7128,74.0060?q=$findHikesNearMe")
//
//                    // create the mapIntent
//                    val mapIntent = Intent(Intent.ACTION_VIEW, searchUri)
//
//                    // open Google Maps using the mapIntent
//                    try {
//                        startActivity(mapIntent)
//                    } catch (ex: ActivityNotFoundException) {
//                        // If it failed, tell the user
//                        Snackbar.make(view, "Error: Failed to launch Google Maps!", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show()
//                    }

                    /** This block is what is intended to be used... */
                    // Request the users location
                    requestLocationPermission(view, "hike")

//                    if (isLocationPermissionGranted()) {
//                        // This is the hardcoded WEB location, for testing
////                        val searchUri = Uri.parse("geo:40.767778,-111.845205?q=$findHikesNearMe")
//                        val searchUri = Uri.parse("geo:${latitude},${longitude}?q=$findHikesNearMe")
//
//                        println("Opening maps, data is...")
//                        println("Latitude = " + latitude)
//                        println("Longitude = " + longitude)
//
//                        // create the mapIntent
//                        val mapIntent = Intent(Intent.ACTION_VIEW, searchUri)
//
//                        // open Google Maps using the mapIntent
//                        try {
//                            startActivity(mapIntent)
//                        } catch (ex: ActivityNotFoundException) {
//                            // If it failed, tell the user
//                            Snackbar.make(view, "Error: Failed to launch Google Maps!", Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show()
//                        }
//                    }
//                    else {
//                        Snackbar.make(view, "Please enable location permissions to find hikes near you.", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show()
//                    }

                    /** ...end of blocks to be tested against... */
                }
            }
        }
    }

    private fun requestLocationPermission(view: View, action: String) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is granted, set it
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                geocoder = Geocoder(this, Locale.getDefault())
                fusedLocationProviderClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            latitude  = location.latitude
                            longitude = location.longitude

                            println("After setting data...")
                            println("Latitude = " + latitude)
                            println("Longitude = " + longitude)
                            println("Raw data...")
                            println("Latitude = " + location.latitude)
                            println("Longitude = " + location.longitude)

                            if (action.equals("hike")){
                                findHikesNearby(view)
                            }
                            else if (action.equals("rGeocode")) {
                                fetchCityAndCountry()
                            }
                            else if (action.equals("weather")) {
                                getWeather()
                            }
                        }
                    }
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                Toast.makeText(this, "Location access required for weather and hiking services", Toast.LENGTH_LONG).show()
//                //Additional rationale should be displayed
////                layout.showSnackbar(
////                    view,
////                    getString(R.string.permission_required),
////                    Snackbar.LENGTH_INDEFINITE,
////                    getString(R.string.ok)
////                )
//                {
//                    requestPermissionLauncher.launch(
//                        Manifest.permission.ACCESS_COARSE_LOCATION
//                    )
////                Snackbar.make(
////                    view,
////                    "getString", Snackbar.LENGTH_LONG).setAction("Action", null).show()
////                )
//                }
            } else -> {
                // Permission has not been asked yet
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
        }
    }


    private fun findHikesNearby(view: View) {
        if (isLocationPermissionGranted()) {
            // This is the hardcoded WEB location, for testing
//                        val searchUri = Uri.parse("geo:40.767778,-111.845205?q=$findHikesNearMe")
            val searchUri = Uri.parse("geo:${latitude},${longitude}?q=$findHikesNearMe")

            println("Opening maps, data is...")
            println("Latitude = " + latitude)
            println("Longitude = " + longitude)

            // create the mapIntent
            val mapIntent = Intent(Intent.ACTION_VIEW, searchUri)

            // open Google Maps using the mapIntent
            try {
                startActivity(mapIntent)
            } catch (ex: ActivityNotFoundException) {
                // If it failed, tell the user
                Snackbar.make(view, "Error: Failed to launch Google Maps!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }
        else {
            Snackbar.make(view, "Please enable location permissions to find hikes near you.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }


    private fun fetchCityAndCountry() {
        val geocodeListener = Geocoder.GeocodeListener { addresses ->
            country = addresses[0].countryName
            city = addresses[0].locality

            println("reverse geocoding results:")
            println("city: " + city)
            println("country: " + country)
        }
        geocoder.getFromLocation(
            latitude!!,
            longitude!!,
            1,
            geocodeListener
        )
    }


    private fun getWeather() {
        // TODO: implement this method
    }

    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                COARSE_LOCATION_REQUEST
            )
            false
        } else {
            true
        }
    }


/**   override fun onClick(view: View) {
        when (view.id) {
            R.id.button_register -> {
                val fragment = UserInfo.newInstance();
                val transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id., fragment).commit();


            }
        }
    }

 */


    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        // https://stackoverflow.com/questions/18374183/how-to-show-icons-in-overflow-menu-in-actionbar
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        var fragmentId: Int? = null
        when (item.itemId) {
            R.id.action_bmr -> fragmentId = R.id.BmrFragment
        }
        fragmentId?.let {
            supportFragmentManager.commit {
                findNavController(R.id.nav_host_fragment_content_main).navigate(fragmentId)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
