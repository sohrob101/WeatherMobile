package com.example.weathermobile


import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("weather")
    suspend fun getCurrentConditions(
        @Query("zip") zip: String,
        @Query("units") units: String = "Imperial",
        @Query("appId") appId: String = "b55cf5a5fd3ecf277058fb06bb0ce80b",
    ) : CurrentConditions

    @GET("forecast/daily")
    suspend fun getForecast(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("cnt") count: Int = 16,
        @Query("units") unit: String = "Imperial",
        @Query("appId") appId: String = "b55cf5a5fd3ecf277058fb06bb0ce80b",
    ) : Forecast
}