package com.charan.bingediary.data.repository

import com.charan.bingediary.data.remote.model.AccountInfo
import com.charan.bingediary.utils.ProcessState

interface AuthenticationRepository {
    suspend fun loadSession()

    suspend fun authorizeUser(token : String) : ProcessState<Boolean>

    suspend fun signOutUser() : ProcessState<Boolean>

    suspend fun getUserDetails() : AccountInfo?
}