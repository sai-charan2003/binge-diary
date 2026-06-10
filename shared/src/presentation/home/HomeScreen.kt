package com.charan.bingediary.presentation.home

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import com.charan.bingediary.presentation.common.components.CustomMediumTopBar
import com.charan.bingediary.presentation.home.components.MediaItemCard
import org.koin.compose.viewmodel.koinViewModel

import com.charan.bingediary.presentation.common.model.MediaType

@Composable
fun HomeScreen(
    onNavigateToContentDetails: (Long, MediaType) -> Unit
) {
    val viewModel: HomeViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeEffect.NavigateToContentDetails -> {
                    onNavigateToContentDetails(effect.mediaId, effect.mediaType)
                }

                is HomeEffect.ShowToast -> {
                    // Handle toast/snackbar
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CustomMediumTopBar(
                title = "Binge Diary",
                )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding() + 16.dp,
                bottom = innerPadding.calculateBottomPadding()
            ),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(state.sections) { item ->
                Column {
                    SectionHeader(title = item.title)
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(item.items) { mediaItem ->
                            MediaItemCard(
                                title = mediaItem.title,
                                imageUrl = mediaItem.posterPath,
                                rating = mediaItem.rating,
                                onClick = {
                                    viewModel.onEvent(
                                        HomeEvent.OnMediaClicked(
                                            mediaItem.id,
                                            mediaItem.mediaType
                                        )
                                    )
                                }


                            )
                        }

                    }
                }

            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
//        IconButton(
//            onClick = {}
//        ) {
//            Icon(
//                imageVector = Icons.Filled.ArrowForward,
//                null
//            )
//        }

    }
}
