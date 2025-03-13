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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Composable function to create an interactive animation where a circle moves to the tapped position.
 */
@Composable
fun GestureBasedAnimationScreen() {
    // Animatable offset to track the circle's position
    val offset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0E0E0)) // Light gray background for better visibility
            .pointerInput(Unit) { // Detects tap gestures anywhere on the screen
                coroutineScope {
                    while (true) {
                        // Wait for user touch input
                        val position = awaitPointerEventScope {
                            awaitFirstDown().position
                        }
                        // Animate the circle to the new position
                        launch {
                            offset.animateTo(
                                position,
                                animationSpec = tween(600, easing = FastOutSlowInEasing) // Smooth easing effect
                            )
                        }
                    }
                }
            }
    ) {
        // Instruction text at the top
        Text(
            text = "Tap anywhere to move the circle",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(50.dp),
            color = Color.DarkGray,
            fontWeight = FontWeight.Bold
        )

        // Circular shape that moves to the tapped position
        Box(
            modifier = Modifier
                .size(60.dp) // Circle size
                .offset { offset.value.toIntOffset() } // Moves based on user input
                .background(Color(0xFF2979FF), shape = CircleShape) // Blue color with circular shape
        )
    }
}

/**
 * Extension function to convert Offset values to IntOffset for positioning the circle.
 */
private fun Offset.toIntOffset() = IntOffset(x.roundToInt(), y.roundToInt())
