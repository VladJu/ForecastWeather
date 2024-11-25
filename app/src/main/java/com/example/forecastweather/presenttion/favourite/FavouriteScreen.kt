package com.example.forecastweather.presenttion.favourite

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.forecastweather.R
import com.example.forecastweather.domain.entity.City
import com.example.forecastweather.getApplicationComponent
import com.example.forecastweather.presenttion.extensions.tempToFormattedString
import com.example.forecastweather.presenttion.ui.theme.CardGradients
import com.example.forecastweather.presenttion.ui.theme.Gradient
import com.example.forecastweather.presenttion.ui.theme.Orange

@Composable
fun FavouriteScreen(
    onCityClick: (City) -> Unit,
    onSearchClick: () -> Unit,
    onAddToFavouriteClick: () -> Unit
) {

    Log.d("FavouriteScreen1", "FavouriteScreen1RECOMPOSITION")

    val component = getApplicationComponent()
    val viewModel: FavouriteViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState =
        viewModel.favouriteScreenState.collectAsState(initial = FavouriteScreenState.Initial)


    FavouriteScreenContent(
        screenState = screenState,
        onCityClick = onCityClick,
        onSearchClick = onSearchClick,
        onAddToFavouriteClick = onAddToFavouriteClick
    )
}

@Composable
private fun FavouriteScreenContent(
    screenState: State<FavouriteScreenState>,
    onCityClick: (City) -> Unit,
    onSearchClick: () -> Unit,
    onAddToFavouriteClick: () -> Unit
) {

    Log.d("FavouriteScreen2", "FavouriteScreen2RECOMPOSITION")
    when (val currentState = screenState.value) {
        FavouriteScreenState.Error -> {}
        FavouriteScreenState.Initial -> {}

        is FavouriteScreenState.Loaded -> {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item(span = { GridItemSpan(2) }) {
                    SearchCard(onClick = { onSearchClick() })
                }
                itemsIndexed(
                    items = currentState.cityItems,
                    key = { _, item -> item.id }
                ) { index, city ->
                    CityCard(
                        city = city,
                        index = index,
                        onCityClick = {
                            onCityClick(city)
                        }
                    )
                }

                item {
                    AddToFavouriteCityCard(
                        onClick = { onAddToFavouriteClick() }
                    )
                }

            }
        }

        FavouriteScreenState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CityCard(
    city: City,
    index: Int,
    onCityClick: () -> Unit
) {

    val gradient = getGradientById(index)
    Card(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onCityClick() }
            .shadow(
                elevation = 14.dp,
                shape = MaterialTheme.shapes.extraLarge,
                spotColor = gradient.shadowColor
            ),
    ) {
        Box(
            modifier = Modifier
                .background(gradient.primaryGradient)
                .fillMaxSize()
                .sizeIn(minHeight = 196.dp)
                .drawBehind {
                    drawCircle(
                        brush = gradient.secondaryGradient,
                        center = Offset(
                            x = center.x - size.width / 10,
                            y = center.y + size.height / 2
                        )
                    )
                }
                .padding(24.dp)
        ) {

            GlideImage(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(50.dp),
                model = city.conditionUrl,
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 26.dp),
                text = city.tempC.tempToFormattedString(),
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 48.sp)
            )

            Text(
                modifier = Modifier.align(Alignment.BottomStart),
                text = city.name,
                color = MaterialTheme.colorScheme.background
            )
        }
    }

}

@Composable
fun SearchCard(
    onClick: () -> Unit
) {

    val gradient = CardGradients.gradients[3]
    Card(
        shape = CircleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradient.primaryGradient)
                .clickable { onClick() }
        ) {
            Icon(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.background,
                contentDescription = null
            )
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(id = R.string.search),
                color = MaterialTheme.colorScheme.background
            )

        }

    }
}

@Composable
private fun AddToFavouriteCityCard(
    onClick: () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(width = 1.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        Column(
            modifier = Modifier
                .sizeIn(minHeight = 196.dp)
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(24.dp)
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
                    .size(48.dp),
                imageVector = Icons.Default.Edit,
                tint = Orange,
                contentDescription = null
            )
            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.button_add_favourite),
                style = MaterialTheme.typography.titleMedium
            )

        }

    }

}


private fun getGradientById(index: Int): Gradient {
    val gradients = CardGradients.gradients
    return gradients[index % gradients.size]
}
