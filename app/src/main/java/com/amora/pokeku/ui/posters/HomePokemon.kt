package com.amora.pokeku.ui.posters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.amora.pokeku.persistence.entity.PokemonEntity
import com.amora.pokeku.persistence.entity.PokemonEntity.Companion.toPokemonPoster
import com.amora.pokeku.repository.model.PokemonPoster
import com.amora.pokeku.ui.main.MainViewModel
import com.amora.pokeku.ui.utils.NetworkImage

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomePokemon(
	modifier: Modifier = Modifier,
	selectedPoster: (Int?, String?) -> Unit,
	viewModel: MainViewModel
) {
	val state by viewModel.isLoading
	val poster: LazyPagingItems<PokemonEntity> = viewModel.pokemonList.collectAsLazyPagingItems()
	val pullRefreshState =
		rememberPullRefreshState(refreshing = state, onRefresh = { poster.refresh() })
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(backgroundColor)
	) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.pullRefresh(pullRefreshState)
		) {
			when (poster.loadState.refresh) {
				is LoadState.Loading -> {
					viewModel.triggerLoading(true)
					Box(
						modifier = modifier
							.fillMaxSize()
							.background(backgroundColor),
						contentAlignment = Alignment.Center
					) { }
				}
				is LoadState.Error -> {
					Box(
						modifier = modifier
							.fillMaxSize()
							.background(backgroundColor),
						contentAlignment = Alignment.Center
					) {
						Text(text = "Fail to Load", color = Color.White)

					}
				}

				is LoadState.NotLoading -> {
					viewModel.triggerLoading(false)
					LazyVerticalStaggeredGrid(
						modifier = modifier
							.background(backgroundColor),
						columns = StaggeredGridCells.Fixed(2)
					) {
						val size = poster.itemCount
						items(size) { index ->
							val posterItem = poster[index]?.toPokemonPoster()
							HomePoster(
								poster = posterItem,
								selectedPoster = selectedPoster
							)
						}
					}
				}
			}
			PullRefreshIndicator(
				modifier = modifier.align(Alignment.TopCenter),
				refreshing = state,
				state = pullRefreshState
			)
		}
	}
}

@Composable
private fun HomePoster(
	modifier: Modifier = Modifier,
	poster: PokemonPoster?,
	selectedPoster: (Int?, String?) -> Unit
) {
	Surface(
		modifier = modifier
			.padding(8.dp)
			.clickable(onClick = { selectedPoster(poster?.getPokeId(), poster?.name) }),
		color = MaterialTheme.colorScheme.primaryContainer,
		shadowElevation = 8.dp,
		shape = RoundedCornerShape(8.dp)
	) {
		ConstraintLayout {
			val (image, title) = createRefs()
			NetworkImage(
				url = poster?.getImageUrl(),
				modifier = Modifier
					.aspectRatio(0.9f)
					.constrainAs(image) {
						centerHorizontallyTo(parent)
						top.linkTo(parent.top)
					}
			)
			Text(modifier = Modifier
				.constrainAs(title) {
					centerHorizontallyTo(parent)
					top.linkTo(image.bottom)
				}
				.padding(8.dp),
				text = poster?.name ?: "Unknown",
				style = MaterialTheme.typography.titleMedium)
		}
	}
}