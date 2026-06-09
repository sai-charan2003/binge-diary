package com.charan.bingediary.presentation.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charan.bingediary.presentation.common.components.CustomMediumTopBar
import com.charan.bingediary.presentation.home.components.MediaItemCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(

) {
    val viewModel: HomeViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeEffect.NavigateToMovieDetails -> {
                    // Handle navigation
                }
                is HomeEffect.NavigateToShowDetails -> {
                    // Handle navigation
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
            contentPadding = innerPadding,
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(state.sections){ item->
                SectionHeader(title = item.title)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),

                ) {
                    items(item.items){ mediaItem->
                        MediaItemCard(
                            title = mediaItem.title,
                            imageUrl = mediaItem.posterPath,
                            rating = mediaItem.rating,
                            onClick = {  }


                        )
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
            ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
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
