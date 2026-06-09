package com.charan.bingediary.presentation.home

import com.charan.bingediary.data.remote.tmdb.dto.MovieDto
import com.charan.bingediary.data.remote.tmdb.dto.ShowDto
import com.charan.bingediary.presentation.common.model.MediaUiModel

data class HomeState(
    val isLoading: Boolean = false,
    val sections : List<HomeSection> = emptyList(),
    val error: String? = null
)

data class HomeSection(
    val index: Int,
    val title : String,
    val items : List<MediaUiModel> = emptyList(),
    val isLoading : Boolean = false,
)




