package com.example.forecastweather.presenttion.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.example.forecastweather.presenttion.detail.DetailsScreen
import com.example.forecastweather.presenttion.favourite.FavouriteScreen
import com.example.forecastweather.presenttion.navigation.AppNavGraph
import com.example.forecastweather.presenttion.navigation.Screen
import com.example.forecastweather.presenttion.navigation.rememberNavigationState
import com.example.forecastweather.presenttion.search.SearchScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainScreen() {

    val navigationState = rememberNavigationState()

    AppNavGraph(
        navHostController = navigationState.navHostController,
        favouriteScreenContent = {
            FavouriteScreen(
                onAddToFavouriteClick = {
                    navigationState.navigateTo(Screen.ROUTE_SEARCH)
                },
                onSearchClick = {
                    navigationState.navigateTo(Screen.ROUTE_SEARCH)
                },
                onCityClick = {
                    navigationState.navigateToDetails(it)
                }
            )
        },
        detailsScreenContent = {
            DetailsScreen(
                city = it,
                onBackPressed = {
                    navigationState.navHostController.popBackStack()
                }
            )
        },
        searchScreenContent = {
            SearchScreen(
                onBackPressed = {
                    navigationState.navHostController.popBackStack()
                },
                onSavedFavouriteCity = {
                    navigationState.navigateTo(Screen.ROUTE_FAVOURITE)
                }
            )
        }
    )
}