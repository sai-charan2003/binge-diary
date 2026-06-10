package com.charan.bingediary.core.utils

fun String.toPosterUrl(): String =
    "https://image.tmdb.org/t/p/w500$this"

fun String.toBackdropUrl(): String =
    "https://image.tmdb.org/t/p/w500$this"


fun Double.toRatingString(): String {
    val rounded = (this * 10).toInt() / 10.0
    return rounded.toString()
}

fun Int.toRuntimeString(): String {
    val hours = this / 60
    val minutes = this % 60
    return if (hours > 0) {
        "${hours}h ${minutes}m"
    } else {
        "${minutes}m"
    }
}