package com.charan.bingediary.core.utils

fun String.toPosterUrl(): String =
    "https://image.tmdb.org/t/p/w500$this"

fun String.toBackdropUrl(): String =
    "https://image.tmdb.org/t/p/w500$this"


fun Double.toRatingString(): String {
    val rounded = (this * 10).toInt() / 10.0
    return rounded.toString()
}