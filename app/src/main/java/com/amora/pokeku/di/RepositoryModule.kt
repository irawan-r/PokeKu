package com.amora.pokeku.di

import com.amora.pokeku.network.ApiService
import com.amora.pokeku.persistence.PokemonDao
import com.amora.pokeku.persistence.PokemonDatabase
import com.amora.pokeku.repository.MainRepository
import com.amora.pokeku.repository.MainRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

	@Provides
	@Singleton
	fun provideRepository(
		apiService: ApiService,
		data: PokemonDatabase
	): MainRepository {
		return MainRepositoryImpl(
			apiService,
			data
		)
	}
}