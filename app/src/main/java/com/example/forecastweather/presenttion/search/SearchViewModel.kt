package com.example.forecastweather.presenttion.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forecastweather.domain.entity.City
import com.example.forecastweather.domain.usecase.AddToFavouriteUseCase
import com.example.forecastweather.domain.usecase.SearchCityUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    //private val openReason: OpenReason,
    private val searchCityUseCase: SearchCityUseCase,
    private val addToFavouriteUseCase: AddToFavouriteUseCase
) : ViewModel() {

    private val _searchScreenState = MutableStateFlow<SearchScreenState>(SearchScreenState.Initial)
    val searchScreenState = _searchScreenState.asStateFlow()

    var searchFieldValue by mutableStateOf("")
        private set


    fun searchCityClick() {
        viewModelScope.launch(Dispatchers.IO) {
            _searchScreenState.value = SearchScreenState.Loading
            try {
                if (checkSearchFieldValue()) {
                    searchCity(searchFieldValue)
                } else {
                    _searchScreenState.value = SearchScreenState.Error
                }
            } catch (e: Exception) {
                _searchScreenState.value = SearchScreenState.Error
            }
        }
    }

    fun addToFavourite(city: City, onSavedCity: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            addToFavouriteUseCase(city)
        }
        clearSearchValue()
        onSavedCity()
    }

    private fun clearSearchValue() {
        searchFieldValue = ""
    }

    private suspend fun searchCity(query: String) {
        try {
            val cities = searchCityUseCase(query)
            _searchScreenState.value = SearchScreenState.Success(cities)
        } catch (e: Exception) {
            _searchScreenState.value = SearchScreenState.Error
        }
    }

    private fun checkSearchFieldValue(): Boolean {
        return searchFieldValue.isNotEmpty()
    }

    fun updateSearchField(input: String) {
        searchFieldValue = input
    }

}