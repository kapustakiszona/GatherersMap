@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.gatherersmap.presentation.components.reusables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val ANIMATION_TIME_QTR_SEC = 250L
const val ANIMATION_TIME_ONE_SEC = 1000L
const val DIALOG_BUILD_TIME = 300L

@Composable
fun AnimatedEntryDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable () -> Unit,
) {
    val animatedTrigger = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit) {
        launch {
            delay(DIALOG_BUILD_TIME)
            animatedTrigger.value = true
        }
    }
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            contentAlignment = contentAlignment,
            modifier = modifier.fillMaxSize()
        ) {
            AnimatedScaleInTransition(
                visible = animatedTrigger.value,
                animateDuration = ANIMATION_TIME_ONE_SEC
            ) {
                content()
            }
        }
    }
}

@Composable
fun AnimatedEntryExitDialog(
    onDismissRequest: () -> Unit,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable () -> Unit,
) {
    val scope: CoroutineScope = rememberCoroutineScope()
    val animatedTrigger = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit) {
        launch {
            delay(DIALOG_BUILD_TIME)
            animatedTrigger.value = true
        }
    }
    AlertDialog(onDismissRequest = {
        scope.launch {
            startDismissWithExitAnimation(
                animateTrigger = animatedTrigger,
                onDismissRequest = onDismissRequest
            )
        }
    }, modifier = Modifier.wrapContentSize()) {
        Box(
            contentAlignment = contentAlignment,
            modifier = Modifier.size(350.dp)
        ) {
            AnimatedScaleInTransition(
                visible = animatedTrigger.value,
                animateDuration = ANIMATION_TIME_ONE_SEC
            ) {
                content()
            }
        }
    }
}

private suspend fun startDismissWithExitAnimation(
    animateTrigger: MutableState<Boolean>,
    onDismissRequest: () -> Unit,
) {
    animateTrigger.value = false
    delay(ANIMATION_TIME_ONE_SEC)
    onDismissRequest()
}

@Composable
fun AnimatedTransitionSample(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(
            animationSpec = tween(
                1500,
                easing = EaseInBounce
            )
        ),
        exit = shrinkVertically(
            animationSpec = tween(
                1500,
                easing = EaseInBounce
            )
        ),
        content = content
    )
}

@Composable
fun AnimatedScaleInTransition(
    visible: Boolean,
    animateDuration: Long,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            animationSpec = tween(animateDuration.toInt())
        ),
        exit = scaleOut(
            animationSpec = tween(animateDuration.toInt())
        ),
        content = content
    )
}