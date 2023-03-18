package com.lifestyle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.GsonBuilder
import com.lifestyle.databinding.FragmentWeatherBinding
import okhttp3.*
import java.io.IOException

class WeatherFragment : Fragment() {
    private val client = OkHttpClient()
    private val baseUrl = "https://api.tomorrow.io/v4/weather/realtime"
    private val wapikey: String = BuildConfig.WAPI_KEY

    private lateinit var tv_temp: TextView

    private var _binding: FragmentWeatherBinding? = null

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        val view = binding.root

        val btnFetchWeather = view.findViewById<View>(R.id.button_fetch_weather) as Button
        tv_temp = view.findViewById(R.id.tv_temperature)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(getActivity()!!.applicationContext)

        val location = "40.75872069597532,-73.98529171943665"

        btnFetchWeather.setOnClickListener {
            fetchWeather(location)
        }

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
                //TODO: handle network errors, etc.
            }

            override fun onResponse(call: Call, response: Response) {
                val resbody = response.body?.string()
                val gson = GsonBuilder().create()
                val weatherInfo = gson.fromJson(resbody, TomorrowResponse::class.java)

//                println(resbody)
                runOnUiThread {
                    tv_temp.text = weatherInfo.data.values.temperature.toString()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}

class TomorrowResponse(
    val data: TomorrowData,
    val location: TomorrowLocation
)

class TomorrowLocation(
    val lat: Float,
    val lon: Float
)

class TomorrowData(
    val time: String,
    val values: WeatherValues
)

class WeatherValues(
    val cloudBase: Float?,
    val cloudCeiling: Float?,
    val cloudCover: Float?,
    val dewPoint: Float?,
    val freezingRainIntensity: Float?,
    val humidity: Float?,
    val precipitationProbability: Float?,
    val pressureSurfaceLevel: Float?,
    val rainIntensity: Float?,
    val sleetIntensity: Float?,
    val snowIntensity: Float?,
    val temperature: Float?,
    val temperatureApparent: Float?,
    val uvHealthConcern: Float?,
    val uvIndex: Float?,
    val visibility: Float?,
    val weatherCode: Float?,
    val windDirection: Float?,
    val windGust: Float?,
    val windSpeed: Float?
)

