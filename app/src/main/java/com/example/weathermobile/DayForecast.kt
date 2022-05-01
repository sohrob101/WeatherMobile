package com.example.weathermobile

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class DayForecast(
    val weather: List<WeatherIcon>,
    @Json(name = "dt") val date: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: ForecastTemp,
    val pressure: Float,
    val humidity: Int,
    val speed : Float
    ) : Parcelable

@Parcelize
data class WeatherIcon(
    val icon: String,
    @Json(name = "main") val description : String
) : Parcelable


@Parcelize
data class ForecastTemp(
    val day: Float,
    val min: Float,
    val max: Float
) : Parcelable

