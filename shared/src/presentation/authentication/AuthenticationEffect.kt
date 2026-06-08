package com.charan.bingediary.presentation.authentication

sealed interface AuthenticationEffect {
    data object NavigateToHome : AuthenticationEffect
    data class ShowToast(val message: String) : AuthenticationEffect
}