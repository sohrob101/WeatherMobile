package com.example.weathermobile

import android.widget.ImageView
import com.squareup.moshi.Json

data class DayForecast(
    val weather: List<WeatherIcon>,
    @Json(name = "dt") val date: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: ForecastTemp,
    val pressure: Float,
    val humidity: Int
    )

data class WeatherIcon(
    val icon: String
)



data class ForecastTemp(
    val day: Float,
    val min: Float,
    val max: Float
)

//val sunrise: Long,
//val sunset: Long,
//val temp: ForecastTemp,
//val pressure: Float,
//val humidity: Int