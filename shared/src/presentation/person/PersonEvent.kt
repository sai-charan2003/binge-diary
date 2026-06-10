package com.charan.bingediary.presentation.person

import com.charan.bingediary.presentation.common.model.MediaType

sealed class PersonEvent {
    data class LoadPerson(val personId: Long) : PersonEvent()
    data object NavigateBack : PersonEvent()
    data class OnCreditClick(val mediaId: Long, val mediaType: MediaType) : PersonEvent()
}
