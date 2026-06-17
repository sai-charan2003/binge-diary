package com.charan.bingediary.data.repository

import com.charan.bingediary.data.remote.model.ReviewDto
import com.charan.bingediary.data.remote.model.ReviewWithProfileDto
import kotlinx.coroutines.flow.Flow

interface ReviewsRepository {
    suspend fun saveReview(
        tmdbMovieId: Long,
        mediaType: String,
        reviewId: String?,
        userMovieId: String?,
        reviewText: String,
        rating: Float,
        isWatched: Boolean,
        inWatchlist: Boolean,
        isWatching: Boolean,
        isLoved: Boolean
    ): Boolean

    fun getUserReview(tmdbMovieId: Long): Flow<ReviewDto?>

    fun getUserReviews(): Flow<List<ReviewDto>>

    suspend fun syncUserReviews()

    suspend fun getAllReviews(): List<ReviewWithProfileDto>
}

