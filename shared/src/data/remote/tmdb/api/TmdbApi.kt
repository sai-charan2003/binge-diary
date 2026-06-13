package com.charan.bingediary.data.remote.tmdb.api

import com.charan.bingediary.config.BuildConfig
import com.charan.bingediary.data.remote.tmdb.dto.MovieDetailsDto
import com.charan.bingediary.data.remote.tmdb.dto.MovieResponseDto
import com.charan.bingediary.data.remote.tmdb.dto.ShowDetailsDto
import com.charan.bingediary.data.remote.tmdb.dto.ShowResponseDto
import com.charan.bingediary.data.remote.tmdb.dto.PersonDetailsDto
import com.charan.bingediary.data.remote.tmdb.dto.SearchResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Single

@Single
class TmdbApi(
    val client: HttpClient
) {

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/"
    }

    private suspend inline fun <reified T> tmdbGet(
        path: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): T {
        return client.get("$BASE_URL$path") {
            parameter("api_key", BuildConfig.TMDB_API_KEY)
            block()
        }.body()
    }
    suspend fun trendingMovies(watchRegion: String, page: Int = 1): MovieResponseDto {
        return tmdbGet("discover/movie") {
            parameter("watch_region", watchRegion)
            parameter("sort_by", "popularity.desc")
            parameter("page", page)
        }
    }

    suspend fun trendingTvShows(watchRegion: String, page: Int = 1): ShowResponseDto {
        return tmdbGet("discover/tv") {
            parameter("watch_region", watchRegion)
            parameter("sort_by", "popularity.desc")
            parameter("page", page)
        }
    }

    suspend fun getMovieDetails(movieId: Long, language: String = "en-US"): MovieDetailsDto {
        return tmdbGet("movie/$movieId") {
            parameter("language", language)
            parameter("append_to_response", "credits")
        }
    }

    suspend fun getShowDetails(seriesId: Long, language: String = "en-US"): ShowDetailsDto {
        return tmdbGet("tv/$seriesId") {
            parameter("language", language)
            parameter("append_to_response", "credits")
        }
    }

    suspend fun getPersonDetails(personId: Long, language: String = "en-US"): PersonDetailsDto {
        return tmdbGet("person/$personId") {
            parameter("language", language)
            parameter("append_to_response", "combined_credits")
        }
    }

    suspend fun searchMulti(query: String, page: Int = 1): SearchResponseDto {
        return tmdbGet("search/multi") {
            parameter("query", query)
            parameter("page", page)
        }
    }
}
