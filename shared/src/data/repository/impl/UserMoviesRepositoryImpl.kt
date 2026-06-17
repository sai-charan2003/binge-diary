package com.charan.bingediary.data.repository.impl

import com.charan.bingediary.data.remote.model.UserMovieDto
import com.charan.bingediary.data.remote.supabase.SupabaseRemoteDataSource
import com.charan.bingediary.data.repository.UserMoviesRepository
import org.koin.core.annotation.Singleton

import com.charan.bingediary.data.local.dao.UserMediaDao
import com.charan.bingediary.data.local.mapper.toEntity

@Singleton(binds = [UserMoviesRepository::class])
class UserMoviesRepositoryImpl(
    private val supabaseRemoteDataSource: SupabaseRemoteDataSource,
    private val userMediaDao: UserMediaDao
) : UserMoviesRepository {

    override suspend fun getWatchlistStatus(tmdbMovieId: Long): Boolean {
        val userId = supabaseRemoteDataSource.getCurrentUserId() ?: return false
        val movie = supabaseRemoteDataSource.getUserMovie(userId, tmdbMovieId)
        return movie?.inWatchlist == true
    }

    override suspend fun addToWatchlist(tmdbMovieId: Long): Boolean {
        val userId = supabaseRemoteDataSource.getCurrentUserId() ?: return false
        return try {
            val existing = supabaseRemoteDataSource.getUserMovie(userId, tmdbMovieId)
            val updated = existing?.copy(inWatchlist = true) ?: UserMovieDto(
                userId = userId,
                tmdbMovieId = tmdbMovieId,
                inWatchlist = true,
                isWatching = false,
                isWatched = false
            )
            supabaseRemoteDataSource.upsertUserMovie(updated)
            userMediaDao.insertUserMedia(updated.toEntity())
            true
        } catch (e: Exception) {
            println("Error adding to watchlist: ${e.message}")
            false
        }
    }

    override suspend fun removeFromWatchlist(tmdbMovieId: Long): Boolean {
        val userId = supabaseRemoteDataSource.getCurrentUserId() ?: return false
        return try {
            val existing = supabaseRemoteDataSource.getUserMovie(userId, tmdbMovieId)
            if (existing != null) {
                val updated = existing.copy(inWatchlist = false)
                supabaseRemoteDataSource.upsertUserMovie(updated)
                userMediaDao.insertUserMedia(updated.toEntity())
            }

            true
        } catch (e: Exception) {
            println("Error removing from watchlist: ${e.message}")
            false
        }
    }

    override suspend fun getUserMovie(tmdbMovieId: Long): UserMovieDto? {
        val userId = supabaseRemoteDataSource.getCurrentUserId() ?: return null
        return supabaseRemoteDataSource.getUserMovie(userId, tmdbMovieId)
    }
}
