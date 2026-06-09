package com.charan.bingediary.data.repository.impl

import com.charan.bingediary.data.remote.tmdb.datasource.TmdbDataSource
import com.charan.bingediary.data.remote.tmdb.dto.MovieResponseDto
import com.charan.bingediary.data.remote.tmdb.dto.ShowResponseDto
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
}