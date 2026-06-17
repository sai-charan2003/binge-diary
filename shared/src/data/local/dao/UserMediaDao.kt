package com.charan.bingediary.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.charan.bingediary.data.local.entity.UserMediaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserMediaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserMedia(media: UserMediaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(media: List<UserMediaEntity>)

    @Query("SELECT * FROM user_media WHERE user_id = :userId")
    fun getUserMedia(userId: String): Flow<List<UserMediaEntity>>
    
    @Query("SELECT * FROM user_media WHERE user_id = :userId AND tmdb_movie_id = :tmdbMovieId LIMIT 1")
    suspend fun getUserMediaForMovie(userId: String, tmdbMovieId: Long): UserMediaEntity?

    @Query("DELETE FROM user_media WHERE user_id = :userId")
    suspend fun clearUserMedia(userId: String)
}
