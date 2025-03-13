package com.ashna.mapd721_a3_ashnapaul

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Enhanced Animation Component
 *
 * This composable demonstrates multiple complex animations using Jetpack Compose:
 * - Color transitions with gradient effects
 * - Size and shape animations
 * - Content visibility animations with custom effects
 * - Rotation and scaling animations
 * - Coordinated transitions between multiple properties
 */
@Composable
fun ValueBasedAnimation1Screen() {
    // State to track if the card is selected/expanded
    var selected by remember { mutableStateOf(false) }

    // Create a transition that will coordinate all animations based on the 'selected' state
    val transition = updateTransition(
        targetState = selected,
        label = "Card Expansion Transition"
    )

    // Define all animated properties using the transition

    // 1. Animated gradient border colors
    val startBorderColor by transition.animateColor(
        transitionSpec = {
            tween(
                durationMillis = 800,
                easing = FastOutSlowInEasing
            )
        },
        label = "Start Border Color"
    ) { isSelected ->
        if (isSelected) Color(0xFF6200EE) else Color(0xFFBBBBBB)
    }

    val endBorderColor by transition.animateColor(
        transitionSpec = {
            tween(
                durationMillis = 800,
                easing = FastOutSlowInEasing
            )
        },
        label = "End Border Color"
    ) { isSelected ->
        if (isSelected) Color(0xFF03DAC5) else Color(0xFFEEEEEE)
    }

    // 2. Animated elevation with spring physics
    val elevation by transition.animateDp(
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        },
        label = "Card Elevation"
    ) { isSelected ->
        if (isSelected) 16.dp else 2.dp
    }

    // 3. Animated corner radius
    val cornerRadius by transition.animateDp(
        transitionSpec = {
            tween(
                durationMillis = 500,
                easing = LinearOutSlowInEasing
            )
        },
        label = "Corner Radius"
    ) { isSelected ->
        if (isSelected) 24.dp else 8.dp
    }

    // 4. Animated padding
    val padding by transition.animateDp(
        transitionSpec = {
            tween(
                durationMillis = 300,
                delayMillis = 50,
                easing = LinearOutSlowInEasing
            )
        },
        label = "Card Padding"
    ) { isSelected ->
        if (isSelected) 24.dp else 16.dp
    }

    // 5. Animated size
    val width by transition.animateDp(
        transitionSpec = {
            tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        },
        label = "Card Width"
    ) { isSelected ->
        if (isSelected) 320.dp else 280.dp
    }

    // 6. Animated background color
    val backgroundColor by transition.animateColor(
        transitionSpec = {
            tween(
                durationMillis = 500,
                easing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
            )
        },
        label = "Background Color"
    ) { isSelected ->
        if (isSelected) Color(0xFFF3E5F5) else Color(0xFFFFFFFF)
    }

    // 7. Animated rotation for the icon
    val iconRotation by transition.animateFloat(
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        },
        label = "Icon Rotation"
    ) { isSelected ->
        if (isSelected) 180f else 0f
    }

    // 8. Animated scale for highlight effects
    val scale by transition.animateFloat(
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )
        },
        label = "Content Scale"
    ) { isSelected ->
        if (isSelected) 1.05f else 1f
    }

    // Main container with spacing
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title for the demo
        Text(
            text = "Interactive Animation Demo",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Animated card with multiple effects
        Card(
            onClick = { selected = !selected },
            modifier = Modifier
                .width(width)
                .wrapContentHeight()
                .scale(scale),
            shape = RoundedCornerShape(cornerRadius),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = elevation
            ),
            border = BorderStroke(
                width = 2.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(startBorderColor, endBorderColor)
                )
            )
        ) {
            // Card content
            Column(
                modifier = Modifier.padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with animation status indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selected) "Expanded View" else "Click to Expand",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Animated icon with rotation
                    IconButton(
                        onClick = { selected = !selected },
                        modifier = Modifier
                            .size(32.dp)
                            .rotate(iconRotation)
                    ) {
                        Icon(
                            imageVector = if (selected) Icons.Filled.ArrowDropDown else Icons.Filled.Add,
                            contentDescription = if (selected) "Collapse" else "Expand",
                            tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Animated progress indicator
                if (selected) {
                    LinearProgressIndicator(
                        progress = { 0.8f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    )
                }

                // Animated content visibility with custom enter/exit animations
                transition.AnimatedVisibility(
                    visible = { it },
                    enter = slideInVertically(
                        initialOffsetY = { fullHeight -> fullHeight },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    ) + expandVertically(
                        expandFrom = Alignment.Top
                    ) + fadeIn(),
                    exit = slideOutVertically(
                        targetOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(
                            durationMillis = 150,
                            easing = LinearOutSlowInEasing
                        )
                    ) + shrinkVertically() + fadeOut()
                ) {
                    Column(
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        // Main content
                        Text(
                            text = "This card demonstrates complex animations in Jetpack Compose, including coordinated transitions, physics-based animations, and visibility animations.",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Animated feature dots
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AnimatedDot(color = Color(0xFF6200EE), delay = 0)
                            AnimatedDot(color = Color(0xFF03DAC5), delay = 150)
                            AnimatedDot(color = Color(0xFFFF8800), delay = 300)
                        }
                    }
                }
            }
        }

        // Instructions
        AnimatedVisibility(
            visible = !selected,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Text(
                text = "Tap the card to see animations",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

/**
 * Animated Dot Component
 *
 * Creates a pulsating dot with delay parameter to create a wave effect
 * when multiple dots are displayed together.
 */
@Composable
fun AnimatedDot(color: Color, delay: Int) {
    // Create infinite transition for continuous animation
    val infiniteTransition = rememberInfiniteTransition(label = "Pulsating Dot")

    // Animate the scale of the dot
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                delayMillis = delay,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Dot Scale"
    )

    Box(
        modifier = Modifier
            .size(16.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(color)
    )
}