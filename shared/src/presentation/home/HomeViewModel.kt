package com.charan.bingediary.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charan.bingediary.data.repository.TmdbRepository
import com.charan.bingediary.presentation.common.mapper.toMediaUIModel
import com.charan.bingediary.presentation.common.model.MediaUiModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val tmdbRepository: TmdbRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HomeEffect>()
    val effect: SharedFlow<HomeEffect> = _effect.asSharedFlow()

    init {
        onEvent(HomeEvent.LoadTrending)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.LoadTrending -> loadTrending()
            is HomeEvent.OnMediaClicked -> emitEffect(
                HomeEffect.NavigateToContentDetails(event.mediaId, event.mediaType)
            )
            HomeEvent.ClearError -> clearError()
        }
    }

    private fun loadTrending() {
        _state.update { state ->
            val loadingSections = listOf(
                HomeSection(index = 0, title = "Trending Movies", isLoading = true),
                HomeSection(index = 1, title = "Trending TV Shows", isLoading = true)
            )
            state.copy(sections = loadingSections, isLoading = false)
        }

        viewModelScope.launch {
            fetchMovies()
        }

        viewModelScope.launch {
            fetchShows()
        }
    }

    private suspend fun fetchMovies() {
        val result = tmdbRepository.getTrendingMovies(watchRegion = "IN")

        result.fold(
            onSuccess = { response ->
                updateSection(
                    index = 0,
                    title = "Trending Movies",
                    items = response.toMediaUIModel(),
                    isLoading = false
                )
            },
            onFailure = { error ->
                updateSection(
                    index = 0,
                    title = "Trending Movies",
                    items = emptyList(),
                    isLoading = false
                )
                updateError(error.message)
                emitEffect(HomeEffect.ShowToast(error.message ?: "Failed to load movies"))
            }
        )
    }

    private suspend fun fetchShows() {
        val result = tmdbRepository.getTrendingTvShows(watchRegion = "IN")

        result.fold(
            onSuccess = { response ->
                updateSection(
                    index = 1,
                    title = "Trending TV Shows",
                    items = response.toMediaUIModel(),
                    isLoading = false
                )
            },
            onFailure = { error ->
                updateSection(
                    index = 1,
                    title = "Trending TV Shows",
                    items = emptyList(),
                    isLoading = false
                )
                updateError(error.message)
                emitEffect(HomeEffect.ShowToast(error.message ?: "Failed to load TV shows"))
            }
        )
    }

    private fun updateSection(
        index: Int,
        title: String,
        items: List<MediaUiModel>,
        isLoading: Boolean
    ) {
        _state.update { state ->
            val newSection = HomeSection(
                index = index,
                title = title,
                items = items,
                isLoading = isLoading
            )

            val updatedSections = state.sections
                .filterNot { it.title == title }
                .plus(newSection)
                .sortedBy { it.index }

            state.copy(
                sections = updatedSections,
            )
        }
    }

    private fun handleLoadingState(isLoading : Boolean) {
        _state.update {
            it.copy(
                isLoading = isLoading,
            )
        }
    }

    private fun clearError() {
        _state.update { it.copy(error = null) }
    }

    private fun updateError(message: String?) {
        _state.update { it.copy(error = message) }
    }

    private fun emitEffect(effect: HomeEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
