package com.amora.pokeku.ui

sealed class NavScreen(val route: String) {

	object Home: NavScreen("Home")

	object PokemonDetails: NavScreen("PokemonDetails") {
		const val routeWithArgument: String = "PokemonDetails/{pokeId}/{pokeName}"
		const val argument0: String = "pokeName"
		const val argument1: String = "pokeId"
	}
}