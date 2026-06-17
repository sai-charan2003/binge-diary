package com.charan.bingediary.data.local.mapper

import com.charan.bingediary.data.local.entity.ReviewEntity
import com.charan.bingediary.data.local.entity.UserMediaEntity
import com.charan.bingediary.data.remote.model.ReviewDto
import com.charan.bingediary.data.remote.model.UserMovieDto

fun ReviewDto.toEntity(): ReviewEntity {
    return ReviewEntity(
        id = id ?: "",
        userId = userId,
        tmdbMovieId = tmdbMovieId,
        reviewText = reviewText,
        rating = rating,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun ReviewEntity.toDto(): ReviewDto {
    return ReviewDto(
        id = id,
        userId = userId,
        tmdbMovieId = tmdbMovieId,
        reviewText = reviewText,
        rating = rating,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun UserMovieDto.toEntity(): UserMediaEntity {
    return UserMediaEntity(
        id = id ?: "",
        userId = userId,
        tmdbMovieId = tmdbMovieId,
        inWatchlist = inWatchlist,
        isWatching = isWatching,
        isWatched = isWatched,
        isLoved = isLoved,
        rating = rating,
        addedAt = addedAt,
        watchedAt = watchedAt
    )
}
