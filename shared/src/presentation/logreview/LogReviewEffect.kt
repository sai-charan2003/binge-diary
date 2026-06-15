package com.charan.bingediary.presentation.logreview

sealed class LogReviewEffect {
    data object NavigateBack : LogReviewEffect()
    data class ShowToast(val message: String) : LogReviewEffect()
}
