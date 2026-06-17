package com.charan.bingediary.presentation.logreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charan.bingediary.presentation.common.model.MediaType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import com.charan.bingediary.data.repository.ReviewsRepository
import com.charan.bingediary.data.repository.UserMediaRepository

@KoinViewModel
class LogReviewViewModel(
    private val reviewsRepository: ReviewsRepository,
    private val userMediaRepository: UserMediaRepository,
    @InjectedParam private val mediaId: Long,
    @InjectedParam private val mediaType: MediaType,
    @InjectedParam private val mediaTitle: String,
    @InjectedParam private val mediaYear: String
) : ViewModel() {

    private val mediaTypeStr = if (mediaType == MediaType.MOVIE) "movie" else "show"

    private val _state = MutableStateFlow(
        LogReviewUiState(
            title = mediaTitle,
            releaseYear = mediaYear
        )
    )
    val state: StateFlow<LogReviewUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LogReviewEffect>()
    val effect: SharedFlow<LogReviewEffect> = _effect.asSharedFlow()

    init {
        loadExistingReview()
    }

    private fun loadExistingReview() {
        viewModelScope.launch {
            val review = reviewsRepository.getUserReview(mediaId).firstOrNull()
            val userMovie = userMediaRepository.getUserMedia(mediaId, mediaTypeStr)
            _state.update {
                it.copy(
                    reviewId = review?.id,
                    userMovieId = userMovie?.id,
                    inWatchlist = userMovie?.inWatchlist ?: false,
                    isWatching = userMovie?.isWatching ?: false,
                    reviewText = review?.reviewText ?: "",
                    rating = review?.rating ?: 0.0f,
                    watched = userMovie?.isWatched ?: (review != null),
                    loved = userMovie?.isLoved ?: false
                )
            }
        }
    }

    fun onEvent(event: LogReviewEvent) {
        when (event) {
            LogReviewEvent.ToggleWatched -> {
                _state.update { it.copy(watched = !it.watched) }
            }
            LogReviewEvent.ToggleLoved -> {
                _state.update {
                    val newLoved = !it.loved
                    it.copy(
                        loved = newLoved,
                        watched = if (newLoved) true else it.watched
                    )
                }
            }
            is LogReviewEvent.SetRating -> {
                _state.update {
                    it.copy(
                        rating = event.rating,
                        watched = if (event.rating > 0.0f) true else it.watched
                    )
                }
            }
            is LogReviewEvent.ToggleTag -> {
                _state.update {
                    val newTags = if (it.selectedTags.contains(event.tag)) {
                        it.selectedTags - event.tag
                    } else {
                        it.selectedTags + event.tag
                    }
                    it.copy(selectedTags = newTags)
                }
            }
            is LogReviewEvent.UpdateReviewText -> {
                _state.update { it.copy(reviewText = event.text) }
            }
            LogReviewEvent.SaveReview -> {
                saveReview()
            }
        }
    }

    private fun saveReview() {
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }
            val success = reviewsRepository.saveReview(
                tmdbMovieId = mediaId,
                mediaType = mediaTypeStr,
                reviewId = _state.value.reviewId,
                userMovieId = _state.value.userMovieId,
                reviewText = _state.value.reviewText,
                rating = _state.value.rating,
                isWatched = _state.value.watched,
                inWatchlist = _state.value.inWatchlist,
                isWatching = _state.value.isWatching,
                isLoved = _state.value.loved
            )
            if (success) {
                _state.update { it.copy(isSaving = false, saveSuccess = true) }
                _effect.emit(LogReviewEffect.ShowToast("Review saved to your diary!"))
                _effect.emit(LogReviewEffect.NavigateBack)
            } else {
                _state.update { it.copy(isSaving = false, error = "Failed to save review") }
                _effect.emit(LogReviewEffect.ShowToast("Failed to save review"))
            }
        }
    }
}
