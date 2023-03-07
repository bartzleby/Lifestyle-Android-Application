package com.lifestyle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.lifestyle.databinding.FragmentWeatherBinding
import okhttp3.*
import java.io.IOException

class WeatherFragment : Fragment() {
    private val client = OkHttpClient()
    private val baseUrl = "https://api.tomorrow.io/v4/weather/realtime"
    private val location = "40.75872069597532,-73.98529171943665"
    private val wapikey: String = BuildConfig.WAPI_KEY
    private var _binding: FragmentWeatherBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        val view = binding.root
        val btnFetchWeather = view.findViewById<View>(R.id.button_fetch_weather) as Button
        btnFetchWeather.setOnClickListener { _ ->
            Toast.makeText(
                getActivity()!!.getApplicationContext(),
                "fetching weather",
                Toast.LENGTH_SHORT
            ).show()

            fetchWeather()
        }

        return view
    }

    private fun fetchWeather(): String {
        val url =
            baseUrl + "?location="+location+"&apikey="+wapikey
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) = println(response.body?.string())
        })

        return "fetched"
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
