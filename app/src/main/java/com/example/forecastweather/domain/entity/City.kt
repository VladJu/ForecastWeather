package com.example.forecastweather.domain.entity

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    val id : Int,
    val name : String,
    val country:String,
    val tempC : Float = 0.0f,
    val conditionUrl : String ="",
    val conditionText : String=""
) : Parcelable {

    companion object {
        val NavigationType: NavType<City> = object : NavType<City>(false) {
            override fun get(bundle: Bundle, key: String): City? {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bundle.getParcelable(key, City::class.java)
                } else {
                    bundle.getParcelable(key)
                }
            }

            override fun parseValue(value: String): City {
                return Gson().fromJson(value, City::class.java)
            }

            override fun put(bundle: Bundle, key: String, value: City) {
                bundle.putParcelable(key, value)
            }
        }
    }
}
