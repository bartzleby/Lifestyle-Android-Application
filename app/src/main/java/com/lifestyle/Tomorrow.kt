//
// Contains classes corresponding to tomorrow.io
// API responses for simple unpacking
//

package com.lifestyle

import com.google.gson.JsonObject

class TomorrowResponse(
    val data: TomorrowData,
    val location: TomorrowLocation
)

class TomorrowHourlyForecast(
    val timelines: TimeLines,
    val location: TomorrowLocation
)

class TimeLines(
    val hourly: ArrayList<TomorrowData>
)

class TomorrowLocation(
    val lat: Double,
    val lon: Double
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
    val weatherCode: String,
    val windDirection: Float?,
    val windGust: Float?,
    val windSpeed: Float?
)

class WeatherCodes(
    val weatherCode: JsonObject,
    val weatherCodeFullDay: JsonObject,
    val weatherCodeDay: JsonObject,
    val weatherCodeNight: JsonObject
)

// see https://docs.tomorrow.io/reference/data-layers-weather-codes
val weather_code_to_icon_map = mapOf(
    1000 to R.drawable.ti_clear_day,
    10000 to R.drawable.ti_clear_day,
    10001 to R.drawable.ti_clear_night,

    1100 to R.drawable.ti_mostly_clear_day,
    11000 to R.drawable.ti_mostly_clear_day,
    11001 to R.drawable.ti_mostly_clear_night,

    1101 to R.drawable.ti_partly_cloudy_day,
    11010 to R.drawable.ti_partly_cloudy_day,
    11011 to R.drawable.ti_partly_cloudy_night,

    1102 to R.drawable.ti_mostly_cloudy,
    11020 to R.drawable.ti_mostly_cloudy, // day
    11021 to R.drawable.ti_mostly_cloudy, // night

    1001 to R.drawable.ti_cloudy,
    10010 to R.drawable.ti_cloudy, // day
    10011 to R.drawable.ti_cloudy, // night

    // 1103 mixed conditions

    2100 to R.drawable.ti_fog_light,
    21000 to R.drawable.ti_fog_light,
    21001 to R.drawable.ti_fog_light,

    2000 to R.drawable.ti_fog,
    20000 to R.drawable.ti_fog,
    20001 to R.drawable.ti_fog,

    // 2101 mixed conditions
    // 2102 mixed conditions
    // 2103 mixed conditions
    // 2106 mixed conditions
    // 2107 mixed conditions
    // 2108 mixed conditions

    4000 to R.drawable.ti_drizzle,
    40000 to R.drawable.ti_drizzle,
    40001 to R.drawable.ti_drizzle,

    4200 to R.drawable.ti_rain_light,
    42000 to R.drawable.ti_rain_light,
    42001 to R.drawable.ti_rain_light,

    4001 to R.drawable.ti_rain,
    40010 to R.drawable.ti_rain,
    40011 to R.drawable.ti_rain,

    4201 to R.drawable.ti_rain_heavy,
    42010 to R.drawable.ti_rain_heavy,
    42011 to R.drawable.ti_rain_heavy,

    // 4203 mixed conditions
    // 4204 mixed conditions
    // 4205 mixed conditions
    // 4213 mixed conditions
    // 4214 mixed conditions
    // 4215 mixed conditions
    // 4209 mixed conditions
    // 4208 mixed conditions
    // 4210 mixed conditions
    // 4211 mixed conditions
    // 4202 mixed conditions
    // 4212 mixed conditions

    5001 to R.drawable.ti_flurries,
    50010 to R.drawable.ti_flurries,
    50011 to R.drawable.ti_flurries,

    5100 to R.drawable.ti_snow_light,
    51000 to R.drawable.ti_snow_light,
    51001 to R.drawable.ti_snow_light,

    5000 to R.drawable.ti_snow,
    50000 to R.drawable.ti_snow,
    50001 to R.drawable.ti_snow,

    5101 to R.drawable.ti_snow_heavy,
    51010 to R.drawable.ti_snow_heavy,
    51011 to R.drawable.ti_snow_heavy,

    // 5115 mixed conditions
    // 5116 mixed conditions
    // 5117 mixed conditions
    // 5122 mixed conditions
    // 5102 mixed conditions
    // 5103 mixed conditions
    // 5104 mixed conditions
    // 5105 mixed conditions
    // 5106 mixed conditions
    // 5107 mixed conditions
    // 5119 mixed conditions
    // 5120 mixed conditions
    // 5121 mixed conditions
    // 5110 mixed conditions
    // 5108 mixed conditions
    // 5114 mixed conditions
    // 5112 mixed conditions

    6000 to R.drawable.ti_freezing_drizzle,
    60000 to R.drawable.ti_freezing_drizzle,
    60001 to R.drawable.ti_freezing_drizzle,

    6200 to R.drawable.ti_freezing_rain_light,
    62000 to R.drawable.ti_freezing_rain_light,
    62001 to R.drawable.ti_freezing_rain_light,

    6001 to R.drawable.ti_freezing_rain,
    60010 to R.drawable.ti_freezing_rain,
    60011 to R.drawable.ti_freezing_rain,

    6201 to R.drawable.ti_freezing_rain_heavy,
    62010 to R.drawable.ti_freezing_rain_heavy,
    62011 to R.drawable.ti_freezing_rain_heavy,

    // 6003 mixed conditions
    // 6002 mixed conditions
    // 6004 mixed conditions
    // 6204 mixed conditions
    // 6206 mixed conditions
    // 6205 mixed conditions
    // 6203 mixed conditions
    // 6209 mixed conditions
    // 6213 mixed conditions
    // 6214 mixed conditions
    // 6215 mixed conditions
    // 6212 mixed conditions
    // 6220 mixed conditions
    // 6222 mixed conditions
    // 6207 mixed conditions
    // 6202 mixed conditions
    // 6208 mixed conditions

    7102 to R.drawable.ti_ice_pellets_light,
    71020 to R.drawable.ti_ice_pellets_light,
    71021 to R.drawable.ti_ice_pellets_light,

    7000 to R.drawable.ti_ice_pellets,
    70000 to R.drawable.ti_ice_pellets,
    70001 to R.drawable.ti_ice_pellets,

    7101 to R.drawable.ti_ice_pellets_heavy,
    71010 to R.drawable.ti_ice_pellets_heavy,
    71011 to R.drawable.ti_ice_pellets_heavy,

    // 7110 mixed conditions
    // 7111 mixed conditions
    // 7112 mixed conditions
    // 7108 mixed conditions
    // 7107 mixed conditions
    // 7109 mixed conditions
    // 7113 mixed conditions
    // 7114 mixed conditions
    // 7116 mixed conditions
    // 7105 mixed conditions
    // 7115 mixed conditions
    // 7117 mixed conditions
    // 7106 mixed conditions
    // 7113 mixed conditions

    8000 to R.drawable.ti_tstorm,
    80000 to R.drawable.ti_tstorm,
    80001 to R.drawable.ti_tstorm,

    // 8001 mixed conditions
    // 8003 mixed conditions
    // 8002 mixed conditions

)