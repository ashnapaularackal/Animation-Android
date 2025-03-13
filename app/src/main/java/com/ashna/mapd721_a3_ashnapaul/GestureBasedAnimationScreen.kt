package com.ashna.mapd721_a3_ashnapaul

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun GestureBasedAnimationScreen() {
    val offset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray)
        .pointerInput(Unit) {
            coroutineScope {
                while (true) {
                    // This is the problematic line
                    // You need to use awaitPointerEventScope to get access to awaitFirstDown
                    val position = awaitPointerEventScope {
                        awaitFirstDown().position
                    }
                    launch {
                        offset.animateTo(position)
                    }
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .offset { offset.value.toIntOffset() }
                .background(Color.Blue, shape = CircleShape)
        )
        Text("Tap anywhere to move the circle",
            modifier = Modifier.align(Alignment.TopCenter).padding(16.dp))
    }
}

private fun Offset.toIntOffset() = IntOffset(x.roundToInt(), y.roundToInt())
