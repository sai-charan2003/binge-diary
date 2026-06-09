package com.charan.bingediary.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.entryProvider
import com.charan.bingediary.presentation.authentication.AuthenticationScreen
import com.charan.bingediary.presentation.home.HomeScreen
import kotlinx.serialization.Serializable

sealed class NavigationDestination : NavKey{
    @Serializable
    data object Authentication : NavigationDestination()
    @Serializable
    data class Home(val userName: String, val isGuest: Boolean) : NavigationDestination()
}

@Composable
fun NavigationHost() {
    val backStack = rememberSaveable() { mutableStateListOf<Any>(NavigationDestination.Home("",false)) }

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<NavigationDestination.Authentication> {
                AuthenticationScreen(
                    onNavigateToHome = {
                        backStack.add(NavigationDestination.Home(userName = "Binge Explorer", isGuest = false))
                    }
                )
            }
            entry<NavigationDestination.Home> { dest ->
                HomeScreen(

                )
            }
        }
    )
}