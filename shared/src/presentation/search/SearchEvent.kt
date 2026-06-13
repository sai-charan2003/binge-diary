package com.charan.bingediary.presentation.search

import com.charan.bingediary.presentation.common.model.MediaType

sealed class SearchEvent {
    data class QueryChanged(val query: String) : SearchEvent()
    data class OnMediaClicked(val mediaId: Long, val mediaType: MediaType) : SearchEvent()
    data class FilterChanged(val filter: SearchFilter) : SearchEvent()
    data object ClearSearch : SearchEvent()
    data object ClearError : SearchEvent()
}
