package com.charan.bingediary.presentation.home

sealed class HomeEvent {
    data object LoadTrending : HomeEvent()
    data class OnMovieClicked(val movieId: Int) : HomeEvent()
    data class OnShowClicked(val showId: Long) : HomeEvent()
    data object ClearError : HomeEvent()
}
