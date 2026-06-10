package com.charan.bingediary.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.charan.bingediary.presentation.common.components.CustomMediumTopBar
import com.charan.bingediary.presentation.common.model.MediaType
import com.charan.bingediary.presentation.details.model.CastCrewUiModel
import androidx.compose.material3.TopAppBarDefaults
import com.charan.bingediary.presentation.details.ContentDetailsEffect.NavigateToPersonDetails
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
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

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
            CustomMediumTopBar(
                title = "",
                showBackButton = true,
                onBackClick = { viewModel.onEvent(ContentDetailsEvent.NavigateBack) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                ),
                actions = {

                }
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
                }
                
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
                                        text = "${details.rating} / 10",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    
                                    if (details.runtime.isNotEmpty()) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "•",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = details.runtime,
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }

                                    if (details.releaseYear.isNotEmpty()) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "•",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = details.releaseYear,
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

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
                                        onClick = { /* Log Review */ },
                                        modifier = Modifier.weight(1f).height(50.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    ) {
                                        Icon(Icons.Default.Edit, contentDescription = null)
                                        Spacer(Modifier.width(8.dp))
                                        Text("Log Review")
                                    }
                                    OutlinedButton(
                                        onClick = { /* Save to Watchlater */ },
                                        modifier = Modifier.weight(1f).height(50.dp)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = null)
                                        Spacer(Modifier.width(8.dp))
                                        Text("Watchlist")
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }

                    // Directors Section
                    if (details.directors.isNotEmpty()) {
                        item { PersonListSection(title = "Directors", persons = details.directors, onPersonClick = onNavigateToPerson) }
                    }

                    // Writers Section
                    if (details.writers.isNotEmpty()) {
                        item { PersonListSection(title = "Writers", persons = details.writers, onPersonClick = onNavigateToPerson) }
                    }

                    // Producers Section
                    if (details.producers.isNotEmpty()) {
                        item { PersonListSection(title = "Producers", persons = details.producers, onPersonClick = onNavigateToPerson) }
                    }

                    // Cast Section
                    if (details.cast.isNotEmpty()) {
                        item { PersonListSection(title = "Cast", persons = details.cast, onPersonClick = onNavigateToPerson) }
                    }
                }
            }

    }
}

@Composable
fun PersonItem(name: String, subtitle: String, imageUrl: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(88.dp)
            .clickable { onClick() }
    ) {
        if (imageUrl.isNotEmpty()) {
            AsyncImage(
                model = imageUrl.ifEmpty { Icons.Rounded.Person },
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f), CircleShape)
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = name,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            maxLines = 1,
            textAlign = TextAlign.Center
        )
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

@Composable
private fun PersonListSection(
    title: String,
    persons: List<CastCrewUiModel>,
    onPersonClick: (Long) -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 24.dp)
        ) {
            items(persons) { person ->
                PersonItem(
                    name = person.name,
                    subtitle = person.character,
                    imageUrl = person.profileUrl,
                    onClick = { onPersonClick(person.id) }
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
