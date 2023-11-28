package com.amora.pokeku.ui.posters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.LocalContentColor
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.amora.pokeku.R
import com.amora.pokeku.constant.HomeTab
import com.amora.pokeku.persistence.entity.PokemonEntity
import com.amora.pokeku.ui.main.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Posters(
	viewModel: MainViewModel,
	selectedPoster: (Int?, String?) -> Unit
) {
	val posters: LazyPagingItems<PokemonEntity> = viewModel.pokemonList.collectAsLazyPagingItems()
	val selectedTab = HomeTab.getTabFromSource(viewModel.selectedTab.value)
	val tabs = HomeTab.values()
	val scrollState = rememberScrollState()

	val bottomNavOffset by animateDpAsState(
		targetValue = if (scrollState.value > 0) (-56f).dp else 0.dp,
		animationSpec = tween(300), label = ""
	)

	val bottomNavAlpha by animateFloatAsState(
		targetValue = if (scrollState.value > 0) 0f else 1f,
		animationSpec = tween(300), label = ""
	)

	val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

	ConstraintLayout {
		val (body) = createRefs()
		Scaffold(
			modifier = Modifier.constrainAs(body) {
				top.linkTo(parent.top)
			},
			topBar = { PokemonAppBar() },
			bottomBar = {
				AnimatedVisibility(
					visible = bottomBarState.value,
					enter = slideInVertically(initialOffsetY = { it }),
					exit = slideOutVertically(targetOffsetY = { it })
				) {
					BottomNavigation(
						modifier = Modifier
							.navigationBarsPadding()
							.offset(y = bottomNavOffset)
							.alpha(bottomNavAlpha),
						elevation = 8.dp,
						backgroundColor = MaterialTheme.colorScheme.surfaceTint
					) {
						tabs.forEach { tab ->
							BottomNavigationItem(
								selected = tab == selectedTab,
								onClick = { viewModel.selectTab(tab.title) },
								icon = {
									Icon(
										imageVector = tab.icon,
										contentDescription = null,
										tint = MaterialTheme.colorScheme.background
									)
								},
								selectedContentColor = LocalContentColor.current,
								unselectedContentColor = LocalContentColor.current
							)
						}
					}
				}
			}
		) { innerPadding ->
			val modifier = Modifier.padding(innerPadding)
			Crossfade(targetState = selectedTab, label = "") { destination ->
				when (destination) {
					HomeTab.HOME -> HomePokemon(modifier, posters, selectedPoster, viewModel)
					HomeTab.INVENTORY -> HomeInventory(modifier, viewModel)
				}
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PokemonAppBar() {
	TopAppBar(
		title = {
			Text(
				modifier = Modifier.padding(8.dp),
				text = stringResource(id = R.string.app_name),
				color = MaterialTheme.colorScheme.background,
				fontSize = 18.sp,
				fontWeight = FontWeight.Bold
			)
		},
		colors = TopAppBarDefaults.smallTopAppBarColors(
			containerColor = MaterialTheme.colorScheme.surfaceTint
		)
	)
}