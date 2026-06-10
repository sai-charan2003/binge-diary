package com.charan.bingediary.presentation.details

import com.charan.bingediary.presentation.common.model.MediaType

sealed class ContentDetailsEvent {
    data object NavigateBack : ContentDetailsEvent()
    data object ClearError : ContentDetailsEvent()

    data class NavigateToPersonDetail(val id : Long) : ContentDetailsEvent()
}
