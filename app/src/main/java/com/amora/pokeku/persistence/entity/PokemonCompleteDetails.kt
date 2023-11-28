package com.amora.pokeku.persistence.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.amora.pokeku.BuildConfig

data class PokemonCompleteDetails(
	@Embedded
	val pokemonDetails: PokemonDetailsEntity,

	@Relation(parentColumn = "remote_id", entityColumn = "fkId")
	val stats: List<StatsItemEntity>,

	@Relation(parentColumn = "remote_id", entityColumn = "fkId")
	val types: List<TypesEntity>
) {
	companion object {
		fun PokemonCompleteDetails.getImageUrl(): String {
			val index = pokemonDetails.remoteId
			return "${BuildConfig.BASE_IMG_URL}$index.png"
		}
	}
}