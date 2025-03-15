package com.ashna.mapd721_a3_ashnapaul

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.animation.with
import androidx.compose.animation.core.animateFloat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EnhancedCountAnimation(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    // Create an infinite transition for the pulse effect
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    // Animate the scale of the button for a pulsing effect
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000), // Animation duration of 1 second
            repeatMode = RepeatMode.Reverse // Reverse the animation after each iteration
        ),
        label = "button pulse"
    )

    // Column to hold the counter elements
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, // Center items horizontally
        verticalArrangement = Arrangement.spacedBy(24.dp), // Add space between items
        modifier = Modifier.fillMaxWidth() // Fill the available width
    ) {
        // Title text
        Text(
            text = "Counter Animation",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center // Center the text
        )

        // Box to display the counter value with a background
        Box(
            modifier = Modifier
                .size(140.dp) // Set the size of the box
                .background(
                    brush = Brush.linearGradient( // Use a linear gradient for the background
                        colors = listOf(
                            Color(0xFF6F3CE9),
                            Color(0xFF33C9FF)
                        )
                    ),
                    shape = CircleShape // Make the background a circle
                ),
            contentAlignment = Alignment.Center // Center the content inside the box
        ) {
            // Animate the content when the count changes
            AnimatedContent(
                targetState = count, // The target state is the current count
                transitionSpec = {
                    // Determine the animation direction based on whether the count is increasing or decreasing
                    val direction = if (targetState > initialState) {
                        // Count increasing: slide in from bottom and fade in
                        (slideIn(initialOffset = { IntOffset(0, it.height) }) + fadeIn()) with
                                (slideOut(targetOffset = { IntOffset(0, -it.height) }) + fadeOut()) // Slide out to top and fade out
                    } else {
                        // Count decreasing: slide in from top and fade in
                        (slideIn(initialOffset = { IntOffset(0, -it.height) }) + fadeIn()) with
                                (slideOut(targetOffset = { IntOffset(0, it.height) }) + fadeOut()) // Slide out to bottom and fade out
                    }
                    direction.using(SizeTransform(clip = false)) // Disable clipping during the animation
                },
                label = "digit animation"
            ) { targetCount ->
                // Display the counter value
                Text(
                    text = "$targetCount", // Convert the count to a string
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }

        // Row to hold the increment and decrement buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp), // Add space between
            verticalAlignment = Alignment.CenterVertically // Align items vertically
        ) {
            // Decrement button
            IconButton(
                onClick = onDecrement, // Call the onDecrement function when clicked
                modifier = Modifier.size(56.dp) // Set the size of the button
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(color = Color(0xFFE91E63), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown, // Use the Remove icon
                        contentDescription = "Decrement", // Content description for accessibility
                        tint = Color.White, // Tint the icon white
                        modifier = Modifier.size(32.dp) // Set the size of the icon
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp)) // Add some space

            // Increment button
            IconButton(
                onClick = onIncrement, // Call the onIncrement function when clicked
                modifier = Modifier.size(56.dp) // Set the size of the button
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(color = Color(0xFF4CAF50), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp, // Use the Add icon
                        contentDescription = "Increment", // Content description for accessibility
                        tint = Color.White, // Tint the icon white
                        modifier = Modifier.size(32.dp) // Set the size of the icon
                    )
                }
            }
        }
    }
}
