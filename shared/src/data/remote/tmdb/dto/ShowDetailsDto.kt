package com.charan.bingediary.data.remote.tmdb.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShowDetailsDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("overview") val overview: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("first_air_date") val firstAirDate: String? = null,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Int,
    @SerialName("episode_run_time") val episodeRunTime: List<Int>? = null,
    @SerialName("credits") val credits: CreditsDto? = null,
    @SerialName("created_by") val createdBy: List<CreatedByDto>? = null
)

@Serializable
data class CreatedByDto(
    @SerialName("id") val id: Long,
    @SerialName("credit_id") val creditId: String,
    @SerialName("name") val name: String,
    @SerialName("original_name") val originalName: String? = null,
    @SerialName("gender") val gender: Int? = null,
    @SerialName("profile_path") val profilePath: String? = null
)
