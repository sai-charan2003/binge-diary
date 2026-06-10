package com.charan.bingediary.data.remote.tmdb.datasource

import com.charan.bingediary.data.remote.tmdb.api.TmdbApi
import org.koin.core.annotation.Single
import org.koin.core.annotation.Singleton

@Single
class TmdbDataSource(
    private val tmdbApi: TmdbApi,
) {
    suspend fun getTrendingMovies(watchRegion: String, page: Int = 1) = tmdbApi.trendingMovies(watchRegion, page)

    suspend fun getTrendingTvShows(watchRegion: String, page: Int = 1) = tmdbApi.trendingTvShows(watchRegion, page)

    suspend fun getMovieDetails(movieId: Long, language: String = "en-US") = tmdbApi.getMovieDetails(movieId, language)

    suspend fun getShowDetails(seriesId: Long, language: String = "en-US") = tmdbApi.getShowDetails(seriesId, language)
}
