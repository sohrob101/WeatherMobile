package com.example.weathermobile

import android.annotation.SuppressLint
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.w3c.dom.Text
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class MyAdapter(private val data: List<DayForecast>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    @SuppressLint("NewApi")
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val stringHolding = "Temp: %.0f°F \n High: %.0f°F   Low: %.0f°F"
        private val sunFormat = DateTimeFormatter.ofPattern("h:mma")
        private val formatter = DateTimeFormatter.ofPattern("MMM dd")
        private val dateView: TextView = view.findViewById(R.id.date)
        private val tempView: TextView = view.findViewById(R.id.temp)
        private val sunsetView: TextView = view.findViewById(R.id.sunset)
        private val sunriseView: TextView = view.findViewById(R.id.sunrise)
        private val iconView: ImageView = view.findViewById(R.id.imageView5)
        private val recyclerView: RecyclerView = view.findViewById(R.id.recyclerV)


        @SuppressLint("SetTextI18n")
        fun bind(data: DayForecast){
            val instant = Instant.ofEpochSecond(data.date)
            val sunriseInstant = Instant.ofEpochSecond(data.sunrise)
            val sunsetInstant = Instant.ofEpochSecond(data.sunset)
            val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            val sunrise = LocalDateTime.ofInstant(sunriseInstant, ZoneId.systemDefault())
            val sunset = LocalDateTime.ofInstant(sunsetInstant, ZoneId.systemDefault())
            val tempFormat = stringHolding.format(data.temp.day, data.temp.max, data.temp.min)

            val iconName = data.weather.firstOrNull()?.icon
            val iconUrl = "https://openweathermap.org/img/wn/${iconName}@2x.png"
            Glide.with(recyclerView)
                .load(iconUrl)
                .into(iconView)




            dateView.text = formatter.format(dateTime)
            tempView.text = tempFormat
            sunsetView.text = "Sunset: " + sunFormat.format(sunset)
            sunriseView.text = "Sunrise: " + sunFormat.format(sunrise)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_forecast, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])

    }

    override fun getItemCount() = data.size

}