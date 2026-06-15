package com.charan.bingediary.presentation.authentication

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mmk.kmpauth.google.GoogleButtonUiContainer
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthenticationScreen(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Welcome to Binge Diary",
    onAuthSuccess: (() -> Unit)? = null,
) {
    val viewModel: AuthenticationViewModel = koinViewModel<AuthenticationViewModel>()
    val state by viewModel.state.collectAsState()
    val isBottomSheet = onAuthSuccess != null

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AuthenticationEffect.NavigateToHome -> {
                    if (onAuthSuccess != null) {
                        onAuthSuccess()
                    } else {
                        onNavigateToHome()
                    }
                }

                is AuthenticationEffect.ShowToast -> {
                }
            }
        }
    }

    if (isBottomSheet) {
        AuthenticationContent(
            title = title,
            state = state,
            isBottomSheet = true,
            onEvent = viewModel::onEvent,
            modifier = modifier
        )
    } else {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AuthenticationContent(
                title = title,
                state = state,
                isBottomSheet = false,
                onEvent = viewModel::onEvent,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun AuthenticationContent(
    title: String,
    state: AuthenticationState,
    isBottomSheet: Boolean,
    onEvent: (AuthenticationEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = if (isBottomSheet) 32.dp else 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isBottomSheet) {
            Spacer(modifier = Modifier.height(8.dp))
        }

        Icon(
            imageVector = Icons.Default.ThumbUp,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isBottomSheet) "Sign in to continue"
            else "Track, review, and share your favorite movies and shows",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        GoogleButtonUiContainer(onGoogleSignInResult = { googleUser ->
            val idToken = googleUser?.idToken
            onEvent(AuthenticationEvent.SignInWithGoogle(idToken ?: ""))
        }) {
            Button(
                onClick = { this.onClick() },
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = !state.isLoading,
                shapes = ButtonDefaults.shapes()
            ) {
                AnimatedVisibility(
                    visible = state.isLoading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Row {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(Modifier.width(12.dp))
                    }
                }
                Text(
                    text = if (state.isLoading) "Signing in…" else "Sign in with Google",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        if (!isBottomSheet) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                Text(
                    text = "or",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = { onEvent(AuthenticationEvent.SignInAsGuest) },
                enabled = !state.isLoading
            ) {
                Text(
                    text = "Continue as Guest",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(if (isBottomSheet) 16.dp else 0.dp))
    }
}
