package com.charan.bingediary.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

@Composable
fun PersonPlaceholder(
    contentDescription: String = "",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Person,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(0.45f),
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}
