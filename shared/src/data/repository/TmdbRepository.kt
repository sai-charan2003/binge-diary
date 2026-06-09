package com.charan.bingediary.data.repository

import com.charan.bingediary.data.remote.tmdb.dto.MovieDto
import com.charan.bingediary.data.remote.tmdb.dto.MovieResponseDto
import com.charan.bingediary.data.remote.tmdb.dto.ShowResponseDto

interface TmdbRepository {
    suspend fun getTrendingMovies(watchRegion: String, page: Int = 1) : Result<MovieResponseDto>

    suspend fun getTrendingTvShows(watchRegion: String, page: Int = 1) : Result<ShowResponseDto>
}