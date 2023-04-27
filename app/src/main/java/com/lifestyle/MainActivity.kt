package com.lifestyle

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.lifestyle.databinding.ActivityMainBinding
import java.util.*
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null
    private var mCity: String? = null

    private val mLifestyleViewModel: LifestyleViewModel by viewModels {
        LifestyleViewModelFactory((application as LifestyleApplication).repository)
    }

    private fun navigateToFragmentFromItem(item: MenuItem): Boolean {
        var fragmentId: Int? = null
        when (item.itemId) {
            R.id.BmrFragment -> fragmentId = R.id.BmrFragment
            R.id.UserInfo -> fragmentId = R.id.UserInfo
            R.id.WeatherFragment -> {
                    fragmentId = R.id.WeatherFragment
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

        mLifestyleViewModel.liveUserData.observe(this, userDataObserver)

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

        // When the user clicks on the fabHikesNearby object, Google Maps opens and searches for
        // "hikes near <city>" near the users set location
        binding.fabHikesNearby?.setOnClickListener { view ->
            when (view.id) {
                R.id.fab_hikes_nearby -> {
                    findHikesNearby(view)
                }
            }
        }

    }

    private val userDataObserver: Observer<UserData> =
        Observer { userData ->
            if (userData != null) {
                mCity = userData.city
            } else {
                // if the user is logged out, make sure the location is set
                // to null so that they are prompted to submit their info
                mCity = null
            }
        }

    // Open Google Maps searching for hikes near the users set location
    private fun findHikesNearby(view: View) {
        val TAG = "findHikesNearby"
        Log.d(TAG, "mCity = $mCity")

        if (mCity != null) {
            val findHikesNear = "hikes near"

            val searchUri = Uri.parse("geo:0,0?q=$findHikesNear $mCity")

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
            Snackbar.make(view, "Please submit location data to find hikes near you.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
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
