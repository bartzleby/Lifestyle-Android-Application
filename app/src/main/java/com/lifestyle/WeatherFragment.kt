package com.lifestyle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lifestyle.databinding.FragmentWeatherBinding
import kotlin.collections.ArrayList
import kotlin.math.round

class WeatherFragment : Fragment() {

    private lateinit var weatherCodes: WeatherCodes

    private lateinit var pbLoading: ProgressBar
    private lateinit var tvCity: TextView
    private lateinit var tvTemperature: TextView
    private lateinit var tvCondition: TextView
    private lateinit var ivCondition: ImageView
    private lateinit var rvForecast: RecyclerView
    private lateinit var rvAdapter: RecyclerView.Adapter<*>
    private var weatherData: List<WeatherDataForecast> = ArrayList()
    private lateinit var layoutManager: RecyclerView.LayoutManager

    private val mLifestyleViewModel: LifestyleViewModel by viewModels {
        LifestyleViewModelFactory((requireContext().applicationContext as LifestyleApplication).repository)
    }

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        val view = binding.root

        val gson = GsonBuilder().create()
        readWeatherCodes(gson)

        // set an observer for the flow-converted-to-livedata objects
        mLifestyleViewModel.liveUserData.observe(this, userDataObserver)
        mLifestyleViewModel.currentWeather.observe(this, currentWeatherFlowObserver)
        mLifestyleViewModel.forecastWeather.observe(this, forecastWeatherFlowObserver)

        // TODO: change background based on time of day

        pbLoading = view.findViewById(R.id.pb_loading)
        tvCity = view.findViewById(R.id.tv_city)
        tvTemperature = view.findViewById(R.id.tv_temperature)
        tvCondition = view.findViewById(R.id.tv_condition)
        ivCondition = view.findViewById(R.id.iv_condition)

        // set up forecast recycler view
        rvForecast = view.findViewById(R.id.rv_forecast)
        rvForecast.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        (layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
        rvForecast.layoutManager = layoutManager

        // set RV Adapter
        rvAdapter = WeatherRVAdapter(ArrayList(weatherData), null)
        rvForecast.adapter = rvAdapter

        // clear weather data and indicatate a loading status to user
        mLifestyleViewModel.clearWeatherData()
        pbLoading.visibility = View.VISIBLE

        return view
    }

    private val userDataObserver: Observer<UserData> =
        Observer { userData ->
            if (userData != null) {
                val loc = userData.city

                if (loc.isNullOrBlank()) {
                    // TODO: tell user to submit data:

                } else {
                    // we perform this asynchronously via the viewmodel
                    mLifestyleViewModel.fetchWeather(loc)
                }
            }
        }

    // This observer is triggered when the Flow object in the repository
    // detects a change to the database (including at the start of the app)
    // we update relevant UI elements
    private val currentWeatherFlowObserver: Observer<WeatherDataCurrent> =
        Observer { weatherData ->
            if (weatherData != null) {
                pbLoading.visibility = View.GONE
                val weatherCode = weatherData.weatherCode
                var weatherDescription = weatherCodes.weatherCode[weatherCode].toString()
                weatherDescription = weatherDescription.substring(
                    1,
                    weatherDescription.length - 1
                ) // hacky to remove bounding quotes

                // map description to icon as described,
                // and set image view appropriately
                weather_code_to_icon_map[weatherCode.toInt()]?.let {
                    ivCondition.setImageResource(it)
                }

                // TODO: support multi-line condition description
                tvCondition.text = weatherDescription
                tvTemperature.text =
                    "${weatherData.temperature.let { round(it).toString().dropLast(2) }} \u2103"
                tvCity.text = weatherData.city
            }
        }

    private val forecastWeatherFlowObserver: Observer<List<WeatherDataForecast>> =
        Observer { weatherData ->
            if (weatherData != null) {
                // update RV Adapter with our received forecast data
                rvAdapter = WeatherRVAdapter(ArrayList(weatherData), weatherCodes)
                rvForecast.adapter = rvAdapter
            }
        }


    // TODO
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    // TODO
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    /* reads map of weather code to condition description
     * into fragment variable weatherCodes
     */
    private fun readWeatherCodes(gson: Gson) {
        weatherCodes = gson.fromJson(
            activity!!.applicationContext.resources.openRawResource(
                activity!!.applicationContext.resources.getIdentifier(
                    "weather_codes",
                    "raw",
                    activity!!.applicationContext.packageName
                )
            ).bufferedReader().use { it.readText() },
            WeatherCodes::class.java
        )
    }
}