package com.charan.bingediary.presentation.common.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.TopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomMediumTopBar(
    modifier: Modifier = Modifier,
    showBackButton : Boolean = false,
    onBackClick : () -> Unit = {},
    title : String,
    subTitle : String = "",
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
    ) {
    MediumTopAppBar(
        modifier = modifier,
        colors = colors,
        title = {
            Text(text = title)
        },
        navigationIcon = {
            if(showBackButton){
            CustomBackButton { onBackClick() }}

        },
        actions = actions,
        scrollBehavior = scrollBehavior
    )
}
@Composable
fun CustomTopBar(
    modifier: Modifier = Modifier,
    showBackButton : Boolean = false,
    onBackClick : () -> Unit = {},
    title : String,
    subTitle : String = "",
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null

) {
    TopAppBar(
        modifier = modifier,
        colors = colors,
        title = {
            Text(text = title)
        },
        navigationIcon = {
            if(showBackButton){
                CustomBackButton { onBackClick() }

            }

        },
        actions = actions,
        scrollBehavior = scrollBehavior
    )
}
@Composable
private fun CustomBackButton(
    onBackClick: () -> Unit
) {
    FilledTonalIconButton(
        onClick = onBackClick,
        shapes = IconButtonDefaults.shapes(),
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back"
        )
    }
}