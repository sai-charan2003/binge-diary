package com.charan.bingediary.presentation.logreview

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import com.charan.bingediary.presentation.common.model.MediaType
import com.charan.bingediary.presentation.common.components.InteractiveRatingBar


@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LogReviewScreen(
    mediaId: Long,
    mediaType: MediaType,
    mediaTitle: String,
    mediaYear: String,
    onDismiss: () -> Unit,
    viewModel: LogReviewViewModel = koinViewModel { parametersOf(mediaId, mediaType, mediaTitle, mediaYear) }
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LogReviewEffect.NavigateBack -> onDismiss()
                is LogReviewEffect.ShowToast -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }
    val reviewActionItems = listOf(
        ReviewActionItem(
            title = "Watched",
            unSelectedIcon = Icons.Rounded.Visibility,
            selectedIcon = Icons.Rounded.Visibility,
            onClick = { viewModel.onEvent(LogReviewEvent.ToggleWatched) },
            isSelected = state.watched
        ),
        ReviewActionItem(
            title = "Loved",
            unSelectedIcon = Icons.Rounded.Favorite,
            selectedIcon = Icons.Rounded.Favorite,
            onClick = { viewModel.onEvent(LogReviewEvent.ToggleLoved) },
            isSelected = state.loved
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp, top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = buildString {
                append(state.title)
                if (state.releaseYear.isNotEmpty()) {
                    append(" (${state.releaseYear})")
                }
            },
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        ButtonGroup(
            overflowIndicator = { menuState ->
                ButtonGroupDefaults.OverflowIndicator(menuState = menuState)
            },
        ) {
            reviewActionItems.fastForEach{ item->
                toggleableItem(
                    checked = item.isSelected,
                    onCheckedChange = { item.onClick() },
                    label = item.title,
                    weight = 1f,
                    icon = {
                        Icon(
                            item.selectedIcon,
                            null
                        )
                    }
                )
            }


        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "YOUR RATING",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )

            InteractiveRatingBar(
                rating = state.rating,
                onRatingChanged = { newRating ->
                    viewModel.onEvent(LogReviewEvent.SetRating(newRating))
                }
            )

            AnimatedContent(
                targetState = state.rating,
                transitionSpec = {
                    (slideInVertically { it } + fadeIn())
                        .togetherWith(slideOutVertically { -it } + fadeOut())
                }
            ) { rating ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (rating == rating.toInt().toFloat()) "${rating.toInt()}" else "$rating",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = " / 5",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "REVIEW",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )

            OutlinedTextField(
                value = state.reviewText,
                onValueChange = { viewModel.onEvent(LogReviewEvent.UpdateReviewText(it)) },
                placeholder = {
                    Text(
                        text = "Add your thoughts...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Transparent
                ),
                singleLine = false,
                textStyle = MaterialTheme.typography.bodyMedium
            )
        }

        FilledTonalButton(
            onClick = { viewModel.onEvent(LogReviewEvent.SaveReview) },
            enabled = !state.isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(100.dp)
        ) {
            if (state.isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Save to Diary",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        SnackbarHost(snackbarHostState)
    }
}


data class ReviewActionItem(
    val isSelected : Boolean = false,
    val title: String,
    val unSelectedIcon : ImageVector,
    val selectedIcon: ImageVector,
    val onClick: () -> Unit
)