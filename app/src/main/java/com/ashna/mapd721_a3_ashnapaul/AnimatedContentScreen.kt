package com.ashna.mapd721_a3_ashnapaul

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.sin
import kotlinx.coroutines.launch

// Enable experimental animation APIs
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedContentScreen() {
    // Coroutine scope for managing animations
    val scope = rememberCoroutineScope()

    // State management for different UI states
    var currentState by remember { mutableStateOf(AnimationState.COUNT) }
    var count by remember { mutableIntStateOf(0) }
    var expanded by remember { mutableStateOf(false) }

    // Animatable for continuous background rotation
    val rotation = remember { Animatable(0f) }

    // Background rotation animation (infinite loop)
    LaunchedEffect(key1 = Unit) {
        rotation.animateTo(
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(10000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    // Main container with gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF2E3192),
                        Color(0xFF1BFFFF)
                    ),
                    center = Offset.Infinite,
                    radius = 1200f
                )
            )
    ) {
        // Rotating background elements container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationZ = rotation.value  // Apply continuous rotation
                }
        ) {
            // Create 5 animated floating elements
            repeat(5) { index ->
                // Vertical movement animation for each element
                val animatedOffset = remember { Animatable(0f) }
                LaunchedEffect(key1 = Unit) {
                    animatedOffset.animateTo(
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(3000, delayMillis = index * 500, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        )
                    )
                }

                // Individual floating element
                Box(
                    modifier = Modifier
                        .size(100.dp + (index * 30).dp)
                        .offset(
                            x = (index * 50).dp,
                            y = with(LocalDensity.current) { (sin(animatedOffset.value * Math.PI.toFloat()) * 100).dp }
                        )
                        .scale(0.5f + (animatedOffset.value * 0.5f))
                        .graphicsLayer {
                            alpha = 0.15f  // Semi-transparent effect
                        }
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                        .align(Alignment.Center)
                )
            }
        }

        // Main content column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Animated title text
            Text(
                text = "Animation Showcase",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .graphicsLayer {
                        // Create subtle pulsing effect using rotation value
                        val scale = 1f + (sin(rotation.value * 0.05f) * 0.05f)
                        scaleX = scale
                        scaleY = scale
                    }
            )

            // State selection controls
            AnimatedTabSelector(
                currentState = currentState,
                onStateChanged = { currentState = it }
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Main animated content area with custom transitions
            AnimatedContent(
                targetState = currentState,
                transitionSpec = {
                    // Custom transition logic based on state changes
                    when (targetState) {
                        AnimationState.COUNT -> {
                            // Slide from right to left with fade
                            (slideIn(initialOffset = { IntOffset(it.width, 0) }) + fadeIn()) with
                                    (slideOut(targetOffset = { IntOffset(-it.width, 0) }) + fadeOut())
                        }
                        AnimationState.EXPAND -> {
                            // Slide from bottom to top with fade
                            (slideIn(initialOffset = { IntOffset(0, it.height) }) + fadeIn()) with
                                    (slideOut(targetOffset = { IntOffset(0, -it.height) }) + fadeOut())
                        }
                        else -> {
                            // Scale and fade transition
                            (scaleIn(initialScale = 0.8f) + fadeIn()) with
                                    (scaleOut(targetScale = 1.2f) + fadeOut())
                        }
                    }.using(SizeTransform(clip = false))  // Prevent clipping during animation
                },
                label = "main content"
            ) { targetState ->
                // Container card for content
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1D1E33).copy(alpha = 0.85f)
                    ),
                    border = BorderStroke(2.dp, Color(0xFF5D5FEF).copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    modifier = Modifier
                        .width(300.dp)
                        .fillMaxHeight(0.6f)
                        .padding(16.dp)
                ) {
                    // Content based on current state
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        when (targetState) {
                            AnimationState.COUNT -> EnhancedCountAnimation(
                                count = count,
                                onIncrement = { scope.launch { count++ } },
                                onDecrement = { scope.launch { if (count > 0) count-- } }
                            )
                            AnimationState.EXPAND -> EnhancedExpandAnimation(expanded) { expanded = !expanded }
                            AnimationState.CROSSFADE -> EnhancedCrossfadeAnimation()
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun EnhancedExpandAnimation(expanded: Boolean, onToggle: () -> Unit) {
    // Column to hold the expansion elements
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, // Center items horizontally
        verticalArrangement = Arrangement.spacedBy(16.dp) // Add space between items
    ) {
        // Title text
        Text(
            text = "Expansion Animation",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center // Center the text
        )

        // Create an infinite transition for the glow effect
        val infiniteTransition = rememberInfiniteTransition(label = "glow")

        // Animate the alpha value for the glow effect
        val glowAlpha by infiniteTransition.animateFloat(
            initialValue = 0.2f,
            targetValue = 0.7f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500), // Animation duration of 1.5 seconds
                repeatMode = RepeatMode.Reverse // Reverse the animation after each iteration
            ),
            label = "glow alpha"
        )

        // Animate the rotation angle based on the expanded state
        val rotationAngle by animateFloatAsState(
            targetValue = if (expanded) 360f else 0f, // Rotate 360 degrees if expanded, otherwise 0
            animationSpec = tween(
                durationMillis = 1000, // Animation duration of 1 second
                easing = EaseInOutQuart // Use an ease-in-out-quart easing function
            ),
            label = "rotation"
        )

        // Box to contain the animated circle
        Box(
            contentAlignment = Alignment.Center, // Center the content inside the box
            modifier = Modifier.padding(16.dp) // Add padding around the box
        ) {
            // Glow effect
            Box(
                modifier = Modifier
                    .size(if (expanded) 220.dp else 110.dp) // Size changes based on expanded state
                    .graphicsLayer {
                        alpha = glowAlpha // Apply the glow alpha animation
                    }
                    .background(
                        color = if (expanded) Color(0xFF00FFA3) else Color(0xFF4269E1), // Color changes based on expanded state
                        shape = CircleShape // Make the background a circle
                    )
            )

            // Main animated circle
            Box(
                modifier = Modifier
                    .clip(CircleShape) // Clip the box to a circle shape
                    .clickable { onToggle() } // Call the onToggle function when clicked
                    .size(if (expanded) 200.dp else 100.dp) // Size changes based on expanded state
                    .rotate(rotationAngle) // Apply the rotation animation
                    .animateContentSize( // Animate the content size
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy, // Use a bouncy damping ratio
                            stiffness = Spring.StiffnessMedium // Use a medium stiffness
                        )
                    )
                    .background(
                        brush = Brush.linearGradient( // Use a linear gradient for the background
                            colors = if (expanded) {
                                listOf(
                                    Color(0xFF00FFA3),
                                    Color(0xFF00E0FF)
                                )
                            } else {
                                listOf(
                                    Color(0xFF4269E1),
                                    Color(0xFF2332BD)
                                )
                            }
                        )
                    ),
                contentAlignment = Alignment.Center // Center the content inside the box
            ) {
                // Transition for the text animation
                val textTransition = updateTransition(expanded, label = "text animation")

                // Animate the text scale based on the expanded state
                val textScale by textTransition.animateFloat(
                    label = "text scale",
                    transitionSpec = { spring(stiffness = Spring.StiffnessLow) } // Use a spring animation
                ) { isExpanded ->
                    if (isExpanded) 1.5f else 1f // Scale up if expanded, otherwise stay at 1
                }

                // Animate the arrow rotation based on the expanded state
                val arrowRotation by textTransition.animateFloat(
                    label = "arrow rotation",
                    transitionSpec = { tween(500) } // Use a tween animation with a duration of 500ms
                ) { isExpanded ->
                    if (isExpanded) 180f else 0f // Rotate 180 degrees if expanded, otherwise 0
                }

                // Column to hold the text and icon
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally, // Center items horizontally
                    verticalArrangement = Arrangement.Center, // Center items vertically
                    modifier = Modifier.graphicsLayer {
                        scaleX = textScale // Apply the text scale animation
                        scaleY = textScale // Apply the text scale animation
                    }
                ) {
                    // Text to display the state
                    Text(
                        text = if (expanded) "Expanded" else "Tap Me", // Display different text based on expanded state
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = if (expanded) 22.sp else 16.sp // Change font size based on expanded state
                    )

                    // Add space between the text and the icon
                    Spacer(modifier = Modifier.height(4.dp))

                    // Icon to indicate the toggle state
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp, // Use an upward arrow icon
                        contentDescription = "Toggle", // Content description for accessibility
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp) // Set the size of the icon
                            .rotate(arrowRotation) // Apply the arrow rotation animation
                    )
                }
            }
        }
    }
}








@Composable
fun EnhancedCrossfadeAnimation() {
    // State to hold the current page (A or B)
    var currentPage by remember { mutableStateOf("A") }
    // Coroutine scope for launching animations
    val scope = rememberCoroutineScope()
    // Animatable to handle rotation animation
    val rotation = remember { Animatable(0f) }

    // LaunchedEffect to trigger rotation animation when the current page changes
    LaunchedEffect(currentPage) {
        // Snap rotation to 0 before starting the animation
        rotation.snapTo(0f)
        // Animate rotation to 360 degrees with a custom easing
        rotation.animateTo(
            targetValue = 360f,
            animationSpec = tween(1200, easing = EaseOutBack)
        )
    }

    // Column to hold the entire content
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title text
        Text(
            text = "Crossfade Animation",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        // Box to hold the rotating card
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .graphicsLayer {
                    // Apply rotation to the Y-axis
                    rotationY = rotation.value
                    // Adjust camera distance for 3D effect
                    cameraDistance = 12f * density
                }
                .size(200.dp)
        ) {
            // Crossfade to animate between different screens
            Crossfade(
                targetState = currentPage, // The state to crossfade to
                animationSpec = tween(800), // Animation duration
                label = "enhanced crossfade"
            ) { screen ->
                // Card to display the content for each screen
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent // Make the card background transparent
                    ),
                    border = BorderStroke(
                        width = 2.dp,
                        color = if (screen == "A") Color(0xFFF43F5E) else Color(0xFF60A5FA) // Border color changes based on the screen
                    ),
                    elevation = CardDefaults.cardElevation(6.dp),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .size(200.dp)
                        .graphicsLayer {
                            // Add a subtle wobble animation
                            val wobble = sin(rotation.value * 0.05f) * 2f
                            rotationZ = wobble
                        }
                ) {
                    // Box to hold the content of the card
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = if (screen == "A") {
                                        listOf(
                                            Color(0xFFFDA4AF).copy(alpha = 0.9f),
                                            Color(0xFFF43F5E).copy(alpha = 0.9f)
                                        )
                                    } else {
                                        listOf(
                                            Color(0xFF93C5FD).copy(alpha = 0.9f),
                                            Color(0xFF3B82F6).copy(alpha = 0.9f)
                                        )
                                    }
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        // Column to hold the text elements
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // Text to display the page name
                            Text(
                                text = "Page $screen",
                                color = Color.White,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold
                            )

                            // State to handle the animating dots
                            var animatingDots by remember { mutableStateOf(0) }
                            // LaunchedEffect to animate the dots
                            LaunchedEffect(screen) {
                                while (true) {
                                    animatingDots = (animatingDots + 1) % 4
                                    delay(500)
                                }
                            }

                            // Text to display the animating dots
                            Text(
                                text = "Animating" + ".".repeat(animatingDots),
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(1.dp))

        // Button to switch between pages
        Button(
            onClick = {
                scope.launch {
                    // Toggle the current page
                    currentPage = if (currentPage == "A") "B" else "A"
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5D5FEF)
            ),
            elevation = ButtonDefaults.buttonElevation(8.dp),
            modifier = Modifier.width(180.dp)
        ) {
            // Text to display the button label
            Text(
                "Switch to Page ${if (currentPage == "A") "B" else "A"}",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// Enum to define the different animation states
enum class AnimationState {
    COUNT, EXPAND, CROSSFADE
}
