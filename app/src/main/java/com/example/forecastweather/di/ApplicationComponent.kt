package com.example.forecastweather.di

import android.content.Context
import com.example.forecastweather.presenttion.ViewModelFactory
import com.example.forecastweather.presenttion.main.MainActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        PresentationModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun getViewModelFactory():ViewModelFactory

    fun getDetailScreenComponentFactory() : DetailScreenComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }
}