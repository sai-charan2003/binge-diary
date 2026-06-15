package com.charan.bingediary.presentation.logreview

data class LogReviewUiState(
    val title: String = "",
    val releaseYear: String = "",
    val watched: Boolean = false,
    val loved: Boolean = false,
    val rating: Float = 0.0f,
    val selectedTags: Set<String> = emptySet(),
    val availableTags: List<String> = listOf("Masterpiece", "Visual Feast", "Slow Burn", "Emotional", "Action-Packed", "Suspenseful"),
    val reviewText: String = "",
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null,
    val reviewId: String? = null,
    val userMovieId: String? = null,
    val inWatchlist: Boolean = false,
    val isWatching: Boolean = false
)
