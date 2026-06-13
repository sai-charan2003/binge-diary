package com.charan.bingediary.presentation.common.model

import kotlinx.serialization.Serializable

@Serializable
enum class MediaType(val displayName: String) {
    MOVIE("Movie"),
    TV_SHOW("TV Show"),
    PERSON("Cast & Crew")
}
