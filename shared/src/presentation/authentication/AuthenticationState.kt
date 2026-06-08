package com.charan.bingediary.presentation.authentication

data class AuthenticationState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false,
    val isGuest: Boolean = false,
    val userName: String? = null
)
