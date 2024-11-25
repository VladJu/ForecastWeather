package com.example.forecastweather.presenttion.detail

import com.example.forecastweather.domain.entity.Forecast

sealed interface DetailScreenState {

    data object Initial : DetailScreenState

    data object Loading : DetailScreenState

    data object Error : DetailScreenState

    data class Loaded(val forecast: Forecast) : DetailScreenState

}