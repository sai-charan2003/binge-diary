package com.charan.bingediary.presentation.navigation.bottomNav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.animation.Crossfade
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.charan.bingediary.presentation.activity.ActivityScreen
import com.charan.bingediary.presentation.common.model.MediaType
import com.charan.bingediary.presentation.home.HomeScreen
import com.charan.bingediary.presentation.profile.ProfileScreen
import com.charan.bingediary.presentation.search.SearchScreen

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.mutableStateListOf
import com.charan.bingediary.presentation.navigation.bottom.BottomNavItem

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BottomNavHost(
    onNavigateToContentDetails: (Long, MediaType) -> Unit,
    onNavigateToPerson: (Long) -> Unit,
    onNavigateToAuth: () -> Unit,
) {
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    val backStack = rememberSaveable { mutableStateListOf<Any>(BottomNavItem.Home) }
    val layoutDirection = LocalLayoutDirection.current
    val cutoutInsets = WindowInsets.displayCutout.asPaddingValues()
    var previousSelectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    val forward = selectedItemIndex > previousSelectedItemIndex


    val selectedItem = BottomNavItem.entries[selectedItemIndex]

    val navigateTo: (BottomNavItem) -> Unit = { item ->
        if(selectedItem != item) {
            previousSelectedItemIndex = selectedItemIndex

            selectedItemIndex = BottomNavItem.entries.indexOf(item)

            if (item == BottomNavItem.Home) {
                backStack.clear()
                backStack.add(BottomNavItem.Home)
            } else {
                backStack.clear()
                backStack.add(BottomNavItem.Home)
                backStack.add(item)
            }
        }
    }

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = cutoutInsets.calculateStartPadding(layoutDirection),
                        end = cutoutInsets.calculateEndPadding(layoutDirection)
                    ),
                contentAlignment = Alignment.Center
            ) {
                HorizontalFloatingToolbar(
                    expanded = true,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(bottom = FloatingToolbarDefaults.ScreenOffset)
                    ,
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { navigateTo(BottomNavItem.Search) },
                            containerColor = if (selectedItem == BottomNavItem.Search)
                                             MaterialTheme.colorScheme.primaryContainer
                                             else MaterialTheme.colorScheme.secondaryContainer,
                            shape = CircleShape

                        ) {
                            Icon(BottomNavItem.Search.unselectedIcon, contentDescription = BottomNavItem.Search.title)
                        }
                    }
                ) {
                        BottomNavItem.entries.filter { it != BottomNavItem.Search }.forEach { item ->
                            val isSelected = item == selectedItem
                            ToggleButton(
                                checked = isSelected,
                                onCheckedChange = { navigateTo(item) },
                                colors = ToggleButtonDefaults.toggleButtonColors(
                                    checkedContainerColor = MaterialTheme.colorScheme.primary,
                                    checkedContentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                shapes = ToggleButtonDefaults.shapes(
                                    CircleShape,
                                    CircleShape,
                                    CircleShape
                                ),
                                modifier = Modifier.height(ButtonDefaults.MediumContainerHeight)
                            ) {

                                    Crossfade(targetState = isSelected) { selected ->
                                        if (selected) {
                                            Icon(
                                                imageVector = item.selectedIcon,
                                                contentDescription = item.title
                                            )
                                        } else {
                                            Icon(
                                                imageVector = item.unselectedIcon,
                                                contentDescription = item.title
                                            )
                                        }
                                    }

                            }

                        }

                }
            }
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(layoutDirection),
                    end = innerPadding.calculateEndPadding(layoutDirection)
                )
                .fillMaxSize(),
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            transitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { if (forward) it else -it },
                    animationSpec = tween(250, easing = LinearOutSlowInEasing)
                ) togetherWith
                        slideOutHorizontally(
                            targetOffsetX = { if (forward) -it else it },
                            animationSpec = tween(250, easing = FastOutLinearInEasing)
                        )
            },
            popTransitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { if (forward) it else -it },
                    animationSpec = tween(250, easing = LinearOutSlowInEasing)
                ) togetherWith
                        slideOutHorizontally(
                            targetOffsetX = { if (forward) -it else it },
                            animationSpec = tween(250, easing = FastOutLinearInEasing)
                        )
            },
            entryProvider = entryProvider {
                entry<BottomNavItem.Home> {
                    HomeScreen(
                        onNavigateToContentDetails = onNavigateToContentDetails,
                        bottomPadding = innerPadding.calculateBottomPadding()
                    )
                }
                entry<BottomNavItem.Activity> {
                    ActivityScreen(
                        onNavigateToContentDetails = onNavigateToContentDetails,
                        bottomPadding = innerPadding.calculateBottomPadding()
                    )
                }
                entry<BottomNavItem.Profile> {
                    ProfileScreen(
                        onNavigateToAuth = onNavigateToAuth,
                        onNavigateToContentDetails = onNavigateToContentDetails,
                        bottomPadding = innerPadding.calculateBottomPadding()
                    )
                }
                entry<BottomNavItem.Search> {
                    SearchScreen(
                        onNavigateToContentDetails = onNavigateToContentDetails,
                        onNavigateToPerson = onNavigateToPerson,
                        bottomPadding = innerPadding.calculateBottomPadding()
                    )
                }
            }
        )
    }
}
