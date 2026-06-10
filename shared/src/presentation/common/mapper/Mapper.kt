package com.charan.bingediary.presentation.common.mapper

import com.charan.bingediary.core.DIRECTOR_JOB
import com.charan.bingediary.core.PRODUCER_JOB
import com.charan.bingediary.core.utils.toBackdropUrl
import com.charan.bingediary.core.utils.toPosterUrl
import com.charan.bingediary.core.utils.toRatingString
import com.charan.bingediary.core.utils.toRuntimeString
import com.charan.bingediary.data.remote.tmdb.dto.CastDto
import com.charan.bingediary.data.remote.tmdb.dto.CrewDto
import com.charan.bingediary.data.remote.tmdb.dto.MovieDetailsDto
import com.charan.bingediary.data.remote.tmdb.dto.MovieResponseDto
import com.charan.bingediary.data.remote.tmdb.dto.ShowDetailsDto
import com.charan.bingediary.data.remote.tmdb.dto.CreatedByDto
import com.charan.bingediary.data.remote.tmdb.dto.ShowResponseDto
import com.charan.bingediary.presentation.common.model.MediaUiModel


import com.charan.bingediary.presentation.common.model.MediaType
import com.charan.bingediary.presentation.details.model.CastCrewUiModel
import com.charan.bingediary.presentation.details.model.ContentDetailsUiModel
import kotlin.jvm.JvmName

fun MovieResponseDto.toMediaUIModel() : List<MediaUiModel> {
    val movieDto = this.results
    return movieDto.map {
        MediaUiModel(
            id = it.id,
            title = it.title,
            posterPath = it.posterPath?.toPosterUrl() ?: "",
            backdropPath = it.backdropPath?.toBackdropUrl() ?: "",
            rating = it.voteAverage.toRatingString(),
            mediaType = MediaType.MOVIE
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
            rating = it.voteAverage.toRatingString(),
            mediaType = MediaType.TV_SHOW
        )
    }
}


fun MovieDetailsDto.toContentDetailsUiModel() : ContentDetailsUiModel{
    return ContentDetailsUiModel(
        title = this.title,
        overview = this.overview,
        posterUrl = this.posterPath?.toPosterUrl() ?: "",
        backdropUrl = this.backdropPath?.toBackdropUrl() ?: "",
        rating = this.voteAverage.toRatingString(),
        runtime = this.runtime?.toRuntimeString() ?: "",
        releaseYear = this.releaseDate?.substringBefore("-") ?: "",
        directors = this.credits?.crew?.filter { it.job == DIRECTOR_JOB }?.toCastCrewUiModel() ?: emptyList(),
        writers = this.credits?.crew?.filter { it.job == "Writer" || it.job == "Screenplay" || it.job == "Story" }?.toCastCrewUiModel() ?: emptyList(),
        producers = this.credits?.crew?.filter { it.job == PRODUCER_JOB }?.toCastCrewUiModel() ?: emptyList(),
        cast = this.credits?.cast?.toCastCrewUiModel() ?: emptyList()

    )

}

fun ShowDetailsDto.toContentDetailsUiModel() : ContentDetailsUiModel {
    return ContentDetailsUiModel(
        title = this.name,
        overview = this.overview,
        posterUrl = this.posterPath?.toPosterUrl() ?: "",
        backdropUrl = this.backdropPath?.toBackdropUrl() ?: "",
        rating = this.voteAverage.toRatingString(),
        runtime = this.episodeRunTime?.firstOrNull()?.toRuntimeString() ?: "",
        releaseYear = this.firstAirDate?.substringBefore("-") ?: "",
        directors = this.createdBy?.toCastCrewUiModel() ?: emptyList(),
        writers = this.credits?.crew?.filter { it.job == "Writer" || it.job == "Screenplay" || it.job == "Story" }?.toCastCrewUiModel() ?: emptyList(),
        producers = this.credits?.crew?.filter { it.job == PRODUCER_JOB }?.toCastCrewUiModel() ?: emptyList(),
        cast = this.credits?.cast?.toCastCrewUiModel() ?: emptyList()
    )
}

fun CrewDto.toCastCrewUiModel() : CastCrewUiModel {
    return CastCrewUiModel(
        id = this.id,
        name = this.name,
        profileUrl = this.profilePath?.toPosterUrl() ?: "",
        character = this.job ?: ""
    )
}

@JvmName("crewToCastCrewUiModels")
fun List<CrewDto>.toCastCrewUiModel(): List<CastCrewUiModel> {
    return this.map { it.toCastCrewUiModel() }

}

fun CastDto.toCastCrewUiModel() : CastCrewUiModel {
    return CastCrewUiModel(
        id = this.id,
        name = this.name,
        profileUrl = this.profilePath?.toPosterUrl() ?: "",
        character = this.character ?: ""
    )

}

@JvmName("castToCastCrewUiModels")
fun List<CastDto>.toCastCrewUiModel(): List<CastCrewUiModel> {
    return this.map { it.toCastCrewUiModel() }
}

fun CreatedByDto.toCastCrewUiModel() : CastCrewUiModel {
    return CastCrewUiModel(
        id = this.id,
        name = this.name,
        profileUrl = this.profilePath?.toPosterUrl() ?: "",
        character = "Creator"
    )
}

@JvmName("createdByToCastCrewUiModels")
fun List<CreatedByDto>.toCastCrewUiModel(): List<CastCrewUiModel> {
    return this.map { it.toCastCrewUiModel() }
}
