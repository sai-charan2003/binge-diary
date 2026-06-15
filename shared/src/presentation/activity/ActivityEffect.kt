package com.charan.bingediary.presentation.activity

sealed class ActivityEffect {
    data class ShowToast(val message: String) : ActivityEffect()
}
