package com.charan.bingediary.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.EncodeDefault

@Serializable
data class UserMediaDto(
    @SerialName("id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val id: String? = null,
    @SerialName("user_id")
    val userId: String,
    @SerialName("tmdb_movie_id")
    val tmdbMovieId: Long,
    @SerialName("in_watchlist")
    val inWatchlist: Boolean,
    @SerialName("is_watching")
    val isWatching: Boolean,
    @SerialName("is_watched")
    val isWatched: Boolean,
    @SerialName("is_loved")
    val isLoved: Boolean = false,
    @SerialName("rating")
    val rating: Double? = null,
    @SerialName("added_at")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val addedAt: String? = null,
    @SerialName("watched_at")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val watchedAt: String? = null,
    @SerialName("media_type")
    val mediaType: String
)
