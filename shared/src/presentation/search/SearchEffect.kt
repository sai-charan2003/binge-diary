package com.charan.bingediary.presentation.search

import com.charan.bingediary.presentation.common.model.MediaType

sealed class SearchEffect {
    data class NavigateToContentDetails(val mediaId: Long, val mediaType: MediaType) : SearchEffect()
    data class NavigateToPersonDetails(val personId: Long) : SearchEffect()
    data class ShowToast(val message: String) : SearchEffect()
}
