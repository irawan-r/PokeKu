package com.amora.pokeku.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.amora.pokeku.network.ApiService
import com.amora.pokeku.persistence.PokemonDatabase
import com.amora.pokeku.persistence.entity.PokemonCatchEntity
import com.amora.pokeku.persistence.entity.PokemonCompleteDetails
import com.amora.pokeku.persistence.entity.PokemonEntity
import com.amora.pokeku.repository.DataMapper.toListStatsItemEntity
import com.amora.pokeku.repository.DataMapper.toListTypeEntity
import com.amora.pokeku.repository.DataMapper.toPokemonCompleteDetails
import com.amora.pokeku.repository.DataMapper.toPokemonDetailsEntity
import com.amora.pokeku.repository.mediator.PokemonRemoteMediator
import com.amora.pokeku.repository.mediator.PokemonRemoteMediator.Companion.PAGE_SIZE
import com.amora.pokeku.repository.model.PokeMark
import com.amora.pokeku.ui.utils.NameRenamer
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class MainRepositoryImpl @Inject constructor(
	private val apiService: ApiService,
	private val database: PokemonDatabase
) : MainRepository {

	companion object {
		const val CHANCE_CATCH = 0.5
	}

	@OptIn(ExperimentalPagingApi::class)
	override fun getPokemons(
		page: Int
	): Flow<PagingData<PokemonEntity>> {
		return flow {
			val pager = Pager(
				config = PagingConfig(pageSize = PAGE_SIZE),
				remoteMediator = PokemonRemoteMediator(
					database = database,
					apiService = apiService,
					page = page
				),
				pagingSourceFactory = {
					database.pokemonDao().getAllPokemon()
				}
			)
			emitAll(pager.flow)
		}.flowOn(Dispatchers.IO)
	}

	override fun getPokemonDetails(
		poke: PokeMark
	): Flow<PokemonCompleteDetails?> {
		return flow {
			val pokemonDetails = apiService.getPokemonDetails(poke.name)
			val localPokemonDetails = database.pokemonDao().getPokemonDetailsComplete(poke.id)
			if (localPokemonDetails != null) {
				emit(localPokemonDetails)
			} else {
				pokemonDetails.suspendOnSuccess {
					val detailsData = PokemonCompleteDetails(
						pokemonDetails = data.toPokemonDetailsEntity(),
						stats = data.stats.toListStatsItemEntity(data.id),
						types = data.types.toListTypeEntity(data.id)
					)
					database.pokemonDao().insertCompletePokemonDetails(detailsData)
					emit(data.toPokemonCompleteDetails())
				}
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun getPokemonInventory(): Flow<List<PokemonCatchEntity>> {
		return flow {
			emit(database.pokemonDao().getPokemonInventory() ?: emptyList())
		}
	}

	override fun catchPokemon(): Flow<Boolean> {
		return flow {
			val randomValue = Random.nextDouble()
			val isSuccess = randomValue <= CHANCE_CATCH
			delay(500.milliseconds)
			emit(isSuccess)
		}
	}

	override fun releasePokemon(): Flow<Boolean> {
		return flow {
			val number = Random.nextInt(1, 100)
			if (number <= 1) {
				emit(false)
			}
			val sqrt = sqrt(number.toDouble()).toInt()
			for (i in 2..sqrt) {
				if (number % i == 0) {
					emit(false)
				}
			}
			delay(500.milliseconds)
			emit(true)
		}

	}

	override fun renamePokemon(name: String?, id: Int?): Flow<String> {
		return flow {
			val getPrevPoke = database.pokemonDao().getCurrentNamed(name)?.firstOrNull()
			val id = getPrevPoke?.id?.split("-")?.lastOrNull()?.toIntOrNull()
			if (getPrevPoke != null) {
				val renamer = NameRenamer(name, id).rename()
				emit(renamer)
			} else {
				val renamer = NameRenamer(name, 0).rename()
				emit(renamer)
			}
		}
	}

	override suspend fun insertNamePokemon(pokemon: PokemonCompleteDetails?, renamed: String) {
		val pokemon = pokemon
		val id = renamed.split("-").lastOrNull()?.toIntOrNull() ?: 0
		val genId = "${pokemon?.pokemonDetails?.name}-${id}"
		val pokemonCatch = PokemonCatchEntity(
			name = pokemon?.pokemonDetails?.name,
			renamed = renamed,
			id = genId,
			sprites = pokemon?.pokemonDetails?.sprites?.frontDefault
		)
		database.pokemonDao().insertRenamedPokemon(pokemonCatch)
	}
}