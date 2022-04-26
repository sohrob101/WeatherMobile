package com.example.weathermobile


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val api: Api
) : ViewModel() {

    private var searchText = ""
    private val _events = MutableLiveData<Event>()
    val events: LiveData<Event> = _events

    private val _state = MutableLiveData(State.DEFAULT)
    val state: LiveData<State> = _state

    fun onViewCreated() {
        _events.value = Event.ViewCreated
        _state.value = State.DEFAULT
    }

    fun searchButtonClicked() = viewModelScope.launch {
        try {
            val response = api.getCurrentConditions(searchText)
            _events.value = Event.NavigateToCurrentConditions(response)
        } catch (ex: Exception) {
            _events.value = Event.SearchError
        }
    }

    fun locationButtonClicked(lat: Double, lon: Double) = viewModelScope.launch {
        try{
            val response = api.getCurrentConditionsFromCoordinates(lat.toString(), lon.toString())
            _events.value = Event.NavigateToCurrentConditions(response)
        } catch (ex: Exception) {
            _events.value = Event.SearchError
        }
    }



    fun updateSearchText(searchText: String) {
        this.searchText = searchText
        _state.value = _state.value?.copy(
            searchButtonEnabled = this.searchText.isZipCodeValidated()
        )
    }

    sealed class Event {
        data class NavigateToCurrentConditions(val currentConditions: CurrentConditions) : Event()
        object SearchError : Event()
        object ViewCreated : Event()
    }

    data class State(
        val searchButtonEnabled: Boolean
    ) {
        companion object {
            val DEFAULT: State = State(
                searchButtonEnabled = false
            )
        }
    }
}
