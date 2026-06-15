package com.charan.bingediary.presentation.activity

import com.charan.bingediary.presentation.common.model.MediaType

data class ActivityUiState(
    val isLoading: Boolean = false,
    val reviews: List<ActivityReviewUiModel> = emptyList(),
    val error: String? = null
)

data class ActivityReviewUiModel(
    val reviewId: String,
    val userId: String,
    val username: String,
    val userAvatarUrl: String,
    val mediaId: Long,
    val mediaTitle: String,
    val mediaPosterUrl: String,
    val rating: Float,
    val reviewText: String,
    val mediaType: MediaType,
    val friendlyDate: String
)
