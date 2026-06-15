package com.charan.bingediary.presentation.logreview

sealed class LogReviewEvent {
    data object ToggleWatched : LogReviewEvent()
    data object ToggleLoved : LogReviewEvent()
    data class SetRating(val rating: Float) : LogReviewEvent()
    data class ToggleTag(val tag: String) : LogReviewEvent()
    data class UpdateReviewText(val text: String) : LogReviewEvent()
    data object SaveReview : LogReviewEvent()
}
