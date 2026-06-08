package com.charan.bingediary.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.charan.bingediary.presentation.authentication.AuthenticationScreen
import com.charan.bingediary.presentation.home.HomeScreen

sealed interface NavigationDestination {
    data object Authentication : NavigationDestination
    data class Home(val userName: String, val isGuest: Boolean) : NavigationDestination
}

@Composable
fun NavigationHost() {
    var currentDestination by remember { mutableStateOf<NavigationDestination>(NavigationDestination.Authentication) }

    when (val dest = currentDestination) {
        is NavigationDestination.Authentication -> {
            AuthenticationScreen(
                onNavigateToHome = {
                    // Navigate to Home destination.
                    // We can pass placeholder info that matches what was selected or defaults.
                    currentDestination = NavigationDestination.Home(userName = "Binge Explorer", isGuest = false)
                }
            )
        }
        is NavigationDestination.Home -> {
            HomeScreen(
                userName = dest.userName,
                isGuest = dest.isGuest,
                onLogout = {
                    currentDestination = NavigationDestination.Authentication
                }
            )
        }
    }
}