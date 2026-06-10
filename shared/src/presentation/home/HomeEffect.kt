package com.charan.bingediary.presentation.home

import com.charan.bingediary.presentation.common.model.MediaType

sealed class HomeEffect {
    data class NavigateToContentDetails(val mediaId: Long, val mediaType: MediaType) : HomeEffect()
    data class ShowToast(val message: String) : HomeEffect()
}
