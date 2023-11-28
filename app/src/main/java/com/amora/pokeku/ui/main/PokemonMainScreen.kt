package com.amora.pokeku.ui.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.amora.pokeku.repository.model.PokeMark
import com.amora.pokeku.ui.NavScreen
import com.amora.pokeku.ui.details.PokemonDetails
import com.amora.pokeku.ui.posters.Posters
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun PokemonMainScreen() {
	val navController = rememberNavController()
	val colors = MaterialTheme.colorScheme
	val systemUiController = rememberSystemUiController()

	var statusBarColor by remember { mutableStateOf(colors.surfaceTint) }
	var navigationBarColor by remember { mutableStateOf(colors.surfaceTint) }

	val animatedStatusBarColor by animateColorAsState(
		targetValue = statusBarColor,
		animationSpec = tween(),
		label = "StatusBar"
	)

	val animatedNavigationBarColor by animateColorAsState(
		targetValue = statusBarColor,
		animationSpec = tween(),
		label = "NavigationBar"
	)

	NavHost(navController = navController, startDestination = NavScreen.Home.route) {
		composable(NavScreen.Home.route) {
			Posters(
				viewModel = hiltViewModel(),
				selectedPoster = { idPoke, namePoke ->
					navController.navigate("${NavScreen.PokemonDetails.route}/$idPoke/${namePoke}")
				})
			LaunchedEffect(Unit) {
				statusBarColor = colors.surfaceTint
				navigationBarColor = colors.surfaceTint
			}
		}

		composable(
			route = NavScreen.PokemonDetails.routeWithArgument,
			arguments = listOf(
				navArgument(NavScreen.PokemonDetails.argument0) { type = NavType.StringType },
				navArgument(NavScreen.PokemonDetails.argument1) { type = NavType.IntType })
		) { navBackStackEntry ->
			val pokeName =
				navBackStackEntry.arguments?.getString(NavScreen.PokemonDetails.argument0)
					?: return@composable

			val pokeId = navBackStackEntry.arguments?.getInt(NavScreen.PokemonDetails.argument1)
				?: return@composable

			PokemonDetails(
				poke = PokeMark(id = pokeId, name = pokeName),
				viewModel = hiltViewModel(),
			) {
				navController.navigateUp()
			}
			LaunchedEffect(key1 = Unit) {
				statusBarColor = colors.surfaceTint
				navigationBarColor = colors.surfaceTint
			}
		}
	}

	LaunchedEffect(animatedStatusBarColor, animatedNavigationBarColor) {
		systemUiController.setStatusBarColor(animatedStatusBarColor)
		systemUiController.setNavigationBarColor(animatedNavigationBarColor)
	}
}