package com.charan.bingediary.presentation.authentication

sealed interface AuthenticationEvent {
    data class SignInWithGoogle(val code : String) : AuthenticationEvent
    data object SignInAsGuest : AuthenticationEvent
    data object ClearError : AuthenticationEvent
}