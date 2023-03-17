package com.lifestyle

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.lifestyle.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    //private var mButtonRegister: Button? = null

    private fun navigateToFragmentFromItem(item: MenuItem): Boolean {
        var fragmentId: Int? = null
        when (item.itemId) {
            R.id.item_bmr, R.id.action_bmr -> fragmentId = R.id.BmrFragment
            R.id.item_user -> fragmentId = R.id.UserInfo
        }
        fragmentId?.let {
            supportFragmentManager.commit {
                findNavController(R.id.nav_host_fragment_content_main).navigate(fragmentId)
            }
        }
        return fragmentId != null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Used in tablet layout
        binding.navigationRail?.let {
            binding.navigationRail!!.setOnItemSelectedListener { item -> navigateToFragmentFromItem(item) }
        }

        // Used in phone layout
        binding.navigationBar?.let {
            binding.navigationBar!!.setOnItemSelectedListener { item -> navigateToFragmentFromItem(item) }
        }

//        binding.fabWeather.setOnClickListener { view ->
//            Snackbar.make(view, "Missing location info", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }
    }

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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
