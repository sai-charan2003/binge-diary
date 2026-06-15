package com.charan.bingediary.data.repository

import com.charan.bingediary.data.remote.model.AccountInfo

interface AuthenticationRepository {
    suspend fun loadSession()

    suspend fun authorizeUser(token : String) : Result<Boolean>

    suspend fun signOutUser() : Result<Boolean>

    suspend fun getUserDetails() : AccountInfo?
}