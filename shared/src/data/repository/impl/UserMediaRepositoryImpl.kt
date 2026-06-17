package com.charan.bingediary.data.repository.impl

import com.charan.bingediary.data.remote.model.UserMediaDto
import com.charan.bingediary.data.remote.supabase.SupabaseRemoteDataSource
import com.charan.bingediary.data.repository.UserMediaRepository
import org.koin.core.annotation.Singleton

import com.charan.bingediary.data.local.dao.UserMediaDao
import com.charan.bingediary.data.local.mapper.toEntity

@Singleton(binds = [UserMediaRepository::class])
class UserMediaRepositoryImpl(
    private val supabaseRemoteDataSource: SupabaseRemoteDataSource,
    private val userMediaDao: UserMediaDao
) : UserMediaRepository {

    override suspend fun getWatchlistStatus(tmdbMovieId: Long, mediaType: String): Boolean {
        val userId = supabaseRemoteDataSource.getCurrentUserId() ?: return false
        val media = supabaseRemoteDataSource.getUserMedia(userId, tmdbMovieId, mediaType)
        return media?.inWatchlist == true
    }

    override suspend fun addToWatchlist(tmdbMovieId: Long, mediaType: String): Boolean {
        val userId = supabaseRemoteDataSource.getCurrentUserId() ?: return false
        return try {
            val existing = supabaseRemoteDataSource.getUserMedia(userId, tmdbMovieId, mediaType)
            val updated = existing?.copy(inWatchlist = true) ?: UserMediaDto(
                userId = userId,
                tmdbMovieId = tmdbMovieId,
                inWatchlist = true,
                isWatching = false,
                isWatched = false,
                mediaType = mediaType
            )
            supabaseRemoteDataSource.upsertUserMedia(updated)
            userMediaDao.insertUserMedia(updated.toEntity())
            true
        } catch (e: Exception) {
            println("Error adding to watchlist: ${e.message}")
            false
        }
    }

    override suspend fun removeFromWatchlist(tmdbMovieId: Long, mediaType: String): Boolean {
        val userId = supabaseRemoteDataSource.getCurrentUserId() ?: return false
        return try {
            val existing = supabaseRemoteDataSource.getUserMedia(userId, tmdbMovieId, mediaType)
            if (existing != null) {
                val updated = existing.copy(inWatchlist = false)
                supabaseRemoteDataSource.upsertUserMedia(updated)
                userMediaDao.insertUserMedia(updated.toEntity())
            }
            true
        } catch (e: Exception) {
            println("Error removing from watchlist: ${e.message}")
            false
        }
    }

    override suspend fun getUserMedia(tmdbMovieId: Long, mediaType: String): UserMediaDto? {
        val userId = supabaseRemoteDataSource.getCurrentUserId() ?: return null
        return supabaseRemoteDataSource.getUserMedia(userId, tmdbMovieId, mediaType)
    }
}
