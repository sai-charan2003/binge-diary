package com.charan.bingediary.core.network

import io.ktor.client.engine.HttpClientEngine
import org.koin.core.annotation.Single


expect fun createHttpClientEngine(): HttpClientEngine
