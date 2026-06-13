package com.charan.bingediary.core.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.annotation.Single

@Single
actual fun createHttpClientEngine(): HttpClientEngine {
    return Darwin.create()
}
