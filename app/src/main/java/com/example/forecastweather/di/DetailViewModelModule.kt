package com.example.forecastweather.di

import androidx.lifecycle.ViewModel
import com.example.forecastweather.presenttion.detail.DetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface DetailViewModelModule {

    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    @Binds
    fun bindDetailViewModel(impl : DetailsViewModel) :ViewModel
}