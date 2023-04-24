package com.lifestyle

import androidx.annotation.WorkerThread
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import java.net.UnknownHostException
import kotlin.jvm.Synchronized

class LifestyleRepository private constructor(lifestyleDao: LifestyleDao) {

    // These flows are triggered when any change happens to the respective databases
    val currentUserData: Flow<UserData> = lifestyleDao.getCurrentUserData()
    val currentWeatherData: Flow<WeatherDataCurrent> = lifestyleDao.getCurrentWeather()
    val forecastWeatherData: Flow<List<WeatherDataForecast>> = lifestyleDao.getForecastWeather()

    private var mLifestyleDao: LifestyleDao = lifestyleDao

    // define values used for tomorrow weather API
    private val client = OkHttpClient()
    private val baseUrl = "https://api.tomorrow.io/v4/weather"
    private val wapikey: String = BuildConfig.WAPI_KEY
    private val ts = "1h"

    @WorkerThread
    fun setUserData(userData: UserData) {
        mScope.launch(Dispatchers.IO) {
            mLifestyleDao.insert(userData)
        }
    }

    @WorkerThread
    fun updateUserData(userData: UserData) {
        mScope.launch(Dispatchers.IO) {
            mLifestyleDao.update(userData)
        }
    }

    @WorkerThread
    fun clearActive() {
        mScope.launch {
            mLifestyleDao.clearActive()
        }
    }

    @WorkerThread
    fun clearUserData() {
        mScope.launch {
            mLifestyleDao.clearUserTable()
        }
    }

    @WorkerThread
    fun clearWeatherData() {
        mScope.launch {
            mLifestyleDao.deleteCurrentWeather()
            mLifestyleDao.deleteForecastWeather()
        }
    }

    @WorkerThread
    fun fetchWeather(loc: String) {
        mScope.launch(Dispatchers.IO) {
            fetchWeatherCurrent(loc)
            fetchWeatherForecast(loc)
        }
    }

    /*
     * Fetch real time weather from tomorrow.io realtime endpoint
     */
    private fun fetchWeatherCurrent(location: String) {
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

                mLifestyleDao.deleteCurrentWeather()
                if (weatherInfo != null && weatherInfo.data !=null) {
                    // insert desired fields into weather_current table
                    mLifestyleDao.updateWeather(
                        WeatherDataCurrent(
                            weatherInfo.data.time,
                            weatherInfo.location.lat,
                            weatherInfo.location.lon,
                            location,
                            weatherInfo.data.values.humidity,
                            weatherInfo.data.values.temperature,
                            weatherInfo.data.values.temperatureApparent,
                            weatherInfo.data.values.weatherCode
                        )
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

                mLifestyleDao.deleteForecastWeather()
                weatherForecastInfo?.timelines?.hourly?.forEach { tl ->
                    mLifestyleDao.addWeatherForecast(
                        WeatherDataForecast(
                            tl.time,
                            weatherForecastInfo.location.lat,
                            weatherForecastInfo.location.lon,
                            location,
                            tl.values.humidity,
                            tl.values.temperature,
                            tl.values.temperatureApparent,
                            tl.values.weatherCode
                        )
                    )
                }
            }
        })
    }


    // Make the repository singleton. Could in theory
    // make this an object class, but the companion object approach
    // is nicer (imo)
    companion object {
        private var mInstance: LifestyleRepository? = null
        private lateinit var mScope: CoroutineScope

        @Synchronized
        fun getInstance(
            lifestyleDao: LifestyleDao,
            scope: CoroutineScope
        ): LifestyleRepository {
            mScope = scope
            return mInstance ?: synchronized(this) {
                val instance = LifestyleRepository(lifestyleDao)
                mInstance = instance
                instance
            }
        }
    }


}

