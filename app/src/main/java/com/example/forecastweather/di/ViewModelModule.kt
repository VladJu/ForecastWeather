package com.example.forecastweather.di

import androidx.lifecycle.ViewModel
import com.example.forecastweather.presenttion.favourite.FavouriteViewModel
import com.example.forecastweather.presenttion.search.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(FavouriteViewModel::class)
    @Binds
    fun bindFavouriteViewModel(impl : FavouriteViewModel) : ViewModel

    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    @Binds
    fun bindSearchViewModel(impl : SearchViewModel) : ViewModel
}