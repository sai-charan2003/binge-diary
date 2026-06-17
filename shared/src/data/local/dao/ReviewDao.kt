package com.charan.bingediary.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.charan.bingediary.data.local.entity.ReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reviews: List<ReviewEntity>)

    @Query("SELECT * FROM reviews WHERE user_id = :userId")
    fun getUserReviews(userId: String): Flow<List<ReviewEntity>>
    
    @Query("SELECT * FROM reviews WHERE user_id = :userId AND tmdb_movie_id = :tmdbMovieId LIMIT 1")
    fun getUserReviewForMovieFlow(userId: String, tmdbMovieId: Long): Flow<ReviewEntity?>

    @Query("DELETE FROM reviews WHERE user_id = :userId")
    suspend fun clearUserReviews(userId: String)
}
