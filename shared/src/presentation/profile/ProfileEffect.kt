package com.charan.bingediary.presentation.profile

sealed class ProfileEffect {
    data object NavigateToAuth : ProfileEffect()
    data class ShowToast(val message: String) : ProfileEffect()
}
