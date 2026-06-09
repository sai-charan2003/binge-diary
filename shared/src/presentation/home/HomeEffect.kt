package com.charan.bingediary.presentation.home

sealed class HomeEffect {
    data class NavigateToMovieDetails(val movieId: Int) : HomeEffect()
    data class NavigateToShowDetails(val showId: Long) : HomeEffect()
    data class ShowToast(val message: String) : HomeEffect()
}
