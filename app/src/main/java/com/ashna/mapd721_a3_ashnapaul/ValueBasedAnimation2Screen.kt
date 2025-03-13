package com.ashna.mapd721_a3_ashnapaul

import androidx.compose.animation.* // Importing Jetpack Compose animation utilities
import androidx.compose.animation.core.* // Importing core animation utilities
import androidx.compose.foundation.background // Importing background modifier
import androidx.compose.foundation.layout.* // Importing layout-related utilities
import androidx.compose.material3.* // Importing Material 3 components
import androidx.compose.runtime.* // Importing Composable functions and state management utilities
import androidx.compose.ui.Alignment // Importing alignment options
import androidx.compose.ui.Modifier // Importing Modifier for UI customization
import androidx.compose.ui.graphics.Color // Importing Color utilities

/**
 * Composable function that demonstrates an infinite color transition animation.
 */
@Composable
fun ValueBasedAnimation2Screen() {
    // Creating an infinite transition for animation
    val infiniteTransition = rememberInfiniteTransition(label = "color transition")

    // Animating color transition between Red and Green using infinite animation
    val color by infiniteTransition.animateColor(
        initialValue = Color.Red,  // Starting color
        targetValue = Color.Green, // Ending color
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing), // Duration of 2 seconds with linear easing
            repeatMode = RepeatMode.Reverse // Reverses the color transition back and forth
        ),
        label = "color"
    )

    // Creating a full-screen Box with background color transitioning infinitely
    Box(
        modifier = Modifier
            .fillMaxSize() // Occupies the entire screen
            .background(color) // Applies the animated background color
    ) {
        // Displaying text at the center of the screen
        Text(
            text = "Infinite Color Transition",
            color = Color.White, // Text color for better visibility
            modifier = Modifier.align(Alignment.Center) // Aligning text in the center
        )
    }
}
