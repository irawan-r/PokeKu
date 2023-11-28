package com.amora.pokeku.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "details")
data class PokemonDetailsEntity(

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	val id: Int? = null,

	@ColumnInfo(name = "name")
	val name: String? = null,

	@ColumnInfo(name = "base_exp")
	val baseExperience: Int? = null,

	@ColumnInfo(name = "weight")
	val weight: Int? = null,

	@Embedded(prefix = "sub_")
	val sprites: SpritesEntity?,

	@ColumnInfo(name = "remote_id")
	val remoteId: Int? = null,

	@ColumnInfo(name = "height")
	val height: Int? = null
)