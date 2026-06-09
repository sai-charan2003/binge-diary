package com.charan.bingediary.presentation.common.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CustomMediumTopBar(
     modifier: Modifier = Modifier,
     showBackButton : Boolean = false,
     onBackClick : () -> Unit = {},
     title : String,
     subTitle : String = ""
) {
    MediumTopAppBar(
        title = {
            Text(text = title)
        },
        actions = {
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
        }

    )
}