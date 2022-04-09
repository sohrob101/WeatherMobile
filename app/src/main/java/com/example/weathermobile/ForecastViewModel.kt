package com.example.weathermobile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ForecastViewModel @Inject constructor(private val api: Api): ViewModel() {

    private val _forecast = MutableLiveData(State.DEFAULT)
    val forecast: LiveData<State> = _forecast

    fun onViewCreated(coordinates: Coordinates){
        fetchForecast(coordinates)
    }

    private fun fetchForecast(coordinates: Coordinates) = viewModelScope.launch {
        val forecast = api.getForecast(
            coordinates.lat.toString(),
            coordinates.lon.toString()
        )
        _forecast.value = _forecast.value?.copy(forecast = forecast)
    }


    data class State(
        val forecast: Forecast?
    ) {
        companion object {
            val DEFAULT = State(null)
        }
    }
}