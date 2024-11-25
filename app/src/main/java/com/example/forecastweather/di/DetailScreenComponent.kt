package com.example.forecastweather.di

import com.example.forecastweather.domain.entity.City
import com.example.forecastweather.presenttion.ViewModelFactory
import dagger.BindsInstance
import dagger.Subcomponent


@Subcomponent(
    modules = [
        DetailViewModelModule::class
    ]

)
interface DetailScreenComponent {

    fun getViewModelFactory(): ViewModelFactory

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance city: City
        ): DetailScreenComponent
    }
}