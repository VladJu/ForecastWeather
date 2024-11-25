package com.example.forecastweather.presenttion.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.forecastweather.domain.entity.City

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    favouriteScreenContent: @Composable () -> Unit,
    detailsScreenContent: @Composable (City) -> Unit,
    searchScreenContent: @Composable () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Favorite.routeTitle
    ) {
        composable(Screen.Favorite.routeTitle) {
            favouriteScreenContent()
        }
        composable(
            route = Screen.Detail.routeTitle,
            arguments = listOf(
                navArgument(Screen.KEY_CITY_ITEM) {
                    type = City.NavigationType
                }
            )
        ) {
            val city = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.arguments?.getParcelable(
                    Screen.KEY_CITY_ITEM,
                    City::class.java
                ) ?: throw RuntimeException("Args is null")
            } else {
                it.arguments?.getParcelable(
                    Screen.KEY_CITY_ITEM
                ) ?: throw RuntimeException("Args is null")
            }
            detailsScreenContent(city)
        }
        composable(Screen.Search.routeTitle) {
            searchScreenContent()
        }
    }
}