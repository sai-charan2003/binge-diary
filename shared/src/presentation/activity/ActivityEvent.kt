package com.charan.bingediary.presentation.activity

sealed class ActivityEvent {
    data object LoadActivity : ActivityEvent()
    data object ClearError : ActivityEvent()
}
