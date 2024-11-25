package com.example.forecastweather.presenttion.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.forecastweather.R
import com.example.forecastweather.domain.entity.City
import com.example.forecastweather.getApplicationComponent


@Composable
fun SearchScreen(
    onBackPressed: () -> Unit,
    onSavedFavouriteCity: () -> Unit
) {

    val component = getApplicationComponent()
    val viewModel: SearchViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState = viewModel.searchScreenState.collectAsState()

    SearchScreenContent(
        screenState = screenState,
        onBackPressed = onBackPressed,
        onSavedFavouriteCity = onSavedFavouriteCity,
        viewModel = viewModel
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreenContent(
    screenState: State<SearchScreenState>,
    viewModel: SearchViewModel,
    onBackPressed: () -> Unit,
    onSavedFavouriteCity: () -> Unit
) {
    SearchBar(
        placeholder = { stringResource(id = R.string.search) },
        query = viewModel.searchFieldValue,
        onQueryChange = { viewModel.updateSearchField(it) },
        onSearch = { viewModel.searchCityClick() },
        active = true,
        onActiveChange = {},
        leadingIcon = {
            IconButton(onClick = { onBackPressed() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        },
        trailingIcon = {
            IconButton(onClick = { viewModel.searchCityClick() }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            }
        }
    ) {

        when (val searchState = screenState.value) {


            SearchScreenState.Error -> {

            }

            SearchScreenState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            is SearchScreenState.Success -> {
                LazyColumn {
                    items(
                        items = searchState.cities,
                        key = { it.id }
                    ) {
                        CityCard(
                            city = it,
                            onCityClick = {
                                viewModel.addToFavourite(
                                    it,
                                    onSavedCity = { onSavedFavouriteCity() }
                                )
                            }
                        )
                    }
                }
            }

            SearchScreenState.Initial -> {

            }
        }

    }
}


@Composable
private fun CityCard(
    city: City,
    onCityClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 8.dp
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 8.dp
                )
                .clickable { onCityClick() }
        ) {
            Text(
                text = city.name,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = city.country)
        }
    }

}