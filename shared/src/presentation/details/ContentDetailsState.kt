package com.charan.bingediary.presentation.details

import com.charan.bingediary.presentation.details.model.ContentDetailsUiModel

data class ContentDetailsState(
    val isLoading: Boolean = false,
    val mediaId: Long? = null,
    val details: ContentDetailsUiModel = ContentDetailsUiModel(),
    val error: String? = null
)
