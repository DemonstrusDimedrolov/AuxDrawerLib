package com.dimedrol.aux_drawer

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun AuxDrawer(
    drawerContent: @Composable (CoroutineScope, closeDrawer: () -> Unit) -> Unit,
    mainContent: @Composable (onMenuClick: () -> Unit, isDrawerOpen: Boolean, closeDrawer: () -> Unit) -> Unit,
    modifier: Modifier = Modifier,
    drawerWidthFraction: Float = 0.74f,
    minDrawerWidth: Dp = 240.dp,
    cornerRadius: Dp = 0.dp,
    drawerElevation: Dp = 0.dp,
    drawerBackgroundColor: Color = MaterialTheme.colorScheme.surface,
    animationDurationMillis: Int = 110,
    animationEasing: Easing = LinearEasing,
    velocityThreshold: Float = 1000f,
    openPercentageToTrigger: Float = 0.4f,
    enableSwipe: Boolean = true,
    onDrawerOpened: () -> Unit = {},
    onDrawerClosed: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    val drawerWidthDp = remember(configuration) {
        (configuration.screenWidthDp * drawerWidthFraction).dp.coerceAtLeast(minDrawerWidth)
    }
    val drawerWidthPx = with(density) { drawerWidthDp.toPx() }

    var isDrawerOpen by remember { mutableStateOf(false) }
    val animatedOffset = remember { Animatable(0f) }
    val interactionSource = remember { MutableInteractionSource() }

    val animateDrawer: (Boolean) -> Unit = { open ->
        scope.launch {
            animatedOffset.animateTo(
                targetValue = if (open) drawerWidthPx else 0f,
                animationSpec = tween(
                    durationMillis = animationDurationMillis,
                    easing = animationEasing
                )
            )
            isDrawerOpen = open
            if (open) onDrawerOpened() else onDrawerClosed()
        }
    }

    val closeDrawerLambda = { animateDrawer(false) }
    val onMenuClickLambda = { animateDrawer(!isDrawerOpen) }

    LaunchedEffect(Unit) {
        animatedOffset.snapTo(0f)
        isDrawerOpen = false
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // DRAWER CONTENT
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(drawerWidthDp)
                .offset { IntOffset((animatedOffset.value - drawerWidthPx).roundToInt(), 0) }
                .graphicsLayer {
                    shadowElevation = drawerElevation.toPx()
                    shape = RoundedCornerShape(topEnd = cornerRadius, bottomEnd = cornerRadius)
                    clip = true
                }
                .background(drawerBackgroundColor, shape = RoundedCornerShape(topEnd = cornerRadius, bottomEnd = cornerRadius))
                .systemBarsPadding()
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .let {
                    if (enableSwipe && isDrawerOpen) {
                        it.draggable(
                            orientation = Orientation.Horizontal,
                            state = rememberDraggableState { delta ->
                                scope.launch {
                                    val newOffset = (animatedOffset.value + delta)
                                        .coerceIn(0f, drawerWidthPx)
                                    animatedOffset.snapTo(newOffset)
                                }
                            },
                            onDragStopped = { velocity ->
                                scope.launch {
                                    val currentOffset = animatedOffset.value
                                    val targetValue = when {
                                        velocity < -velocityThreshold -> 0f
                                        velocity > velocityThreshold -> drawerWidthPx
                                        currentOffset > drawerWidthPx * openPercentageToTrigger -> drawerWidthPx
                                        else -> 0f
                                    }
                                    animatedOffset.animateTo(
                                        targetValue = targetValue,
                                        animationSpec = tween(animationDurationMillis, easing = animationEasing),
                                        initialVelocity = velocity
                                    )
                                    isDrawerOpen = targetValue == drawerWidthPx
                                    if (isDrawerOpen) onDrawerOpened() else onDrawerClosed()
                                }
                            }
                        )
                    } else it
                }
        ) {
            drawerContent(scope, closeDrawerLambda)
        }

        // MAIN CONTENT
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(animatedOffset.value.roundToInt(), 0) }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = isDrawerOpen,
                    onClick = { closeDrawerLambda() }
                )
                .let {
                    if (enableSwipe) {
                        it.draggable(
                            orientation = Orientation.Horizontal,
                            state = rememberDraggableState { delta ->
                                scope.launch {
                                    val newOffset = (animatedOffset.value + delta)
                                        .coerceIn(0f, drawerWidthPx)
                                    animatedOffset.snapTo(newOffset)
                                }
                            },
                            onDragStopped = { velocity ->
                                scope.launch {
                                    val currentOffset = animatedOffset.value
                                    val targetValue = when {
                                        velocity > velocityThreshold -> drawerWidthPx
                                        velocity < -velocityThreshold -> 0f
                                        currentOffset > drawerWidthPx * openPercentageToTrigger -> drawerWidthPx
                                        else -> 0f
                                    }
                                    animatedOffset.animateTo(
                                        targetValue = targetValue,
                                        animationSpec = tween(animationDurationMillis, easing = animationEasing),
                                        initialVelocity = velocity
                                    )
                                    isDrawerOpen = targetValue == drawerWidthPx
                                    if (isDrawerOpen) onDrawerOpened() else onDrawerClosed()
                                }
                            }
                        )
                    } else it
                }
        ) {
            mainContent(onMenuClickLambda, isDrawerOpen, closeDrawerLambda)
        }
    }
}