package com.example.forecastweather.presenttion.navigation

import android.net.Uri
import com.example.forecastweather.domain.entity.City
import com.google.gson.Gson

sealed class Screen(
    val routeTitle: String
) {
    data object Favorite : Screen(ROUTE_FAVOURITE)
    data object Search : Screen(ROUTE_SEARCH)


    data object Detail : Screen(ROUTE_DETAIL) {

        private const val ROUTE_FOR_ARGS = "detail"
        fun getRouteWithArgs(city: City): String {
            val feedPostJson =  Gson().toJson(city)
            return "$ROUTE_FOR_ARGS/${feedPostJson.encode()}"
        }

    }

    companion object {

        const val KEY_CITY_ITEM = "city_item"

        const val ROUTE_FAVOURITE = "favourite"
        const val ROUTE_SEARCH = "search"
        const val ROUTE_DETAIL = "detail/{$KEY_CITY_ITEM}"
    }


}

fun String.encode(): String {
    return Uri.encode(this)
}

