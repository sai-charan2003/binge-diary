package com.charan.bingediary.presentation.common.mapper

import com.charan.bingediary.core.utils.toBackdropUrl
import com.charan.bingediary.core.utils.toPosterUrl
import com.charan.bingediary.core.utils.toRatingString
import com.charan.bingediary.data.remote.tmdb.dto.MovieResponseDto
import com.charan.bingediary.data.remote.tmdb.dto.ShowResponseDto
import com.charan.bingediary.presentation.common.model.MediaUiModel


fun MovieResponseDto.toMediaUIModel() : List<MediaUiModel> {
    val movieDto = this.results
    return movieDto.map {
        MediaUiModel(
            id = it.id,
            title = it.title,
            posterPath = it.posterPath?.toPosterUrl() ?: "",
            backdropPath = it.backdropPath?.toBackdropUrl() ?: "",
            rating = it.voteAverage.toRatingString()
        )
    }
}

fun ShowResponseDto.toMediaUIModel() : List<MediaUiModel> {
    val movieDto = this.results
    return movieDto.map {
        MediaUiModel(
            id = it.id,
            title = it.name,
            posterPath = it.posterPath?.toPosterUrl() ?: "",
            backdropPath = it.backdropPath?.toBackdropUrl() ?: "",
            rating = it.voteAverage.toRatingString()
        )
    }
}
