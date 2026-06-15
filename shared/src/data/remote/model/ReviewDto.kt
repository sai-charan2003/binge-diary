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
