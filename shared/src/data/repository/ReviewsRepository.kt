package com.charan.bingediary.data.repository

import com.charan.bingediary.data.remote.model.ReviewDto

interface ReviewsRepository {
    suspend fun saveReview(
        tmdbMovieId: Long,
        reviewId: String?,
        userMovieId: String?,
        reviewText: String,
        rating: Float,
        isWatched: Boolean,
        inWatchlist: Boolean,
        isWatching: Boolean,
        isLoved: Boolean
    ): Boolean

    suspend fun getUserReview(tmdbMovieId: Long): ReviewDto?

    suspend fun getUserReviews(): List<ReviewDto>
}
