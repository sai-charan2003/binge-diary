package com.charan.bingediary.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charan.bingediary.data.repository.AuthenticationRepository
import com.charan.bingediary.data.repository.ReviewsRepository
import com.charan.bingediary.data.repository.TmdbRepository
import com.charan.bingediary.core.utils.toPosterUrl
import com.charan.bingediary.presentation.common.model.MediaType
import com.charan.bingediary.data.remote.model.AccountInfo
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class ProfileViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val reviewsRepository: ReviewsRepository,
    private val tmdbRepository: TmdbRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ProfileEffect>()
    val effect: SharedFlow<ProfileEffect> = _effect.asSharedFlow()

    private var profileJob: Job? = null

    init {
        onEvent(ProfileEvent.LoadProfile)
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.LoadProfile -> loadProfile()
            ProfileEvent.SignOut -> signOut()
            ProfileEvent.ClearError -> _state.update { it.copy(error = "") }
        }
    }

    private fun loadProfile() {
        _state.update { it.copy(isLoading = true, error = "") }
        profileJob?.cancel()
        profileJob = viewModelScope.launch {
            try {
                val accountInfo = authenticationRepository.getUserDetails()
                if (accountInfo == null) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isAuthenticated = false,
                            accountInfo = AccountInfo(),
                            reviewedMovies = emptyList()
                        )
                    }
                    return@launch
                }
                
                // Sync reviews from remote to local DB
                reviewsRepository.syncUserReviews()

                reviewsRepository.getUserReviews().collect { reviews ->
                    val reviewedMovies = reviews.map { review ->
                        async {
                            // 1. Try movie details
                            val movieResult = tmdbRepository.getMovieDetails(review.tmdbMovieId)
                            if (movieResult.isSuccess) {
                                val movie = movieResult.getOrNull()!!
                                ReviewedMovieUiModel(
                                    mediaId = review.tmdbMovieId,
                                    title = movie.title,
                                    posterUrl = movie.posterPath?.toPosterUrl() ?: "",
                                    rating = review.rating,
                                    reviewText = review.reviewText,
                                    mediaType = MediaType.MOVIE
                                )
                            } else {
                                // 2. Try show details
                                val showResult = tmdbRepository.getShowDetails(review.tmdbMovieId)
                                if (showResult.isSuccess) {
                                    val show = showResult.getOrNull()!!
                                    ReviewedMovieUiModel(
                                        mediaId = review.tmdbMovieId,
                                        title = show.name,
                                        posterUrl = show.posterPath?.toPosterUrl() ?: "",
                                        rating = review.rating,
                                        reviewText = review.reviewText,
                                        mediaType = MediaType.TV_SHOW
                                    )
                                } else {
                                    null
                                }
                            }
                        }
                    }.awaitAll().filterNotNull()

                    _state.update {
                        it.copy(
                            isLoading = false,
                            isAuthenticated = true,
                            accountInfo = accountInfo,
                            reviewedMovies = reviewedMovies
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "Failed to load profile details") }
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = authenticationRepository.signOutUser()
            _state.update { it.copy(isLoading = false) }
            result.fold(
                onSuccess = {
                    _state.update {
                        it.copy(
                            isAuthenticated = false,
                            accountInfo = AccountInfo(),
                        )
                    }
                },
                onFailure = { error ->
                    _effect.emit(ProfileEffect.ShowToast(error.message ?: "Sign out failed"))
                }
            )
        }
    }
}
