package com.charan.bingediary.data.repository.impl

import com.charan.bingediary.data.remote.supabase.SupabaseRemoteDataSource
import com.charan.bingediary.data.remote.model.AccountInfo
import com.charan.bingediary.data.repository.AuthenticationRepository
import org.koin.core.annotation.Singleton

@Singleton(binds = [AuthenticationRepository::class])
class AuthenticationRepositoryImpl(
    private val supabaseRemoteDataSource: SupabaseRemoteDataSource
) : AuthenticationRepository {

    override suspend fun loadSession() {
        supabaseRemoteDataSource.loadSession()
    }

    override suspend fun authorizeUser(token: String): Result<Boolean> {
        return supabaseRemoteDataSource.authorizeUser(token)
    }

    override suspend fun signOutUser(): Result<Boolean> {
        return supabaseRemoteDataSource.signOutUser()
    }

    override suspend fun getUserDetails(): AccountInfo? {
        return try {
            val user = supabaseRemoteDataSource.getUserDetails() ?: return null
            user.let {
                AccountInfo(
                    name = it.name,
                    email = it.email,
                    profilePicUrl = it.avatarUrl
                )
            }
        } catch (e: Exception) {
            println(e)
            null
        }
    }
}
