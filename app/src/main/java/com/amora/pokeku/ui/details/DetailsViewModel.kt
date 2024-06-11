package com.amora.pokeku.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amora.pokeku.repository.MainRepository
import com.amora.pokeku.repository.model.PokeMark
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
	private val repository: MainRepository
) : ViewModel() {
	private val pokeNameSharedFlow: MutableSharedFlow<PokeMark> = MutableSharedFlow(replay = 1)

	private val catchPokemon: MutableStateFlow<Boolean?> = MutableStateFlow(null)
	val getPokemon = catchPokemon.asStateFlow()

	private val _isLoading = MutableStateFlow(false)
	val isLoading = _isLoading.asStateFlow()

	@OptIn(ExperimentalCoroutinesApi::class)
	val posterDetailsFlow = pokeNameSharedFlow.flatMapLatest {
		repository.getPokemonDetails(it)
	}

	fun loadPosterByName(poke: PokeMark) = pokeNameSharedFlow.tryEmit(poke)

	fun catchPokemon() {
		viewModelScope.launch {
			repository.catchPokemon().collect { result ->
				catchPokemon.update { result }
			}
		}
	}

	fun setLoading() {
		_isLoading.value = _isLoading.value.not()
		catchPokemon.value = null
	}

	fun insertPokemonName() {
		viewModelScope.launch {
			val pokemon = posterDetailsFlow.firstOrNull()?.pokemonDetails
			val name = pokemon?.name
			val id = pokemon?.id
			repository.renamePokemon(name, id).collectLatest { renamed ->
				repository.insertNamePokemon(pokemon = posterDetailsFlow.firstOrNull(), renamed)
			}
		}
	}
}