package com.charan.bingediary.data.remote.tmdb.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonDetailsDto(
    val id: Long,
    val name: String,
    val biography: String? = null,
    val birthday: String? = null,
    val deathday: String? = null,
    @SerialName("place_of_birth")
    val placeOfBirth: String? = null,
    @SerialName("profile_path")
    val profilePath: String? = null,
    @SerialName("known_for_department")
    val knownForDepartment: String? = null,
    val popularity: Double? = null,
    @SerialName("combined_credits")
    val combinedCredits: CombinedCreditsDto? = null
)

@Serializable
data class CombinedCreditsDto(
    val cast: List<PersonCreditDto> = emptyList(),
    val crew: List<PersonCreditDto> = emptyList()
)

@Serializable
data class PersonCreditDto(
    val id: Long,
    @SerialName("media_type")
    val mediaType: String, // "movie" or "tv"
    val title: String? = null, // Used for movies
    val name: String? = null, // Used for TV shows
    @SerialName("poster_path")
    val posterPath: String? = null,
    val character: String? = null, // Used in cast
    val job: String? = null, // Used in crew
    val department: String? = null, // Used in crew
    @SerialName("release_date")
    val releaseDate: String? = null, // Used for movies
    @SerialName("first_air_date")
    val firstAirDate: String? = null, // Used for TV shows
    val popularity: Double? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null
)
