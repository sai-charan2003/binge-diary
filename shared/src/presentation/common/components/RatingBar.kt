package com.charan.bingediary.presentation.common.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun InteractiveRatingBar(
    rating: Float,
    onRatingChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
    stars: Int = 5,
    starSize: Dp = 40.dp,
    spacing: Dp = 8.dp,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
) {
    val density = LocalDensity.current
    val starSizePx = with(density) { starSize.toPx() }
    val spacingPx = with(density) { spacing.toPx() }
    val totalWidth = starSizePx * stars + spacingPx * (stars - 1)

    val animatedRating = remember { Animatable(rating) }
    LaunchedEffect(rating) {
        animatedRating.animateTo(rating, animationSpec = spring(stiffness = 600f))
    }

    fun ratingFromX(x: Float): Float {
        val clamped = x.coerceIn(0f, totalWidth)
        val starUnitWidth = starSizePx + spacingPx
        val starIndex = (clamped / starUnitWidth).toInt().coerceIn(0, stars - 1)
        val starStartX = starIndex * starUnitWidth
        val relativeX = clamped - starStartX

        val fraction = (relativeX / starSizePx).coerceIn(0f, 1f)
        val snapped = (fraction * 4).roundToInt() / 4f
        return (starIndex + snapped).coerceIn(0f, stars.toFloat())
    }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val starUnitWidth = starSizePx + spacingPx
                    val tappedStar = (offset.x / starUnitWidth)
                        .toInt()
                        .coerceIn(0, stars - 1) + 1

                    val fullValue = tappedStar.toFloat()
                    val halfValue = tappedStar - 0.5f

                    val nextRating = when {
                        kotlin.math.abs(rating - fullValue) < 0.01f -> halfValue
                        kotlin.math.abs(rating - halfValue) < 0.01f -> 0f
                        else -> fullValue
                    }

                    onRatingChanged(nextRating)
                }
            }
            .pointerInput(Unit) {
                var isDragging = false
                detectDragGestures(
                    onDragStart = { offset ->
                        isDragging = true
                        onRatingChanged(ratingFromX(offset.x))
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        onRatingChanged(ratingFromX(change.position.x))
                    },
                    onDragEnd = { isDragging = false },
                    onDragCancel = { isDragging = false }
                )
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(stars) { index ->
                val starFill = (animatedRating.value - index).coerceIn(0f, 1f)
                StarCanvas(
                    fill = starFill,
                    activeColor = activeColor,
                    inactiveColor = inactiveColor,
                    modifier = Modifier.size(starSize)
                )
            }
        }
    }
}

@Composable
private fun StarCanvas(
    fill: Float,
    activeColor: Color,
    inactiveColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .drawBehind {
                val starPath = createStarPath(size.width, size.height)
                drawPath(starPath, inactiveColor)
                if (fill > 0f) {
                    clipPath(starPath) {
                        drawRect(
                            color = activeColor,
                            size = size.copy(width = size.width * fill)
                        )
                    }
                }
            }
    )
}

private fun createStarPath(width: Float, height: Float): Path {
    val path = Path()
    val cx = width / 2f
    val cy = height / 2f
    val outerRadius = minOf(width, height) / 2f
    val innerRadius = outerRadius * 0.4f
    val points = 5
    val angleStep = PI / points
    val startAngle = -PI / 2

    for (i in 0 until points * 2) {
        val radius = if (i % 2 == 0) outerRadius else innerRadius
        val angle = startAngle + i * angleStep
        val x = cx + (radius * cos(angle)).toFloat()
        val y = cy + (radius * sin(angle)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}

@Composable
fun ReadOnlyRatingBar(
    rating: Float,
    modifier: Modifier = Modifier,
    stars: Int = 5,
    starSize: Dp = 16.dp,
    spacing: Dp = 4.dp,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(stars) { index ->
            val starFill = (rating - index).coerceIn(0f, 1f)
            StarCanvas(
                fill = starFill,
                activeColor = activeColor,
                inactiveColor = inactiveColor,
                modifier = Modifier.size(starSize)
            )
        }
    }
}
