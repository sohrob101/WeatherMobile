package com.example.weathermobile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherCondition(
    val main: String,
    val icon: String,
) : Parcelable
