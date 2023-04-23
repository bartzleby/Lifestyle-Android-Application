package com.lifestyle

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.round

class WeatherRVAdapter(weatherData: ArrayList<WeatherDataForecast>, weatherCodes: WeatherCodes?) :
    RecyclerView.Adapter<WeatherRVAdapter.ViewHolder>() {
    private var ctx: Context? = null
    private var weatherData = weatherData
    private val weatherCodes = weatherCodes

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTemperature: TextView = itemView.findViewById(R.id.tv_forecast_temperature)
        var tvTime: TextView = itemView.findViewById(R.id.tv_forecast_time)
        var ivCondition: ImageView = itemView.findViewById(R.id.iv_forecast_condition)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        ctx = parent.context
        val layoutInflater = LayoutInflater.from(ctx)
        val v = layoutInflater.inflate(R.layout.weather_rv_item, parent, false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data: WeatherDataForecast = weatherData[position]

        val weatherCode = data.weatherCode
        var weatherDescription = weatherCodes?.weatherCode?.get(weatherCode)?.toString()

        if (weatherDescription != null) {
            weatherDescription = weatherDescription.substring(
                1,
                weatherDescription.length - 1
            ) // hacky to remove bounding quotes

//            println(weatherDescription)
            weather_code_to_icon_map[weatherCode.toInt()]?.let {
                holder.ivCondition.setImageResource(it)
            }
        }

        var inputTimeFmt: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'")
        inputTimeFmt.timeZone = TimeZone.getTimeZone("GMT")
        var outputTimeFmt: SimpleDateFormat = SimpleDateFormat("hh:mm aa")
        try {
            var t: Date = inputTimeFmt.parse(data.time)
            holder.tvTime.text = outputTimeFmt.format(t)
            // TODO: display date or limit scrolling to today's entries
        } catch(e: java.lang.Exception) {
            println(e)
        }

        holder.tvTemperature.text = "${data.temperature?.let { round(it).toString().dropLast(2) }} \u2103"

    }

    override fun getItemCount(): Int {
        return weatherData.size
    }
}