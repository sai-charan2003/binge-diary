package com.charan.bingediary.data.repository

import com.charan.bingediary.data.remote.tmdb.dto.MovieDetailsDto
import com.charan.bingediary.data.remote.tmdb.dto.MovieDto
import com.charan.bingediary.data.remote.tmdb.dto.MovieResponseDto
import com.charan.bingediary.data.remote.tmdb.dto.ShowDetailsDto
import com.charan.bingediary.data.remote.tmdb.dto.ShowResponseDto
import com.charan.bingediary.data.remote.tmdb.dto.PersonDetailsDto
import com.charan.bingediary.data.remote.tmdb.dto.SearchResponseDto

interface TmdbRepository {
    suspend fun getTrendingMovies(watchRegion: String, page: Int = 1) : Result<MovieResponseDto>

    suspend fun getTrendingTvShows(watchRegion: String, page: Int = 1) : Result<ShowResponseDto>

    suspend fun getMovieDetails(movieId: Long, language: String = "en-US") : Result<MovieDetailsDto>

    suspend fun getShowDetails(seriesId: Long, language: String = "en-US") : Result<ShowDetailsDto>

    suspend fun getPersonDetails(personId: Long, language: String = "en-US") : Result<PersonDetailsDto>

    suspend fun searchMulti(query: String, page: Int = 1) : Result<SearchResponseDto>
}