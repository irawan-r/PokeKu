package com.amora.pokeku.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amora.pokeku.persistence.entity.PokemonCatchEntity
import com.amora.pokeku.persistence.entity.PokemonDetailsEntity
import com.amora.pokeku.persistence.entity.PokemonEntity
import com.amora.pokeku.persistence.entity.RemoteKeys
import com.amora.pokeku.persistence.entity.StatsItemEntity
import com.amora.pokeku.persistence.entity.TypesEntity

@Database(
	entities = [PokemonEntity::class, StatsItemEntity::class, PokemonDetailsEntity::class, RemoteKeys::class, TypesEntity::class, PokemonCatchEntity::class],
	version = 1,
	exportSchema = true
)
abstract class PokemonDatabase : RoomDatabase() {

	abstract fun pokemonDao(): PokemonDao

	abstract fun remoteKeysDao(): RemoteKeysDao
}