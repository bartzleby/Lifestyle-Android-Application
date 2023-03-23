package com.lifestyle

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