package com.charan.bingediary.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.charan.bingediary.presentation.authentication.AuthenticationScreen
import com.charan.bingediary.presentation.home.HomeScreen
import com.charan.bingediary.presentation.details.ContentDetailsScreen
import com.charan.bingediary.presentation.person.PersonDetailsScreen
import kotlinx.serialization.Serializable

import com.charan.bingediary.presentation.common.model.MediaType

sealed class NavigationDestination : NavKey{
    @Serializable
    data object Authentication : NavigationDestination()
    @Serializable
    data class Home(val userName: String, val isGuest: Boolean) : NavigationDestination()
    @Serializable
    data class ContentDetails(val mediaId: Long, val mediaType: MediaType) : NavigationDestination()
    @Serializable
    data class Person(val personId: Long) : NavigationDestination()
}

@Composable
fun NavigationHost() {
    val backStack = rememberSaveable() { mutableStateListOf<Any>(NavigationDestination.Home("",false)) }

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
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
                    onNavigateToContentDetails = { mediaId, mediaType ->
                        backStack.add(NavigationDestination.ContentDetails(mediaId, mediaType))
                    }
                )
            }
            entry<NavigationDestination.ContentDetails> { dest ->
                ContentDetailsScreen(
                    mediaId = dest.mediaId,
                    mediaType = dest.mediaType,
                    onNavigateBack = {
                        if (backStack.size > 1) {
                            backStack.removeLast()
                        }
                    },
                    onNavigateToPerson = { personId ->
                        backStack.add(NavigationDestination.Person(personId))
                    }
                )
            }
            entry<NavigationDestination.Person> { dest ->
                PersonDetailsScreen(
                    personId = dest.personId,
                    onNavigateBack = {
                        if (backStack.size > 1) {
                            backStack.removeLast()
                        }
                    },
                    onNavigateToContentDetails = { mediaId, mediaType ->
                        backStack.add(NavigationDestination.ContentDetails(mediaId, mediaType))
                    }
                )
            }
        }
    )
}