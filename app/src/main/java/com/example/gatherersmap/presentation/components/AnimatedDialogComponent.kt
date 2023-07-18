@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.gatherersmap.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val ANIMATION_TIME = 1000L
const val DIALOG_BUILD_TIME = 300L

@Composable
fun AnimatedDialog(
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
            AnimatedScaleInTransition(visible = animatedTrigger.value) {
                content()
            }
        }
    }
}

suspend fun startDismissWithExitAnimation(
    animateTrigger: MutableState<Boolean>,
    onDismissRequest: () -> Unit,
) {
    animateTrigger.value = false
    delay(ANIMATION_TIME)
    onDismissRequest()
}

@Composable
fun AnimatedScaleInTransition(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            animationSpec = tween(ANIMATION_TIME.toInt())
        ),
        exit = scaleOut(
            animationSpec = tween(ANIMATION_TIME.toInt())
        ),
        content = content
    )
}