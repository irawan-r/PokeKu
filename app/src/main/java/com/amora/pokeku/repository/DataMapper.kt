package com.amora.pokeku.repository

import com.amora.pokeku.persistence.entity.PokemonCompleteDetails
import com.amora.pokeku.persistence.entity.PokemonDetailsEntity
import com.amora.pokeku.persistence.entity.PokemonEntity
import com.amora.pokeku.persistence.entity.SpritesEntity
import com.amora.pokeku.persistence.entity.StatsEntity
import com.amora.pokeku.persistence.entity.StatsItemEntity
import com.amora.pokeku.persistence.entity.TypeEntity
import com.amora.pokeku.persistence.entity.TypesEntity
import com.amora.pokeku.repository.model.PokemonDetails
import com.amora.pokeku.repository.model.PokemonPoster
import com.amora.pokeku.repository.model.Sprites
import com.amora.pokeku.repository.model.Stat
import com.amora.pokeku.repository.model.StatsItem
import com.amora.pokeku.repository.model.Type
import com.amora.pokeku.repository.model.Types


object DataMapper {

	fun List<StatsItem?>?.toListStatsItemEntity(id: Int?): List<StatsItemEntity> {
		return this?.mapNotNull { it?.toStatsItem(id) } ?: emptyList()
	}

	fun List<Types>?.toListTypeEntity(id: Int?): List<TypesEntity> {
		return this?.map { it.toTypesEntity(id) } ?: emptyList()
	}

	fun List<TypesEntity>.toListTypes(): List<Types> {
		return this.map { it.toTypes() }
	}

	private fun PokemonPoster.toPokemonEntity(pageItem: Int): PokemonEntity {
		return PokemonEntity(page = pageItem, name = name, url = url)
	}

	fun List<PokemonPoster?>.toPokemonsEntity(page: Int): List<PokemonEntity> {
		return this.mapNotNull { it?.toPokemonEntity(page) }
	}

	private fun PokemonEntity.toPokemonPoster(): PokemonPoster {
		return PokemonPoster(name = name, url = url)
	}

	fun List<PokemonEntity>.toPokemons(): List<PokemonPoster> {
		return this.map { it.toPokemonPoster() }
	}

	fun PokemonDetails.toPokemonDetailsEntity(): PokemonDetailsEntity {
		return PokemonDetailsEntity(
			name = name,
			baseExperience = baseExperience,
			weight = weight,
			sprites = sprites?.toSpritesEntity(),
			remoteId = id,
			height = height
		)
	}

	fun PokemonCompleteDetails.toPokemonDetails(): PokemonDetails {
		return PokemonDetails(
			baseExperience = pokemonDetails.baseExperience,
			weight = pokemonDetails.weight,
			sprites = pokemonDetails.sprites?.toSprites(),
			types = types.toListTypes(),
			stats = stats.toListStats(),
			name = pokemonDetails.name,
			id = pokemonDetails.id,
			height = pokemonDetails.height
		)
	}

	fun PokemonDetails.toPokemonCompleteDetails(): PokemonCompleteDetails {
		return PokemonCompleteDetails(
			pokemonDetails = PokemonDetailsEntity(
				id = id,
				name = name,
				baseExperience = baseExperience,
				weight = weight,
				sprites = sprites?.toSpritesEntity(),
				remoteId = id,
				height = height
			),
			stats = stats.toListStatsEntity(id),
			types = types.toListTypeEntity(id)
		)
	}

	fun SpritesEntity.toSprites(): Sprites {
		return Sprites(frontDefault = frontDefault)
	}

	fun TypeEntity.toType(): Type {
		return Type(name = name, url = url)
	}

	private fun Sprites.toSpritesEntity(): SpritesEntity {
		return SpritesEntity(frontDefault = frontDefault)
	}

	fun PokemonDetailsEntity.toPokemonDetails(): PokemonDetails {
		return PokemonDetails(
			baseExperience = baseExperience,
			weight = weight,
			sprites = Sprites(frontDefault = this.sprites?.frontDefault),
			name = name,
			id = id,
			height = height
		)
	}

	private fun List<StatsItemEntity>.toListStats(): List<StatsItem> {
		return this.map { it.toStatItem() }
	}

	private fun List<StatsItem?>?.toListStatsEntity(id: Int?): List<StatsItemEntity> {
		return this?.mapNotNull { it?.toStatsItem(id) } ?: emptyList()
	}

	private fun StatsItem.toStatsItem(id: Int?): StatsItemEntity {
		return StatsItemEntity(
			fkId = id,
			stats = StatsEntity(name = this.stat?.name, url = this.stat?.url),
			baseStat = baseStat,
			effort = effort
		)
	}

	private fun Type.toTypeEntity(id: Int?): TypeEntity {
		return TypeEntity(name = name, url = url)
	}

	private fun Types.toTypesEntity(id: Int?): TypesEntity {
		return TypesEntity(fkId = id, slot = slot, type = TypeEntity(name = type?.name, url = type?.url))
	}

	private fun TypesEntity.toTypes(): Types {
		return Types(slot = slot, type = Type(name = type?.name, url = type?.url))
	}

	fun Types.toSpeciesEntity(): TypesEntity {
		return TypesEntity(
			slot = slot, type =
			TypeEntity(name = type?.name, url = type?.url)
		)
	}

	fun PokemonCompleteDetails.toPokemonData(): PokemonDetails {
		return PokemonDetails(
			baseExperience = pokemonDetails.baseExperience,
			weight = pokemonDetails.weight,
			sprites = Sprites(frontDefault = pokemonDetails.sprites?.frontDefault),
			name = pokemonDetails.name,
			id = pokemonDetails.id,
			height = pokemonDetails.height
		)
	}

	private fun List<StatsItemEntity>.toStatsItemList(): List<StatsItem> {
		return this.map { it.toStatItem() }
	}

	private fun StatsItemEntity.toStatItem(): StatsItem {
		return StatsItem(stat = stats?.toStat(), baseStat = baseStat)
	}

	private fun StatsEntity.toStat(): Stat {
		return Stat(name = name, url = url)
	}
}