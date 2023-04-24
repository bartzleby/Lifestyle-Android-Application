package com.lifestyle

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.location.Geocoder
import android.content.pm.PackageManager
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
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.lifestyle.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var findHikesNearMe: String? = "hikes near"
    //private var mButtonRegister: Button? = null
    private var navController: NavController? = null

    private var FINE_LOCATION_REQUEST: Int = 100

    // FusedLocationProviderClient - Main class for receiving location updates.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    //    private var locationData: LocationData? = null
    private var longitude: Double? = null
    private var latitude: Double? = null

    private var city: String? = null
    private var country: String? = null
    private var address: String? = null

    private var locationShared: Boolean? = null

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

    private fun navigateToFragmentFromItem(item: MenuItem): Boolean {
        var fragmentId: Int? = null
        when (item.itemId) {
            R.id.BmrFragment -> fragmentId = R.id.BmrFragment
            R.id.UserInfo -> fragmentId = R.id.UserInfo
            R.id.WeatherFragment -> {
                if (locationShared!!) {
                    fragmentId = R.id.WeatherFragment
                } else {
                    Toast.makeText(this, "Please share your location to access weather", Toast.LENGTH_LONG).show()
                }
            }
        }
        fragmentId?.let {
            supportFragmentManager.commit {
                navController!!.navigate(fragmentId)
            }
        }
        return fragmentId != null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationShared = false

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.nav_host_fragment_content_main)

        // Used in tablet layout
        binding.navigationRail?.let {
            binding.navigationRail!!.setOnItemSelectedListener { item -> navigateToFragmentFromItem(item) }
            binding.navigationRail!!.setupWithNavController(navController!!)
        }

        // Used in phone layout
        binding.navigationBar?.let {
            binding.navigationBar!!.setOnItemSelectedListener { item -> navigateToFragmentFromItem(item) }
            binding.navigationBar!!.setupWithNavController(navController!!)
        }

        binding.fabLocation?.setOnClickListener { view ->
            when (view.id) {
                R.id.fab_location -> {
                    requestLocationPermission(view, "rGeocode")
                }
            }
        }

        // When the user clicks on the fabHikesNearby object, Google Maps opens and searches for
        // "hikes nearby" near the users location
        binding.fabHikesNearby?.setOnClickListener { view ->
            when (view.id) {
                R.id.fab_hikes_nearby -> {
                    if (locationShared!!) {
                        requestLocationPermission(view, "hike")
                    }
                    else {
                        Toast.makeText(this, "Please share your location to find hikes near you", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }

    private fun requestLocationPermission(view: View, action: String) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
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
                                getCityAndCountry()
                            }
                            else if (action.equals("weather")) {
                                getWeather()
                            }
                        }
                    }
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                Toast.makeText(this, "Location access required for weather and hiking services", Toast.LENGTH_LONG).show()
            } else -> {
                // Permission has not been asked yet
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }


    private fun findHikesNearby(view: View) {
        if (isLocationPermissionGranted()) {
            val searchUri = Uri.parse("geo:0,0?q=$findHikesNearMe $address")

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


    private fun getCityAndCountry() {
        val geocodeListener = Geocoder.GeocodeListener { addresses ->
            country = addresses[0].countryName
            city = addresses[0].locality
            address = addresses[0].getAddressLine(0)
        }
        geocoder.getFromLocation(
            latitude!!,
            longitude!!,
            1,
            geocodeListener
        )

        locationShared = true
    }


    private fun getWeather() {
        // TODO: implement this method
    }

    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                FINE_LOCATION_REQUEST
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
