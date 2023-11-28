package com.amora.pokeku.ui.posters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDefaults
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.amora.pokeku.persistence.entity.PokemonCatchEntity
import com.amora.pokeku.ui.main.MainViewModel
import com.amora.pokeku.ui.utils.NetworkImage

@Composable
fun HomeInventory(
	modifier: Modifier = Modifier,
	viewModel: MainViewModel
) {
	val pokemonInventory by viewModel.pokemonInventory.collectAsState(initial = emptyList())
	val listState = rememberLazyListState()
	Column(
		modifier =
		modifier
			.fillMaxHeight()
			.background(SnackbarDefaults.backgroundColor)
	) {
		LazyColumn(
			state = listState,
			contentPadding = PaddingValues(4.dp)
		) {
			val size = pokemonInventory.size
			items(size) { index ->
				val posterItem = pokemonInventory[index]
				PokemonInven(
					poster = posterItem,
				)
			}
		}
	}
}

@Composable
fun PokemonInven(
	modifier: Modifier = Modifier,
	poster: PokemonCatchEntity
) {
	Surface(
		modifier = modifier
			.fillMaxSize()
			.padding(4.dp)
			.clickable {

			},
		color = MaterialTheme.colors.background,
		elevation = 8.dp,
		shape = RoundedCornerShape(8.dp)
	) {
		ConstraintLayout(modifier = Modifier
			.padding(8.dp)
			.fillMaxWidth()
			.fillMaxHeight()) {
			val (image, title) = createRefs()

			NetworkImage(
				modifier = Modifier
					.constrainAs(image) {
						centerHorizontallyTo(parent)
						end.linkTo(title.start)
					}
					.height(64.dp)
					.aspectRatio(1f)
					.clip(RoundedCornerShape(4.dp)),
				url = poster.sprites
			)

			Text(
				modifier = modifier
					.constrainAs(title) {
						start.linkTo(image.end)
					}
					.padding(horizontal = 12.dp),
				text = poster.renamed ?: "Unknown",
				textAlign = TextAlign.Start,
				style = androidx.compose.material3.MaterialTheme.typography.titleLarge
			)
		}
	}
}