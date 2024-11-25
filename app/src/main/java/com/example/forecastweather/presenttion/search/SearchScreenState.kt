package com.example.forecastweather.presenttion.search

import com.example.forecastweather.domain.entity.City

sealed interface SearchScreenState {

    data object Initial : SearchScreenState

    data object Loading : SearchScreenState

    data object Error : SearchScreenState

    data class Success(val cities: List<City>) : SearchScreenState
}
