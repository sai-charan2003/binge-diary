package com.charan.bingediary.presentation.profile

import com.charan.bingediary.data.remote.model.AccountInfo
import com.charan.bingediary.presentation.common.model.MediaType

data class ProfileUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val accountInfo: AccountInfo = AccountInfo(),
    val bio: String = "",
    val reviewedMovies: List<ReviewedMovieUiModel> = emptyList(),
    val error: String = ""
)

data class ReviewedMovieUiModel(
    val mediaId: Long,
    val title: String,
    val posterUrl: String,
    val rating: Float,
    val reviewText: String,
    val mediaType: MediaType
)
