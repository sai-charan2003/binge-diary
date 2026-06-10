package com.charan.bingediary.presentation.common.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }

            }

        },
        actions = actions,
        scrollBehavior = scrollBehavior
    )
}