package com.example.forecastweather.presenttion.favourite

import com.example.forecastweather.domain.entity.City

sealed class FavouriteScreenState {

    data object Initial : FavouriteScreenState()

    data object Loading : FavouriteScreenState()

    data object Error : FavouriteScreenState()

    data class Loaded(
        val cityItems: List<City>
    ) : FavouriteScreenState()

//    sealed interface WeatherScreenState {
//
//        data object Initial : WeatherScreenState
//
//        data class Loading(val cityId: Int) : WeatherScreenState
//
//        data class Error(val cityId: Int) : WeatherScreenState
//
//        data class Loaded(
//            val cityId: Int,
//            val tempC:Float,
//            val iconUrl : String
//        ) : WeatherScreenState
//    }

}