package com.amora.pokeku.repository.model

import com.amora.pokeku.BuildConfig.BASE_IMG_URL
import com.squareup.moshi.Json

data class PokemonPoster(

	var page: Int = 0,

	val remoteId: Int? = 0,

	@Json(name="name")
	val name: String? = null,

	@Json(name="url")
	val url: String? = null
) {
	fun getImageUrl(): String {
		val index = url?.split("/".toRegex())?.dropLast(1)?.last()
		return "${BASE_IMG_URL}$index.png"
	}

	fun getPokeId(): Int? {
		val id = url?.split("/".toRegex())?.dropLast(1)?.last()?.toIntOrNull()
		return id
	}
}