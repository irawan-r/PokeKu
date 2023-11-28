package com.amora.pokeku.persistence

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.amora.pokeku.persistence.entity.PokemonCatchEntity
import com.amora.pokeku.persistence.entity.PokemonCompleteDetails
import com.amora.pokeku.persistence.entity.PokemonDetailsEntity
import com.amora.pokeku.persistence.entity.PokemonEntity
import com.amora.pokeku.persistence.entity.StatsItemEntity
import com.amora.pokeku.persistence.entity.TypesEntity

@Dao
interface PokemonDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertPokemons(data: List<PokemonEntity>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertPokemonDetails(detailsEntity: PokemonDetailsEntity)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertStatsItem(statsItem: List<StatsItemEntity>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertTypesItem(typesItem: List<TypesEntity>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertRenamedPokemon(pokemon: PokemonCatchEntity)

	@Transaction
	suspend fun insertCompletePokemonDetails(pokemonCompleteDetails: PokemonCompleteDetails) {
		insertPokemonDetails(pokemonCompleteDetails.pokemonDetails)
		insertStatsItem(pokemonCompleteDetails.stats)
		insertTypesItem(pokemonCompleteDetails.types)
	}

	@Transaction
	@Query("SELECT * FROM details WHERE remote_id = :pokeId")
	suspend fun getPokemonDetailsComplete(pokeId: Int): PokemonCompleteDetails?

	@Query("SELECT * FROM pokemon")
	fun getAllPokemon(): PagingSource<Int, PokemonEntity>

	@Query("SELECT * FROM details WHERE name = :name")
	suspend fun getPokemonDetails(name: String): PokemonDetailsEntity?

	@Query("SELECT * FROM poke_catch")
	suspend fun getPokemonInventory(): List<PokemonCatchEntity>?

	@Query("SELECT * FROM poke_catch WHERE name = :name ORDER BY id DESC")
	suspend fun getCurrentNamed(name: String?): List<PokemonCatchEntity>?

	@Query("DELETE FROM pokemon")
	suspend fun deletePokemonsList()
}