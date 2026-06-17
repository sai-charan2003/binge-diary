package com.charan.bingediary.presentation.home

import com.charan.bingediary.presentation.common.model.MediaType

sealed class HomeEvent {
    data object LoadTrending : HomeEvent()
    data class OnMediaClicked(val mediaId: Long, val mediaType: MediaType) : HomeEvent()
    data object ClearError : HomeEvent()

    data object Refresh : HomeEvent()
}
