package com.example.weathermobile

data class CurrentConditions(
    val weather: List<WeatherCondition>,
    val main: Currents,
    val name: String,
)

