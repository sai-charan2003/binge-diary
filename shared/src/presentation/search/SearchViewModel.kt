package com.charan.bingediary.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charan.bingediary.data.repository.TmdbRepository
import com.charan.bingediary.presentation.common.mapper.toMediaUIModel
import com.charan.bingediary.presentation.common.model.MediaType
import com.charan.bingediary.presentation.common.model.MediaUiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
class SearchViewModel(
    private val tmdbRepository: TmdbRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SearchEffect>()
    val effect: SharedFlow<SearchEffect> = _effect.asSharedFlow()

    private var searchJob: Job? = null
    private var allSearchResults: List<MediaUiModel> = emptyList()

    init {
//        loadTrendingRecommendations()
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.QueryChanged -> onQueryChanged(event.query)
            is SearchEvent.OnMediaClicked -> {
                if (event.mediaType == MediaType.PERSON) {
                    emitEffect(SearchEffect.NavigateToPersonDetails(event.mediaId))
                } else {
                    emitEffect(SearchEffect.NavigateToContentDetails(event.mediaId, event.mediaType))
                }
            }
            is SearchEvent.FilterChanged -> {
                _state.update { it.copy(selectedFilter = event.filter) }
                applyFilterAndShowResults()
            }
            SearchEvent.ClearSearch -> {
                onQueryChanged("")
            }
            SearchEvent.ClearError -> clearError()
        }
    }

    private fun onQueryChanged(newQuery: String) {
        _state.update { it.copy(query = newQuery, error = null) }

        searchJob?.cancel()
        if (newQuery.isBlank()) {
            allSearchResults = emptyList()
            _state.update { it.copy(searchResults = emptyList(), isLoading = false) }
            return
        }

        searchJob = viewModelScope.launch {
            performSearch(newQuery)
        }
    }

    private suspend fun performSearch(query: String) {
        _state.update { it.copy(isLoading = true, error = null) }
        val result = tmdbRepository.searchMulti(query)

        result.fold(
            onSuccess = { response ->
                allSearchResults = response.toMediaUIModel()
                applyFilterAndShowResults()
            },
            onFailure = { error ->
                allSearchResults = emptyList()
                _state.update {
                    it.copy(
                        searchResults = emptyList(),
                        isLoading = false,
                        error = error.message ?: "Failed to perform search"
                    )
                }
                emitEffect(SearchEffect.ShowToast(error.message ?: "Search failed"))
            }
        )
    }

    private fun applyFilterAndShowResults() {
        val currentFilter = _state.value.selectedFilter
        val filteredResults = when (currentFilter) {
            SearchFilter.ALL -> allSearchResults
            SearchFilter.MOVIES -> allSearchResults.filter { it.mediaType == MediaType.MOVIE }
            SearchFilter.TV_SHOWS -> allSearchResults.filter { it.mediaType == MediaType.TV_SHOW }
            SearchFilter.PEOPLE -> allSearchResults.filter { it.mediaType == MediaType.PERSON }
        }
        _state.update {
            it.copy(
                searchResults = filteredResults,
                isLoading = false
            )
        }
    }

//    private fun loadTrendingRecommendations() {
//        _state.update { it.copy(isTrendingLoading = true) }
//        viewModelScope.launch {
//            val result = tmdbRepository.getTrendingMovies(watchRegion = "IN")
//            result.fold(
//                onSuccess = { response ->
//                    _state.update {
//                        it.copy(
//                            trendingItems = response.toMediaUIModel().take(10),
//                            isTrendingLoading = false
//                        )
//                    }
//                },
//                onFailure = {
//                    _state.update { it.copy(isTrendingLoading = false) }
//                }
//            )
//        }
//    }

    private fun clearError() {
        _state.update { it.copy(error = null) }
    }

    private fun emitEffect(effect: SearchEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
