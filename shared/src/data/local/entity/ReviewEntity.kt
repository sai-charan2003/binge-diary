package com.charan.bingediary.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "tmdb_movie_id")
    val tmdbMovieId: Long,
    @ColumnInfo(name = "review_text")
    val reviewText: String,
    @ColumnInfo(name = "rating")
    val rating: Float,
    @ColumnInfo(name = "created_at")
    val createdAt: String?,
    @ColumnInfo(name = "updated_at")
    val updatedAt: String?
)
