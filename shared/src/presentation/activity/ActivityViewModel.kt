package com.charan.bingediary.presentation.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charan.bingediary.data.repository.ReviewsRepository
import com.charan.bingediary.data.repository.TmdbRepository
import com.charan.bingediary.core.utils.toPosterUrl
import com.charan.bingediary.presentation.common.model.MediaType
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
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class ActivityViewModel(
    private val reviewsRepository: ReviewsRepository,
    private val tmdbRepository: TmdbRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ActivityUiState())
    val state: StateFlow<ActivityUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ActivityEffect>()
    val effect: SharedFlow<ActivityEffect> = _effect.asSharedFlow()

    init {
        onEvent(ActivityEvent.LoadActivity)
    }

    fun onEvent(event: ActivityEvent) {
        when (event) {
            ActivityEvent.LoadActivity -> loadActivity()
            ActivityEvent.ClearError -> _state.update { it.copy(error = null) }
        }
    }

    private fun loadActivity() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val rawReviews = reviewsRepository.getAllReviews()
                val sortedRawReviews = rawReviews.sortedByDescending { it.createdAt ?: "" }
                
                val mappedReviews = sortedRawReviews.map { review ->
                    async {
                        // 1. Try movie details
                        val movieResult = tmdbRepository.getMovieDetails(review.tmdbMovieId)
                        if (movieResult.isSuccess) {
                            val movie = movieResult.getOrNull()!!
                            ActivityReviewUiModel(
                                reviewId = review.id ?: "",
                                userId = review.userId,
                                username = review.userProfile?.username ?: "Binge Explorer",
                                userAvatarUrl = review.userProfile?.avatarUrl ?: "",
                                mediaId = review.tmdbMovieId,
                                mediaTitle = movie.title,
                                mediaPosterUrl = movie.posterPath?.toPosterUrl() ?: "",
                                rating = review.rating,
                                reviewText = review.reviewText,
                                mediaType = MediaType.MOVIE,
                                friendlyDate = formatCreatedAt(review.createdAt)
                            )
                        } else {
                            // 2. Try show details
                            val showResult = tmdbRepository.getShowDetails(review.tmdbMovieId)
                            if (showResult.isSuccess) {
                                val show = showResult.getOrNull()!!
                                ActivityReviewUiModel(
                                    reviewId = review.id ?: "",
                                    userId = review.userId,
                                    username = review.userProfile?.username ?: "Binge Explorer",
                                    userAvatarUrl = review.userProfile?.avatarUrl ?: "",
                                    mediaId = review.tmdbMovieId,
                                    mediaTitle = show.name,
                                    mediaPosterUrl = show.posterPath?.toPosterUrl() ?: "",
                                    rating = review.rating,
                                    reviewText = review.reviewText,
                                    mediaType = MediaType.TV_SHOW,
                                    friendlyDate = formatCreatedAt(review.createdAt)
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
                        reviews = mappedReviews
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load latest activity"
                    )
                }
            }
        }
    }

    private fun formatCreatedAt(createdAt: String?): String {
        if (createdAt == null) return ""
        return try {
            val datePart = createdAt.substringBefore("T") // "2026-06-14"
            val parts = datePart.split("-")
            if (parts.size != 3) return datePart
            val year = parts[0]
            val month = parts[1]
            val day = parts[2]
            val monthName = when (month) {
                "01" -> "Jan"
                "02" -> "Feb"
                "03" -> "Mar"
                "04" -> "Apr"
                "05" -> "May"
                "06" -> "Jun"
                "07" -> "Jul"
                "08" -> "Aug"
                "09" -> "Sep"
                "10" -> "Oct"
                "11" -> "Nov"
                "12" -> "Dec"
                else -> month
            }
            "$monthName $day, $year"
        } catch (e: Exception) {
            createdAt
        }
    }
}
