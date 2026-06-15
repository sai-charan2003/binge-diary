package com.charan.bingediary.data.remote.supabase

import com.charan.bingediary.data.remote.model.UserDetailsDTO
import com.charan.bingediary.data.remote.model.UserMovieDto
import com.charan.bingediary.data.remote.model.ReviewDto
import com.charan.bingediary.utils.ProcessState
import com.charan.bingediary.core.USER_MOVIES_TABLE
import com.charan.bingediary.core.REVIEWS_TABLE
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
class SupabaseRemoteDataSource(
    private val client : SupabaseClient
) {

    suspend fun loadSession() {
        client.auth.loadFromStorage()
    }

    suspend fun authorizeUser(token: String): ProcessState<Boolean> {
        return try {
            client.auth.signInWith(IDToken) {
                provider = Google
                idToken = token
            }
            ProcessState.Success(true)
        } catch (e: Exception) {
            println(e.message)
            ProcessState.Error(e.message.toString())
        }
    }

    suspend fun signOutUser(): ProcessState<Boolean> {
        return try {
            client.auth.signOut()
            ProcessState.Success(true)
        } catch (e: Exception) {
            ProcessState.Error(e.message.toString())
        }
    }

    suspend fun getUserDetails(): UserDetailsDTO? {
        try {
            val userMetaDataJson = client.auth.currentUserOrNull()?.userMetadata?.toString() ?: return null
            return Json.decodeFromString<UserDetailsDTO>(userMetaDataJson)
        } catch (e: Exception) {
            return null
        }
    }

    fun getCurrentUserId(): String? {
        return client.auth.currentUserOrNull()?.id
    }

    suspend fun getUserMovie(userId: String, tmdbMovieId: Long): UserMovieDto? {
        return try {
            val list = client.postgrest.from(USER_MOVIES_TABLE)
                .select {
                    filter {
                        eq("user_id", userId)
                        eq("tmdb_movie_id", tmdbMovieId)
                    }
                }
                .decodeList<UserMovieDto>()
            list.firstOrNull()
        } catch (e: Exception) {
            println("Error fetching user movie: ${e.message}")
            null
        }
    }

    suspend fun upsertUserMovie(userMovie: UserMovieDto) {
        client.postgrest.from(USER_MOVIES_TABLE).upsert(userMovie) {
            onConflict = "id"
        }
    }

    suspend fun getUserReview(userId: String, tmdbMovieId: Long): ReviewDto? {
        return try {
            val list = client.postgrest.from(REVIEWS_TABLE)
                .select {
                    filter {
                        eq("user_id", userId)
                        eq("tmdb_movie_id", tmdbMovieId)
                    }
                }
                .decodeList<ReviewDto>()
            list.firstOrNull()
        } catch (e: Exception) {
            println("Error fetching user review: ${e.message}")
            null
        }
    }

    suspend fun upsertReview(review: ReviewDto) {
        client.postgrest.from(REVIEWS_TABLE).upsert(review) {
            onConflict = "id"
        }
    }
}