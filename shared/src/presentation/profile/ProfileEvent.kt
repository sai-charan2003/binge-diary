package com.charan.bingediary.presentation.profile

sealed class ProfileEvent {
    data object LoadProfile : ProfileEvent()
    data object SignOut : ProfileEvent()
    data object ClearError : ProfileEvent()
}
