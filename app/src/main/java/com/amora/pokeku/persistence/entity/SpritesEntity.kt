package com.amora.pokeku.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

data class SpritesEntity(

	@ColumnInfo(name = "front_default")
	val frontDefault: String? = null
)