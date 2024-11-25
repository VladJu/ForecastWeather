package com.example.forecastweather.presenttion.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideIn
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.forecastweather.R
import com.example.forecastweather.domain.entity.City
import com.example.forecastweather.domain.entity.Forecast
import com.example.forecastweather.domain.entity.Weather
import com.example.forecastweather.getApplicationComponent
import com.example.forecastweather.presenttion.extensions.formattedFullDate
import com.example.forecastweather.presenttion.extensions.tempToFormattedString
import com.example.forecastweather.presenttion.ui.theme.CardGradients
import com.example.forecastweather.presenttion.ui.theme.Yellow

@Composable
fun DetailsScreen(
    city: City,
    onBackPressed: () -> Unit
) {
    val component = getApplicationComponent()
        .getDetailScreenComponentFactory()
        .create(city)

    val viewModel: DetailsViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState = viewModel.forecastState.collectAsState(DetailScreenState.Initial)

    DetailsScreenContent(
        city = city,
        screenState = screenState,
        viewModel = viewModel,
        onBackPressed = onBackPressed
    )

}

@Composable
private fun DetailsScreenContent(
    city: City,
    screenState: State<DetailScreenState>,
    viewModel: DetailsViewModel,
    onBackPressed: () -> Unit,

    ) {
    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBar(
                cityName = city.name,
                isCityFavourite = viewModel.observeIsFavouriteStatus,
                onBackClick = { onBackPressed() },
                onClickChangeFavouriteStatus = {
                    viewModel.changeIsFavouriteStatus(city)
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(CardGradients.gradients[1].primaryGradient)
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {

            when (val forecastState = screenState.value) {

                DetailScreenState.Error -> {
                    Error()
                }

                DetailScreenState.Initial -> {
                    Initial()
                }

                is DetailScreenState.Loaded -> {
                    Forecast(forecast = forecastState.forecast)
                }

                DetailScreenState.Loading -> {
                    Loading()
                }
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    cityName: String,
    isCityFavourite: Boolean,
    onBackClick: () -> Unit,
    onClickChangeFavouriteStatus: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(text = cityName) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.background
        ),
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    tint = MaterialTheme.colorScheme.background,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = { onClickChangeFavouriteStatus() }) {
                val color = if (isCityFavourite) {
                    Yellow
                } else {
                    MaterialTheme.colorScheme.background
                }
                val icon = if (isCityFavourite) {
                    Icons.Default.Star
                } else {
                    Icons.Default.StarBorder

                }
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color
                )
            }

        }
    )

}


@Composable
private fun Forecast(forecast: Forecast) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        AnimatedCurrentWeather(weather = forecast.currentWeather)
        Spacer(modifier = Modifier.weight(1f))
        AnimatedUpcomingWeather(upcoming = forecast.upcomingForecast)
        Spacer(modifier = Modifier.weight(0.5f))
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun CurrentWeather(weather: Weather) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = weather.conditionText,
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = weather.tempC.tempToFormattedString(),
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 70.sp)
            )
            GlideImage(
                modifier = Modifier.size(70.dp),
                model = weather.conditionUrl,
                contentDescription = null
            )
        }
        Text(
            text = weather.date.formattedFullDate(),
            style = MaterialTheme.typography.titleLarge
        )
    }

}

@Composable
private fun AnimatedCurrentWeather(weather: Weather) {
    val state = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    AnimatedVisibility(
        visibleState = state,
        enter = fadeIn(animationSpec = tween(2000)) + slideIn(
            animationSpec = tween(1000),
            initialOffset = { IntOffset(0, -it.height) }
        )
    ) {
        CurrentWeather(weather = weather)

    }
}

@Composable
private fun AnimatedUpcomingWeather(upcoming: List<Weather>) {

    val state = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    AnimatedVisibility(
        visibleState = state,
        enter = fadeIn(animationSpec = tween(2000)) + slideIn(
            animationSpec = tween(3500),
            initialOffset = { IntOffset(0, it.height) }
        )
    ) {
        UpcomingWeather(upcoming = upcoming)
    }

}

@Composable
private fun UpcomingWeather(upcoming: List<Weather>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(26.dp),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background.copy(
                alpha = 0.25f
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(26.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 26.dp)
                    .align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.upcoming),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 26.sp),
                color = MaterialTheme.colorScheme.background
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                upcoming.forEach {
                    SmallWeatherCard(weather = it)
                }

            }
        }
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun SmallWeatherCard(weather: Weather) {
    Card(
        modifier = Modifier
            .height(128.dp)
            .width(128.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = weather.tempC.tempToFormattedString())
            GlideImage(
                modifier = Modifier.size(48.dp),
                model = weather.conditionUrl,
                contentDescription = null
            )
            Text(text = weather.conditionText)
        }
    }
}

@Composable
private fun Error() {

}

@Composable
private fun Initial() {

}

@Composable
private fun Loading() {

}


