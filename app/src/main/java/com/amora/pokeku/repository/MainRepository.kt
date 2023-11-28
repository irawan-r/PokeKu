package com.amora.pokeku.repository

import androidx.paging.PagingData
import com.amora.pokeku.persistence.entity.PokemonCatchEntity
import com.amora.pokeku.persistence.entity.PokemonCompleteDetails
import com.amora.pokeku.persistence.entity.PokemonEntity
import com.amora.pokeku.repository.model.PokeMark
import kotlinx.coroutines.flow.Flow

interface MainRepository {
	fun getPokemons(page: Int = 0): Flow<PagingData<PokemonEntity>>

	fun getPokemonDetails(poke: PokeMark): Flow<PokemonCompleteDetails?>

	fun getPokemonInventory(): Flow<List<PokemonCatchEntity>>

	fun catchPokemon(): Flow<Boolean>

	fun releasePokemon(): Flow<Boolean>

	fun renamePokemon(name: String?, id: Int?): Flow<String>

	suspend fun insertNamePokemon(pokemon: PokemonCompleteDetails?, renamed: String)
}