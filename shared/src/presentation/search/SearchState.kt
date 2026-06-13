package com.charan.bingediary.presentation.search

import com.charan.bingediary.presentation.common.model.MediaUiModel

enum class SearchFilter(val displayName: String) {
    ALL("All"),
    MOVIES("Movies"),
    TV_SHOWS("TV Shows"),
    PEOPLE("Cast & Crew")
}

data class SearchState(
    val query: String = "",
    val searchResults: List<MediaUiModel> = emptyList(),
    val trendingItems: List<MediaUiModel> = emptyList(),
    val selectedFilter: SearchFilter = SearchFilter.ALL,
    val isLoading: Boolean = false,
    val isTrendingLoading: Boolean = false,
    val error: String? = null
)
