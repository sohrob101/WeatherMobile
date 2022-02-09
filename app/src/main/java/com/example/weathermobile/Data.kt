package com.example.weathermobile

data class Data(val date: Long)



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