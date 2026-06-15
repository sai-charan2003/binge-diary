package com.charan.bingediary.data.repository

import com.charan.bingediary.data.remote.model.UserMovieDto

interface UserMoviesRepository {
    suspend fun getWatchlistStatus(tmdbMovieId: Long): Boolean
    suspend fun addToWatchlist(tmdbMovieId: Long): Boolean
    suspend fun removeFromWatchlist(tmdbMovieId: Long): Boolean
    suspend fun getUserMovie(tmdbMovieId: Long): UserMovieDto?
}
