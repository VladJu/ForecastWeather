package com.example.forecastweather.presenttion.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forecastweather.domain.entity.City
import com.example.forecastweather.domain.usecase.AddToFavouriteUseCase
import com.example.forecastweather.domain.usecase.GetForecastUseCase
import com.example.forecastweather.domain.usecase.ObserveIsFavouriteStatusUseCase
import com.example.forecastweather.domain.usecase.RemoveFromFavouriteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val city: City,
    private val observeIsFavouriteStatusUseCase: ObserveIsFavouriteStatusUseCase,
    private val addToFavouriteUseCase: AddToFavouriteUseCase,
    private val removeFromFavouriteUseCase: RemoveFromFavouriteUseCase,
    private val getForecastUseCase: GetForecastUseCase
) : ViewModel() {

    private val _forecastState = MutableStateFlow<DetailScreenState>(DetailScreenState.Loading)
    val forecastState = _forecastState.asStateFlow()

    var observeIsFavouriteStatus by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            cityIsFavourite(city)
            getForecast(city)
        }

    }


    private suspend fun getForecast(city: City) {
        try {
            val forecast = getForecastUseCase(city.id)
            _forecastState.value = DetailScreenState.Loaded(forecast)
        } catch (e: Exception) {
            _forecastState.value = DetailScreenState.Error
        }

    }

    private suspend fun cityIsFavourite(city: City) {
        viewModelScope.launch(Dispatchers.IO) {
            observeIsFavouriteStatusUseCase(city.id).collect {
                withContext(Dispatchers.Main.immediate){
                    observeIsFavouriteStatus = it
                }
            }
        }
    }


    fun changeIsFavouriteStatus(city: City) {
        viewModelScope.launch(Dispatchers.IO) {
            if (observeIsFavouriteStatus) {
                removeFromFavouriteUseCase(city.id)
            } else {
                addToFavouriteUseCase(city)
            }
        }
    }


//    suspend fun removeCityFromFavourite(city: City) {
//        viewModelScope.launch(Dispatchers.IO) {
//            removeFromFavouriteUseCase(city.id)
//        }
//    }
//
//    suspend fun addCityToFavourite(city: City) {
//        viewModelScope.launch(Dispatchers.IO) {
//            addToFavouriteUseCase(city)
//        }
//    }

}