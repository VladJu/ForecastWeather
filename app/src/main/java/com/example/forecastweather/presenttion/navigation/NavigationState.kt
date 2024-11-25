package com.example.forecastweather.presenttion.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.forecastweather.domain.entity.City

class NavigationState(
    val navHostController: NavHostController
) {

    fun navigateTo(routeTitle: String) {
        navHostController.navigate(routeTitle) {
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }
            restoreState = true
        }
    }


    fun navigateToDetails(city: City) {
        navHostController.navigate(Screen.Detail.getRouteWithArgs(city))
    }
}


@Composable
fun rememberNavigationState(
    navHostController: NavHostController = rememberNavController()
): NavigationState {
    return remember {
        NavigationState(navHostController)
    }
}