package com.charan.bingediary.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.EncodeDefault

@Serializable
data class ReviewDto(
    @SerialName("id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val id: String? = null,
    @SerialName("user_id")
    val userId: String,
    @SerialName("tmdb_movie_id")
    val tmdbMovieId: Long,
    @SerialName("review_text")
    val reviewText: String,
    @SerialName("rating")
    val rating: Float,
    @SerialName("created_at")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val createdAt: String? = null,
    @SerialName("updated_at")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val updatedAt: String? = null
)

@Serializable
data class UserProfileDto(
    @SerialName("username")
    val username: String? = null,
    @SerialName("avatar_url")
    val avatarUrl: String? = null
)

@Serializable
data class ReviewWithProfileDto(
    @SerialName("id")
    val id: String? = null,
    @SerialName("user_id")
    val userId: String,
    @SerialName("tmdb_movie_id")
    val tmdbMovieId: Long,
    @SerialName("review_text")
    val reviewText: String,
    @SerialName("rating")
    val rating: Float,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null,
    @SerialName("user_profiles")
    val userProfile: UserProfileDto? = null
)

