package com.amora.pokeku.ui.main

import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amora.pokeku.persistence.entity.PokemonCatchEntity
import com.amora.pokeku.persistence.entity.PokemonEntity
import com.amora.pokeku.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	mainRepository: MainRepository
) : ViewModel() {

	private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
	val isLoading: State<Boolean> get() = _isLoading

	private val _selectedTab: MutableState<Int> = mutableIntStateOf(0)
	val selectedTab: State<Int> get() = _selectedTab

	val pokemonList: Flow<PagingData<PokemonEntity>> =
		mainRepository.getPokemons().cachedIn(viewModelScope)

	val pokemonInventory: Flow<List<PokemonCatchEntity>> = mainRepository.getPokemonInventory()

	fun selectTab(@StringRes tab: Int) {
		_selectedTab.value = tab
	}

	fun triggerLoading(loading: Boolean) {
		_isLoading.value = loading
	}
}