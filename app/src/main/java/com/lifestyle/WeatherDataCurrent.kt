//
// class corresponding to current weather database table
//
package com.lifestyle

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "weather_current")
class WeatherDataCurrent(
    time: String,
    lat: Double,
    lon: Double,
    city: String,
    humidity: Float,
    temperature: Float,
    temperatureApparent: Float,
    weatherCode: String
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var time: String = time
    var lat: Double = lat
    var lon: Double = lon
    var city: String = city
    var humidity: Float = humidity
    var temperature: Float = temperature
    var temperatureApparent: Float = temperatureApparent
    var weatherCode: String = weatherCode

}
