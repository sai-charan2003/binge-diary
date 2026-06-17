package com.charan.bingediary.data.repository

import com.charan.bingediary.data.remote.model.UserMediaDto

interface UserMediaRepository {
    suspend fun getWatchlistStatus(tmdbMovieId: Long, mediaType: String): Boolean
    suspend fun addToWatchlist(tmdbMovieId: Long, mediaType: String): Boolean
    suspend fun removeFromWatchlist(tmdbMovieId: Long, mediaType: String): Boolean
    suspend fun getUserMedia(tmdbMovieId: Long, mediaType: String): UserMediaDto?
}
