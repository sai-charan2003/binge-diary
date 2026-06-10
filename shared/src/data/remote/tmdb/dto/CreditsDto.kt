package com.charan.bingediary.data.remote.tmdb.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreditsDto(
    @SerialName("cast") val cast: List<CastDto> = emptyList(),
    @SerialName("crew") val crew: List<CrewDto> = emptyList()
)

@Serializable
data class CastDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("profile_path") val profilePath: String? = null,
    @SerialName("character") val character: String? = null
)

@Serializable
data class CrewDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("profile_path") val profilePath: String? = null,
    @SerialName("job") val job: String? = null
)
