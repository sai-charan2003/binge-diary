package com.charan.bingediary.presentation.person

import com.charan.bingediary.presentation.common.model.MediaType

sealed class PersonEffect {
    data object NavigateBack : PersonEffect()
    data class NavigateToContentDetails(val mediaId: Long, val mediaType: MediaType) : PersonEffect()
    data class ShowToast(val message: String) : PersonEffect()
}
