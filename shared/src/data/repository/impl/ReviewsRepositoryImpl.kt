package com.charan.bingediary.data.repository.impl

import com.charan.bingediary.data.remote.model.ReviewDto
import com.charan.bingediary.data.remote.model.UserMovieDto
import com.charan.bingediary.data.remote.supabase.SupabaseRemoteDataSource
import com.charan.bingediary.data.repository.ReviewsRepository
import org.koin.core.annotation.Singleton

@Singleton(binds = [ReviewsRepository::class])
class ReviewsRepositoryImpl(
    private val supabaseRemoteDataSource: SupabaseRemoteDataSource
) : ReviewsRepository {

    override suspend fun saveReview(
        tmdbMovieId: Long,
        reviewId: String?,
        userMovieId: String?,
        reviewText: String,
        rating: Float,
        isWatched: Boolean,
        inWatchlist: Boolean,
        isWatching: Boolean,
        isLoved: Boolean
    ): Boolean {
        val userId = supabaseRemoteDataSource.getCurrentUserId() ?: return false
        return try {
            // 1. Save review via direct upsert (passing the fetched reviewId UUID if it exists)
            val reviewDto = ReviewDto(
                id = reviewId,
                userId = userId,
                tmdbMovieId = tmdbMovieId,
                reviewText = reviewText,
                rating = rating
            )
            supabaseRemoteDataSource.upsertReview(reviewDto)

            // 2. Sync watch status & rating in user_movies via direct upsert (passing the fetched userMovieId UUID if it exists)
            val movieDto = UserMovieDto(
                id = userMovieId,
                userId = userId,
                tmdbMovieId = tmdbMovieId,
                inWatchlist = inWatchlist,
                isWatching = isWatching,
                isWatched = isWatched,
                isLoved = isLoved,
                rating = rating.toDouble()
            )
            supabaseRemoteDataSource.upsertUserMovie(movieDto)

            true
        } catch (e: Exception) {
            println("Error saving review: ${e.message}")
            false
        }
    }

    override suspend fun getUserReview(tmdbMovieId: Long): ReviewDto? {
        val userId = supabaseRemoteDataSource.getCurrentUserId() ?: return null
        return supabaseRemoteDataSource.getUserReview(userId, tmdbMovieId)
    }
}
