package com.charan.bingediary.presentation.navigation.bottom

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search

sealed class BottomNavItem : NavKey {
    abstract val title: String
    abstract val selectedIcon: ImageVector
    abstract val unselectedIcon: ImageVector

    @Serializable
    data object Home : BottomNavItem() {
        override val title = "Home"
        override val selectedIcon = Icons.Rounded.Home
        override val unselectedIcon = Icons.Outlined.Home
    }

    @Serializable
    data object Activity : BottomNavItem() {
        override val title = "Activity"
        override val selectedIcon = Icons.Rounded.List
        override val unselectedIcon = Icons.Outlined.List
    }

    @Serializable
    data object Profile : BottomNavItem() {
        override val title = "Profile"
        override val selectedIcon = Icons.Rounded.Person
        override val unselectedIcon = Icons.Outlined.Person
    }

    @Serializable
    data object Search : BottomNavItem() {
        override val title = "Search"
        override val selectedIcon = Icons.Rounded.Search
        override val unselectedIcon = Icons.Outlined.Search
    }

    companion object {
        val entries: List<BottomNavItem> get() = listOf(Home, Activity, Profile, Search)
    }
}
