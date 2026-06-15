package com.charan.bingediary.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import com.charan.bingediary.presentation.authentication.AuthenticationScreen
import com.charan.bingediary.presentation.logreview.LogReviewScreen
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.charan.bingediary.presentation.common.model.MediaType
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.remember
import com.charan.bingediary.presentation.common.components.CustomTopBar
import com.charan.bingediary.presentation.details.components.MediaMetaDataRow
import com.charan.bingediary.presentation.details.components.PersonListSection
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentDetailsScreen(
    mediaId: Long,
    mediaType: MediaType,
    onNavigateBack: () -> Unit,
    onNavigateToPerson: (Long) -> Unit
) {
    val viewModel: ContentDetailsViewModel = koinViewModel { parametersOf(mediaId, mediaType) }
    val state by viewModel.state.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ContentDetailsEffect.NavigateBack -> onNavigateBack()
                is ContentDetailsEffect.ShowToast -> { /* Handle toast */ }
                is ContentDetailsEffect.NavigateToPersonDetails -> onNavigateToPerson(effect.personId)
            }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopBar(
                title = "",
                showBackButton = true,
                onBackClick = { viewModel.onEvent(ContentDetailsEvent.NavigateBack) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = MaterialTheme.colorScheme.background,

                ),
                actions = {

                },
                scrollBehavior = scrollBehavior
            )
        }

    ) { innerPadding ->
            
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.isLoading) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(500.dp)) {
                            ContainedLoadingIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                } else {

                    state.details?.let { details ->
                        item {
                            Column {
                                FullScreenBackdrop(details.backdropUrl.ifEmpty { details.posterUrl })

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = details.title,
                                        style = MaterialTheme.typography.displaySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        textAlign = TextAlign.Center
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    MediaMetaDataRow(
                                        rating = details.rating,
                                        runTime = details.runtime,
                                        release = details.releaseYear
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Text(
                                        text = details.overview,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                        textAlign = TextAlign.Center
                                    )

                                    Spacer(modifier = Modifier.height(32.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Button(
                                            onClick = { viewModel.onEvent(ContentDetailsEvent.LogReviewClicked) },
                                            modifier = Modifier.weight(1f),
                                            shapes = ButtonDefaults.shapes(),


                                            ) {
                                            Icon(Icons.Default.Edit, contentDescription = null)
                                            Spacer(Modifier.width(8.dp))
                                            Text("Log Review")
                                        }
                                         if (state.isInWatchlist) {
                                             Button(
                                                 onClick = { viewModel.onEvent(ContentDetailsEvent.WatchlistClicked) },
                                                 modifier = Modifier.weight(1f),
                                                 shapes = ButtonDefaults.shapes()
                                             ) {
                                                 Icon(Icons.Default.Check, contentDescription = null)
                                                 Spacer(Modifier.width(8.dp))
                                                 Text("In Watchlist")
                                             }
                                         } else {
                                             OutlinedButton(
                                                 onClick = { viewModel.onEvent(ContentDetailsEvent.WatchlistClicked) },
                                                 modifier = Modifier.weight(1f),
                                                 shapes = ButtonDefaults.shapes()
                                             ) {
                                                 Icon(Icons.Default.Add, contentDescription = null)
                                                 Spacer(Modifier.width(8.dp))
                                                 Text("Watchlist")
                                             }
                                         }
                                    }
                                }

                                Spacer(modifier = Modifier.height(32.dp))
                            }
                        }

                        // Directors Section
                        if (details.directors.isNotEmpty()) {
                            item {
                                PersonListSection(
                                    title = "Directors",
                                    persons = details.directors,
                                    onPersonClick = onNavigateToPerson
                                )
                            }
                        }

                        // Writers Section
                        if (details.writers.isNotEmpty()) {
                            item {
                                PersonListSection(
                                    title = "Writers",
                                    persons = details.writers,
                                    onPersonClick = onNavigateToPerson
                                )
                            }
                        }

                        // Producers Section
                        if (details.producers.isNotEmpty()) {
                            item {
                                PersonListSection(
                                    title = "Producers",
                                    persons = details.producers,
                                    onPersonClick = onNavigateToPerson
                                )
                            }
                        }

                        // Cast Section
                        if (details.cast.isNotEmpty()) {
                            item {
                                PersonListSection(
                                    title = "Cast",
                                    persons = details.cast,
                                    onPersonClick = onNavigateToPerson
                                )
                            }
                        }
                    }
                }
            }

    }

    if (state.showAuthBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.onEvent(ContentDetailsEvent.DismissAuthBottomSheet) },
            sheetState = rememberModalBottomSheetState()
        ) {
            AuthenticationScreen(
                onNavigateToHome = {},
                title = state.authBottomSheetTitle,
                onAuthSuccess = {
                    viewModel.onEvent(ContentDetailsEvent.AuthSuccess)
                },
            )
        }
    }

    if (state.showReviewBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.onEvent(ContentDetailsEvent.DismissReviewBottomSheet) },
            sheetState = rememberModalBottomSheetState()
        ) {
            LogReviewScreen(
                mediaId = mediaId,
                mediaType = mediaType,
                mediaTitle = state.details.title,
                mediaYear = state.details.releaseYear,
                onDismiss = { viewModel.onEvent(ContentDetailsEvent.DismissReviewBottomSheet) }
            )
        }
    }
}



@Composable
private fun FullScreenBackdrop(
    imageUrl: String,
    height: Dp = 300.dp,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth().height(height)) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Backdrop",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                            MaterialTheme.colorScheme.background
                        ),
                        startY = 100f
                    )
                )
        )
    }
}


