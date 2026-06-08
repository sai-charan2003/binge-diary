package com.charan.bingediary

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.charan.bingediary.config.BuildConfig
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import com.charan.bingediary.di.App
import org.koin.compose.KoinApplication
import org.koin.plugin.module.dsl.koinConfiguration
import com.charan.bingediary.presentation.navigation.NavigationHost

@Composable
fun Screen() {
    KoinApplication(configuration = koinConfiguration<App>()) {
        GoogleAuthProvider.create(credentials = GoogleAuthCredentials(serverId = BuildConfig.SUPABASE_KEY))
        MaterialTheme {
            NavigationHost()
        }
    }
}
