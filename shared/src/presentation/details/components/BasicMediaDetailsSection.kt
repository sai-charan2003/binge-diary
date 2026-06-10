package com.charan.bingediary.presentation.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MediaMetaDataRow(
    rating : String,
    runTime : String,
    release : String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Star,
            contentDescription = "Rating",
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$rating / 10",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )

        if (runTime.isNotEmpty()) {
            MediaMetaItem(text = runTime)
        }

        if (release.isNotEmpty()) {
            MediaMetaItem(text = release)

        }
    }

}

@Composable
private fun MediaMetaItem(
    text : String
) {
    Spacer(modifier = Modifier.width(8.dp))
    Text(
        text = "•",
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold
    )

}