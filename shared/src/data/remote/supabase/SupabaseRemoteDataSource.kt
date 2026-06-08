package com.charan.bingediary.data.remote.supabase

import com.charan.bingediary.data.remote.model.UserDetailsDTO
import com.charan.bingediary.utils.ProcessState
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Single
class SupabaseRemoteDataSource(
    private val client : SupabaseClient
) {

    suspend fun loadSession() {
        client.auth.loadFromStorage()
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun authorizeUser(token : String) : ProcessState<Boolean> {
        return try {
            val rowNonce = Uuid.random().toString()
            client.auth.signInWith(IDToken) {
                provider = Google
                idToken = token
                nonce = rowNonce
            }
            ProcessState.Success(true)
        } catch (e: Exception){
            ProcessState.Error(e.message.toString())
        }
    }

    suspend fun signOutUser() : ProcessState<Boolean>{
        return try {
            client.auth.signOut()
            ProcessState.Success(true)
        } catch (e: Exception){
            ProcessState.Error(e.message.toString())
        }
    }

    suspend fun getUserDetails() : UserDetailsDTO? {
        try {
            val userMetaDataJson = client.auth.currentUserOrNull()?.userMetadata?.toString() ?: "{}"
            return Json.decodeFromString<UserDetailsDTO>(userMetaDataJson)
        } catch (e: Exception){
            return null
        }
    }
}