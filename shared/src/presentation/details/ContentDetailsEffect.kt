package com.charan.bingediary.presentation.details

sealed class ContentDetailsEffect {
    data object NavigateBack : ContentDetailsEffect()
    data class ShowToast(val message: String) : ContentDetailsEffect()
}
