package com.charan.bingediary.presentation.person

import com.charan.bingediary.presentation.person.model.PersonUiModel

data class PersonState(
    val isLoading: Boolean = true,
    val person: PersonUiModel? = null,
    val errorMessage: String? = null
)
