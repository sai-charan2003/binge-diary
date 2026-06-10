package com.charan.bingediary.data.remote.tmdb.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("overview") val overview: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Int,
    @SerialName("runtime") val runtime: Int? = null,
    @SerialName("credits") val credits: CreditsDto? = null
)
