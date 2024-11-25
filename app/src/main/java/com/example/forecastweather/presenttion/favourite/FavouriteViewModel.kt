package com.example.forecastweather.presenttion.favourite

import androidx.lifecycle.ViewModel
import com.example.forecastweather.domain.entity.City
import com.example.forecastweather.domain.usecase.GetFavouriteCitiesUseCase
import com.example.forecastweather.domain.usecase.GetWeatherUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavouriteViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getFavouriteCitiesUseCase: GetFavouriteCitiesUseCase,
) : ViewModel() {


    private val _weatherForCity = mutableListOf<City>()
    private val weatherForCity: List<City>
        get() = _weatherForCity.toList()

    private val getFavouriteCities = getFavouriteCitiesUseCase()

    private fun favouriteCitiesList(): Flow<List<City>> {
        return flow {
            getFavouriteCities.collect {
                weatherForCities(it)
                emit(weatherForCity)
            }
        }
    }


    val favouriteScreenState = favouriteCitiesList()
        .map { FavouriteScreenState.Loaded(it) as FavouriteScreenState }
        .onStart { emit(FavouriteScreenState.Loading) }


    private suspend fun weatherForCities(list: List<City>) {
        withContext(Dispatchers.IO) {
            _weatherForCity.clear()
            list.forEach { cityItem ->
                val weather = getWeatherUseCase(cityItem.id)
                val city = City(
                    id = cityItem.id,
                    name = cityItem.name,
                    country = cityItem.country,
                    tempC = weather.tempC,
                    conditionUrl = weather.conditionUrl,
                    conditionText = weather.conditionText
                )
                _weatherForCity.add(city)
            }
        }

    }
    //fun <T> Flow<T>.mergeWith(another: Flow<T>): Flow<T> = merge(this, another)

}