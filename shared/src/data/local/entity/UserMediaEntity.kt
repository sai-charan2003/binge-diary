package com.charan.bingediary.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_media")
data class UserMediaEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "tmdb_movie_id")
    val tmdbMovieId: Long,
    @ColumnInfo(name = "in_watchlist")
    val inWatchlist: Boolean,
    @ColumnInfo(name = "is_watching")
    val isWatching: Boolean,
    @ColumnInfo(name = "is_watched")
    val isWatched: Boolean,
    @ColumnInfo(name = "is_loved")
    val isLoved: Boolean,
    @ColumnInfo(name = "rating")
    val rating: Double?,
    @ColumnInfo(name = "added_at")
    val addedAt: String?,
    @ColumnInfo(name = "watched_at")
    val watchedAt: String?
)
