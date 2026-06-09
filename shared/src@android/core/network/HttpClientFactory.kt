package com.charan.bingediary.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.annotation.Single

@Single
actual fun createHttpClientEngine(): HttpClientEngine {
    return OkHttp.create()
}
