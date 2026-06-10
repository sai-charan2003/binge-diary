package com.charan.bingediary.presentation.person.model

import com.charan.bingediary.core.utils.toPosterUrl
import com.charan.bingediary.data.remote.tmdb.dto.PersonDetailsDto
import com.charan.bingediary.presentation.common.model.MediaType

data class PersonUiModel(
    val id: Long = 0,
    val name: String = "",
    val biography: String = "",
    val profileUrl: String = "",
    val knownForDepartment: String = "",
    val creditsByDepartment: Map<String, List<CreditUiModel>> = emptyMap()
)

data class CreditUiModel(
    val id: Long = 0,
    val title: String = "",
    val posterUrl: String = "",
    val role: String = "",
    val mediaType: MediaType = MediaType.MOVIE,
    val releaseYear: String = ""
)

