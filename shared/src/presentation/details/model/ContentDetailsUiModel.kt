package com.charan.bingediary.presentation.details.model

data class ContentDetailsUiModel(
    val title: String = "",
    val overview: String = "",
    val posterUrl: String = "",
    val backdropUrl: String = "",
    val releaseYear: String = "",
    val runtime: String = "",
    val rating: String = "",
    val directors : List<CastCrewUiModel> = emptyList(),
    val writers : List<CastCrewUiModel> = emptyList(),
    val cast: List<CastCrewUiModel> = emptyList(),
    val producers: List<CastCrewUiModel> = emptyList()
)

data class CastCrewUiModel(
    val id: Long = 0,
    val name: String = "",
    val character: String = "",
    val profileUrl: String = ""
)
