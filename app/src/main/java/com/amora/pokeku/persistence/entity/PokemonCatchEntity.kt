package com.amora.pokeku.persistence.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "poke_catch")
data class PokemonCatchEntity(
	@PrimaryKey
	val id: String,
	val name: String?,
	val renamed: String?,
	val sprites: String?
)
