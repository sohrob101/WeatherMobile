package com.example.weathermobile

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentConditions(
    val weather: List<WeatherCondition>,
    val main: Currents,
    val name: String,
    @Json(name = "coord") val coordinates: Coordinates
) : Parcelable

