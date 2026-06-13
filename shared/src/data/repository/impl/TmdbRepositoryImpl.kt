package com.charan.bingediary.data.repository.impl

import com.charan.bingediary.data.remote.tmdb.datasource.TmdbDataSource
import com.charan.bingediary.data.remote.tmdb.dto.MovieDetailsDto
import com.charan.bingediary.data.remote.tmdb.dto.MovieResponseDto
import com.charan.bingediary.data.remote.tmdb.dto.ShowDetailsDto
import com.charan.bingediary.data.remote.tmdb.dto.ShowResponseDto
import com.charan.bingediary.data.remote.tmdb.dto.PersonDetailsDto
import com.charan.bingediary.data.remote.tmdb.dto.SearchResponseDto
import com.charan.bingediary.data.repository.TmdbRepository

import org.koin.core.annotation.Single

@Single
class TmdbRepositoryImpl(
    private val tmdbDataSource: TmdbDataSource
) : TmdbRepository {

    override suspend fun getTrendingMovies(
        watchRegion: String,
        page: Int
    ): Result<MovieResponseDto> {
        return runCatching {
            tmdbDataSource.getTrendingMovies(watchRegion, page)
        }
    }

    override suspend fun getTrendingTvShows(
        watchRegion: String,
        page: Int
    ): Result<ShowResponseDto> {
        return runCatching {
            tmdbDataSource.getTrendingTvShows(watchRegion, page)
        }
    }

    override suspend fun getMovieDetails(
        movieId: Long,
        language: String
    ): Result<MovieDetailsDto> {
        return runCatching {
            tmdbDataSource.getMovieDetails(movieId, language)
        }
    }

    override suspend fun getShowDetails(
        seriesId: Long,
        language: String
    ): Result<ShowDetailsDto> {
        return runCatching {
            tmdbDataSource.getShowDetails(seriesId, language)
        }
    }

    override suspend fun getPersonDetails(
        personId: Long,
        language: String
    ): Result<PersonDetailsDto> {
        return runCatching {
            tmdbDataSource.getPersonDetails(personId, language)
        }
    }

    override suspend fun searchMulti(
        query: String,
        page: Int
    ): Result<SearchResponseDto> {
        return runCatching {
            tmdbDataSource.searchMulti(query, page)
        }
    }
}