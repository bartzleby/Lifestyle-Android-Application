package com.lifestyle

import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.lifestyle.databinding.FragmentWeatherBinding
import okhttp3.*
import java.io.IOException
import java.net.UnknownHostException
import java.util.*

class WeatherFragment : Fragment() {
    private val client = OkHttpClient()
    private val baseUrl = "https://api.tomorrow.io/v4/weather/realtime"
    private val wapikey: String = BuildConfig.WAPI_KEY

    private lateinit var weatherCodes: WeatherCodes

    private lateinit var tvCity: TextView
    private lateinit var tvTemperature: TextView
    private lateinit var tvCondition: TextView
    private lateinit var ivCondition: ImageView
    private lateinit var rvForecast: RecyclerView

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        val view = binding.root

        val gson = GsonBuilder().create()
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

        tvCity = view.findViewById(R.id.tv_city)
        tvTemperature = view.findViewById(R.id.tv_temperature)
        tvCondition = view.findViewById(R.id.tv_condition)
        ivCondition = view.findViewById(R.id.iv_condition)
        rvForecast = view.findViewById(R.id.rv_forecast)

        val location = "40.75872069597532,-73.98529171943665"
        fetchWeather(location)

        return view
    }

    fun Fragment?.runOnUiThread(action: () -> Unit) {
        this ?: return
        if (!isAdded) return // Fragment not attached to an Activity
        activity?.runOnUiThread(action)
    }

    private fun fetchWeather(location: String) {
        val url =
            "$baseUrl?location=$location&apikey=$wapikey"
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
                    val weatherCode = weatherInfo.data.values.weatherCode
                    val weatherDescription = weatherCodes.weatherCode[weatherCode].toString()
                    tvCondition.text = weatherDescription.substring(1, weatherDescription.length - 1) // hacky to remove bounding quotes
                    tvTemperature.text = "${weatherInfo.data.values.temperature.toString()} \u2103"
                    tvCity.text = getCityName(
                        weatherInfo.location.lat,
                        weatherInfo.location.lon
                    )

                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    // hacky, from https://stackoverflow.com/questions/73497416/how-can-i-find-city-name-from-longitude-and-latitude-kotlin
    private fun getCityName(lat: Double,long: Double): String? {
        var cityName: String?
        val geoCoder = Geocoder(activity!!, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat,long,1)
        cityName = address?.get(0)?.adminArea
        if (cityName == null){
            cityName = address?.get(0)?.locality
            if (cityName == null){
                cityName = address?.get(0)?.subAdminArea
            }
        }
        return cityName
    }

}
