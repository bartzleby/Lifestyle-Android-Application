package com.lifestyle

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WeatherRVAdapter(weatherData: ArrayList<TomorrowData>, weatherCodes: WeatherCodes?) :
    RecyclerView.Adapter<WeatherRVAdapter.ViewHolder>() {
    private var ctx: Context? = null
    private var weatherData = weatherData
    private val weatherCodes = weatherCodes

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvWindSpeed: TextView = itemView.findViewById(R.id.tv_forecast_wind_sp)
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
        var data: TomorrowData = weatherData[position]

        val weatherCode = data.values.weatherCode
        var weatherDescription = weatherCodes?.weatherCode?.get(weatherCode)?.toString()

        if (weatherDescription != null) {
            weatherDescription = weatherDescription.substring(
                1,
                weatherDescription.length - 1
            ) // hacky to remove bounding quotes

            println(weatherDescription)
            weather_code_to_icon_map[weatherCode.toInt()]?.let {
                holder.ivCondition.setImageResource(it)
            }
        }

        holder.tvTime.text = data.time
        holder.tvTemperature.text = data.values.temperature.toString()

    }

    override fun getItemCount(): Int {
        return weatherData.size
    }
}