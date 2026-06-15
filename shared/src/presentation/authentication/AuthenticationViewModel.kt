package com.charan.bingediary.presentation.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charan.bingediary.data.repository.AuthenticationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class AuthenticationViewModel(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthenticationState())
    val state: StateFlow<AuthenticationState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AuthenticationEffect>()
    val effect: SharedFlow<AuthenticationEffect> = _effect.asSharedFlow()

    init {
        loadActiveSession()
    }

    fun onEvent(event: AuthenticationEvent) {
        when (event) {
            is AuthenticationEvent.SignInWithGoogle -> handleGoogleSignIn(event.code)
            is AuthenticationEvent.SignInAsGuest -> handleGuestSignIn()
            is AuthenticationEvent.ClearError -> clearError()
        }
    }

    private fun loadActiveSession() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                authenticationRepository.loadSession()
                val userDetails = authenticationRepository.getUserDetails()
                if (userDetails != null) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        isGuest = false,
                        userName = userDetails.name.ifBlank { "User" }
                    )
                    _effect.emit(AuthenticationEffect.NavigateToHome)
                } else {
                    _state.value = _state.value.copy(isLoading = false)
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    private fun handleGoogleSignIn(idToken: String) {
        viewModelScope.launch {
            if (idToken.isBlank()) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Sign in cancelled or invalid credentials"
                )
                return@launch
            }
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = authenticationRepository.authorizeUser(idToken)
            result.fold(
                onSuccess = {
                    val userDetails = authenticationRepository.getUserDetails()
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        isGuest = false,
                        userName = userDetails?.name ?: "Google User"
                    )
                    _effect.emit(AuthenticationEffect.NavigateToHome)
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message ?: "Sign in failed"
                    )
                    _effect.emit(AuthenticationEffect.ShowToast(error.message ?: "Sign in failed"))
                }
            )
        }
    }

    private fun handleGuestSignIn() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                // Simulate network latency for guest login
                delay(1000)
                _state.value = _state.value.copy(
                    isLoading = false,
                    isAuthenticated = true,
                    isGuest = true,
                    userName = "Guest User"
                )
                _effect.emit(AuthenticationEffect.NavigateToHome)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to sign in as Guest"
                )
                _effect.emit(AuthenticationEffect.ShowToast("Guest login failed"))
            }
        }
    }

    private fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
