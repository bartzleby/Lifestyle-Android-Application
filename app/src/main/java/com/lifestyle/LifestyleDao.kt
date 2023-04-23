package com.lifestyle

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LifestyleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userTable: UserData)

    @Query("DELETE from user_table")
    suspend fun clearUserTable()

    // Get all user information
    @Query("SELECT * from user_table")
    fun getAllUserData(): Flow<UserData>

    // most recent user information submission is considered current user
    // select the location associated with the most recently added user information
    @Query("SELECT * FROM user_table ORDER BY id DESC LIMIT 1")
    fun getCurrentUserData(): Flow<UserData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateWeather(CurrentWeatherTable: WeatherDataCurrent)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWeatherForecast(ForecastWeatherTable: WeatherDataForecast)

    @Query("DELETE from weather_current")
    fun deleteCurrentWeather()

    // Get the most recent entry in weather_current table TODO
    // automatically triggered when the db is updated because of Flow<WeatherDataCurrent>
    @Query("SELECT * from weather_current ORDER BY id")
    fun getCurrentWeather(): Flow<WeatherDataCurrent>

    // Get all entries in weather_forecast table
    @Query("SELECT * from weather_forecast ORDER BY time")
    fun getForecastWeather(): Flow<List<WeatherDataForecast>>

    @Query("DELETE from weather_forecast")
    fun deleteForecastWeather()

}