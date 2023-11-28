package com.amora.pokeku.network

import com.amora.pokeku.repository.model.PokemonDetails
import com.amora.pokeku.repository.model.PokemonResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

	@GET("pokemon")
	suspend fun getAllPokemon(
		@Query("limit") limit: Int = 20,
		@Query("offset") offset: Int = 1
	): PokemonResponse

	@GET("pokemon/{name}")
	suspend fun getPokemonDetails(@Path("name") name: String): ApiResponse<PokemonDetails>
}