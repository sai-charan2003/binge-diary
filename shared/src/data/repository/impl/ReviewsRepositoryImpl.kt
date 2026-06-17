package com.charan.bingediary.data.repository.impl

import com.charan.bingediary.data.remote.model.ReviewDto
import com.charan.bingediary.data.remote.model.UserMediaDto
import com.charan.bingediary.data.remote.supabase.SupabaseRemoteDataSource
import com.charan.bingediary.data.repository.ReviewsRepository
import org.koin.core.annotation.Singleton

import com.charan.bingediary.data.local.dao.ReviewDao
import com.charan.bingediary.data.local.dao.UserMediaDao
import com.charan.bingediary.data.local.mapper.toEntity
import com.charan.bingediary.data.local.mapper.toDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Singleton(binds = [ReviewsRepository::class])
class ReviewsRepositoryImpl(
    private val supabaseRemoteDataSource: SupabaseRemoteDataSource,
    private val reviewDao: ReviewDao,
    private val userMediaDao: UserMediaDao
) : ReviewsRepository {

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun saveReview(
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
    ): Boolean {
        val userId = supabaseRemoteDataSource.getCurrentUserId() ?: return false
        return try {
            // 1. Save review via direct upsert (passing the fetched reviewId UUID if it exists)
            val reviewDto = ReviewDto(
                id = reviewId ?: Uuid.generateV4().toString(),
                userId = userId,
                tmdbMovieId = tmdbMovieId,
                reviewText = reviewText,
                rating = rating
            )
            supabaseRemoteDataSource.upsertReview(reviewDto)
            reviewDao.insertReview(reviewDto.toEntity())

            // 2. Sync watch status & rating in user_media via direct upsert (passing the fetched userMovieId UUID if it exists)
            val mediaDto = UserMediaDto(
                id = userMovieId ?: Uuid.generateV4().toString(),
                userId = userId ,
                tmdbMovieId = tmdbMovieId,
                inWatchlist = inWatchlist,
                isWatching = isWatching,
                isWatched = isWatched,
                isLoved = isLoved,
                rating = rating.toDouble(),
                mediaType = mediaType
            )
            supabaseRemoteDataSource.upsertUserMedia(mediaDto)
            userMediaDao.insertUserMedia(mediaDto.toEntity())

            true
        } catch (e: Exception) {
            println("Error saving review: ${e.message}")
            false
        }
    }

    override fun getUserReview(tmdbMovieId: Long): Flow<ReviewDto?> {
        val userId = supabaseRemoteDataSource.getCurrentUserId() ?: return flowOf(null)
        return reviewDao.getUserReviewForMovieFlow(userId, tmdbMovieId).map { it?.toDto() }
    }

    override fun getUserReviews(): Flow<List<ReviewDto>> {
        val userId = supabaseRemoteDataSource.getCurrentUserId() ?: return flowOf(emptyList())
        return reviewDao.getUserReviews(userId).map { list -> list.map { it.toDto() } }
    }

    override suspend fun syncUserReviews() {
        val userId = supabaseRemoteDataSource.getCurrentUserId() ?: return
        try {
            val remoteReviews = supabaseRemoteDataSource.getUserReviews(userId)
            if (remoteReviews.isNotEmpty()) {
                reviewDao.insertAll(remoteReviews.map { it.toEntity() })
            }
        } catch (e: Exception) {
            println("Error syncing user reviews: ${e.message}")
        }
    }

    override suspend fun getAllReviews(): List<com.charan.bingediary.data.remote.model.ReviewWithProfileDto> {
        return supabaseRemoteDataSource.getAllReviews()
    }
}
