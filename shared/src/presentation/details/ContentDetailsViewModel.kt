package com.charan.bingediary.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

import com.charan.bingediary.data.repository.TmdbRepository
import com.charan.bingediary.data.repository.AuthenticationRepository
import com.charan.bingediary.data.repository.UserMoviesRepository

import com.charan.bingediary.presentation.common.model.MediaType

import org.koin.core.annotation.InjectedParam

import com.charan.bingediary.core.utils.toBackdropUrl
import com.charan.bingediary.core.utils.toPosterUrl
import com.charan.bingediary.core.utils.toRatingString
import com.charan.bingediary.presentation.common.mapper.toContentDetailsUiModel
import com.charan.bingediary.presentation.details.model.ContentDetailsUiModel

@KoinViewModel
class ContentDetailsViewModel(
    private val tmdbRepository: TmdbRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val userMoviesRepository: UserMoviesRepository,
    @InjectedParam private val mediaId: Long,
    @InjectedParam private val mediaType: MediaType
) : ViewModel() {

    private val _state = MutableStateFlow(ContentDetailsState())
    val state: StateFlow<ContentDetailsState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ContentDetailsEffect>()
    val effect: SharedFlow<ContentDetailsEffect> = _effect.asSharedFlow()

    init {
        loadDetails(mediaId, mediaType)
    }

    fun onEvent(event: ContentDetailsEvent) {
        when (event) {
            ContentDetailsEvent.NavigateBack -> emitEffect(ContentDetailsEffect.NavigateBack)
            ContentDetailsEvent.ClearError -> clearError()
            is ContentDetailsEvent.NavigateToPersonDetail -> {
                emitEffect(ContentDetailsEffect.NavigateToPersonDetails(event.id))
            }
            ContentDetailsEvent.LogReviewClicked -> {
                viewModelScope.launch {
                    val user = authenticationRepository.getUserDetails()
                    if (user != null) {
                        _state.update { it.copy(showReviewBottomSheet = true) }
                    } else {
                        _state.update {
                            it.copy(
                                showAuthBottomSheet = true,
                                authBottomSheetTitle = "Sign in to review this content"
                            )
                        }
                    }
                }
            }
            ContentDetailsEvent.WatchlistClicked -> {
                viewModelScope.launch {
                    val user = authenticationRepository.getUserDetails()
                    if (user != null) {
                        val currentStatus = _state.value.isInWatchlist
                        if (currentStatus) {
                            val success = userMoviesRepository.removeFromWatchlist(mediaId)
                            if (success) {
                                _state.update { it.copy(isInWatchlist = false) }
                                emitEffect(ContentDetailsEffect.ShowToast("Removed from Watchlist!"))
                            } else {
                                emitEffect(ContentDetailsEffect.ShowToast("Failed to remove from watchlist"))
                            }
                        } else {
                            val success = userMoviesRepository.addToWatchlist(mediaId)
                            if (success) {
                                _state.update { it.copy(isInWatchlist = true) }
                                emitEffect(ContentDetailsEffect.ShowToast("Added to Watchlist!"))
                            } else {
                                emitEffect(ContentDetailsEffect.ShowToast("Failed to add to watchlist"))
                            }
                        }
                    } else {
                        _state.update {
                            it.copy(
                                showAuthBottomSheet = true,
                                authBottomSheetTitle = "Sign in to add to your watchlist"
                            )
                        }
                    }
                }
            }
            ContentDetailsEvent.DismissAuthBottomSheet -> {
                _state.update { it.copy(showAuthBottomSheet = false) }
            }
            ContentDetailsEvent.DismissReviewBottomSheet -> {
                _state.update { it.copy(showReviewBottomSheet = false) }
            }
            ContentDetailsEvent.AuthSuccess -> {
                _state.update { it.copy(showAuthBottomSheet = false) }
                emitEffect(ContentDetailsEffect.ShowToast("Successfully authenticated!"))
                viewModelScope.launch {
                    val isInWatchlist = userMoviesRepository.getWatchlistStatus(mediaId)
                    _state.update { it.copy(isInWatchlist = isInWatchlist) }
                }
            }
        }
    }

    private fun loadDetails(mediaId: Long, mediaType: MediaType) {
        _state.update { it.copy(isLoading = true, mediaId = mediaId, error = null) }
        
        viewModelScope.launch {
            val user = authenticationRepository.getUserDetails()
            if (user != null) {
                val isInWatchlist = userMoviesRepository.getWatchlistStatus(mediaId)
                _state.update { it.copy(isInWatchlist = isInWatchlist) }
            }
        }
        
        viewModelScope.launch {
            try {
                if (mediaType == MediaType.MOVIE) {
                    tmdbRepository.getMovieDetails(mediaId).fold(
                        onSuccess = { dto ->
                            _state.update { it.copy(isLoading = false, details = dto.toContentDetailsUiModel()) }
                        },
                        onFailure = { error ->
                            _state.update { it.copy(isLoading = false, error = error.message ?: "Failed to load movie details") }
                        }
                    )
                } else {
                    tmdbRepository.getShowDetails(mediaId).fold(
                        onSuccess = { dto ->

                            _state.update { it.copy(isLoading = false, details = dto.toContentDetailsUiModel()) }
                        },
                        onFailure = { error ->
                            _state.update { it.copy(isLoading = false, error = error.message ?: "Failed to load show details") }
                        }
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "An unexpected error occurred") }
            }
        }
    }

    private fun clearError() {
        _state.update { it.copy(error = null) }
    }

    private fun emitEffect(effect: ContentDetailsEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
