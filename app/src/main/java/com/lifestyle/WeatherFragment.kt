package com.lifestyle

import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lifestyle.databinding.FragmentWeatherBinding
import okhttp3.*
import java.io.IOException
import java.net.UnknownHostException
import java.util.*
import kotlin.collections.ArrayList

class WeatherFragment : Fragment() {
    private val client = OkHttpClient()
    private val baseUrl = "https://api.tomorrow.io/v4/weather"
    private val wapikey: String = BuildConfig.WAPI_KEY
    private val ts = "1h"

    private lateinit var weatherCodes: WeatherCodes

    private lateinit var pbLoading: ProgressBar
    private lateinit var tvCity: TextView
    private lateinit var tvTemperature: TextView
    private lateinit var tvCondition: TextView
    private lateinit var ivCondition: ImageView
    private lateinit var rvForecast: RecyclerView
    private lateinit var rvAdapter: RecyclerView.Adapter<*>
    private var weatherData: ArrayList<TomorrowData> = ArrayList()
    private lateinit var layoutManager: RecyclerView.LayoutManager

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

        pbLoading = view.findViewById(R.id.pb_loading)
        tvCity = view.findViewById(R.id.tv_city)
        tvTemperature = view.findViewById(R.id.tv_temperature)
        tvCondition = view.findViewById(R.id.tv_condition)
        ivCondition = view.findViewById(R.id.iv_condition)

        // set up forecast recycler view
        rvForecast = view.findViewById(R.id.rv_forecast)
        rvForecast!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        (layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
        rvForecast!!.layoutManager = layoutManager

        // set RV Adapter
        rvAdapter = WeatherRVAdapter(weatherData, null)
        rvForecast.adapter = rvAdapter

        val location = "40.75872069597532,-73.98529171943665"
        fetchWeather(location)
        fetchWeatherForecast(location)

        return view
    }

    /*
     * Fetch real time weather from tomorrow.io realtime endpoint
     */
    private fun fetchWeather(location: String) {
        val url =
            "$baseUrl/realtime?location=$location&apikey=$wapikey"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                when (e) {
                    is UnknownHostException -> {
                        println(e) // weak wifi? ,  no internet?
                    }
                    else -> {
                    }
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val resbody = response.body?.string()
                val gson = GsonBuilder().create()
                val weatherInfo = gson.fromJson(resbody, TomorrowResponse::class.java)

                runOnUiThread {
                    pbLoading.visibility = View.GONE
                    val weatherCode = weatherInfo.data.values.weatherCode
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

                    tvCondition.text = weatherDescription
                    tvTemperature.text = "${weatherInfo.data.values.temperature.toString()} \u2103"
                    tvCity.text = getCityName(
                        weatherInfo.location.lat,
                        weatherInfo.location.lon
                    )


                }
            }
        })
    }

    /*
     * Fetch hourly forecast from tomorrow.io forecast endpoint
     */
    private fun fetchWeatherForecast(location: String) {
        val url =
            "$baseUrl/forecast?location=$location&timesteps=$ts&apikey=$wapikey"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                when (e) {
                    is UnknownHostException -> {
                        println(e) // weak wifi? ,  no internet?
                    }
                    else -> {
                    }
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val resbody = response.body?.string()
                val gson = GsonBuilder().create()
                val weatherForecastInfo = gson.fromJson(resbody, TomorrowHourlyForecast::class.java)

                runOnUiThread {
                    weatherData.clear()
                    weatherData = weatherForecastInfo.timelines.hourly

                    // update RV Adapter with our received forecast data
                    rvAdapter = WeatherRVAdapter(weatherData, weatherCodes)
                    rvForecast.adapter = rvAdapter

                }
            }
        })
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

    // hacky, from https://stackoverflow.com/questions/73497416/how-can-i-find-city-name-from-longitude-and-latitude-kotlin
    private fun getCityName(lat: Double, long: Double): String? {
        var cityName: String?
        val geoCoder = Geocoder(activity!!, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, long, 1)
        cityName = address?.get(0)?.adminArea
        if (cityName == null) {
            cityName = address?.get(0)?.locality
            if (cityName == null) {
                cityName = address?.get(0)?.subAdminArea
            }
        }
        return cityName
    }

    fun Fragment?.runOnUiThread(action: () -> Unit) {
        this ?: return
        if (!isAdded) return // Fragment not attached to an Activity
        activity?.runOnUiThread(action)
    }

}