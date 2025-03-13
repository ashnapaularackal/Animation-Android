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
import androidx.compose.material.icons.filled.Add
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
import kotlin.math.sin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedContentScreen() {
    val scope = rememberCoroutineScope()
    var currentState by remember { mutableStateOf(AnimationState.COUNT) }
    var count by remember { mutableIntStateOf(0) }
    var expanded by remember { mutableStateOf(false) }

    // For rotating background animation
    val rotation = remember { Animatable(0f) }

    // Background animation
    LaunchedEffect(key1 = Unit) {
        rotation.animateTo(
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(10000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

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
        // Rotating background elements
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationZ = rotation.value
                }
        ) {
            repeat(5) { index ->
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

                Box(
                    modifier = Modifier
                        .size(100.dp + (index * 30).dp)
                        .offset(
                            x = (index * 50).dp,
                            y = with(LocalDensity.current) { (sin(animatedOffset.value * Math.PI.toFloat()) * 100).dp }
                        )
                        .scale(0.5f + (animatedOffset.value * 0.5f))
                        .graphicsLayer {
                            alpha = 0.15f
                        }
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                        .align(Alignment.Center)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Animation Showcase",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .graphicsLayer {
                        val scale = 1f + (sin(rotation.value * 0.05f) * 0.05f)
                        scaleX = scale
                        scaleY = scale
                    }
            )

            // Animated tab selector
            AnimatedTabSelector(
                currentState = currentState,
                onStateChanged = { currentState = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Main content with advanced transition
            AnimatedContent(
                targetState = currentState,
                transitionSpec = {
                    when (targetState) {
                        AnimationState.COUNT -> {
                            (slideIn(initialOffset = { IntOffset(it.width, 0) }) + fadeIn()) with
                                    (slideOut(targetOffset = { IntOffset(-it.width, 0) }) + fadeOut())
                        }
                        AnimationState.EXPAND -> {
                            (slideIn(initialOffset = { IntOffset(0, it.height) }) + fadeIn()) with
                                    (slideOut(targetOffset = { IntOffset(0, -it.height) }) + fadeOut())
                        }
                        else -> {
                            (scaleIn(initialScale = 0.8f) + fadeIn()) with
                                    (scaleOut(targetScale = 1.2f) + fadeOut())
                        }
                    }.using(SizeTransform(clip = false))
                },
                label = "main content"
            ) { targetState ->
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
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        when (targetState) {
                            AnimationState.COUNT -> EnhancedCountAnimation(
                                count = count,
                                onIncrement = {
                                    scope.launch {
                                        count++
                                    }
                                },
                                onDecrement = {
                                    scope.launch {
                                        if (count > 0) count--
                                    }
                                }
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedTabSelector(
    currentState: AnimationState,
    onStateChanged: (AnimationState) -> Unit
) {
    val options = AnimationState.values()

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1D1E33).copy(alpha = 0.7f)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            options.forEach { state ->
                val selected = state == currentState
                val transition = updateTransition(selected, label = "tab transition")

                val backgroundColor by transition.animateColor(
                    label = "background color",
                    transitionSpec = { tween(300) }
                ) { isSelected ->
                    if (isSelected) Color(0xFF5D5FEF) else Color.Transparent
                }

                val textColor by transition.animateColor(
                    label = "text color",
                    transitionSpec = { tween(300) }
                ) { isSelected ->
                    if (isSelected) Color.White else Color.White.copy(alpha = 0.6f)
                }

                val scale by transition.animateFloat(
                    label = "scale",
                    transitionSpec = { spring(stiffness = Spring.StiffnessLow) }
                ) { isSelected ->
                    if (isSelected) 1.1f else 1f
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(backgroundColor)
                        .clickable { onStateChanged(state) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                ) {
                    Text(
                        text = state.name.lowercase().replaceFirstChar { it.uppercase() },
                        color = textColor,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EnhancedCountAnimation(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "button pulse"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Counter Animation",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        // Counter digits with individual animations
        Box(
            modifier = Modifier
                .size(140.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF6F3CE9),
                            Color(0xFF33C9FF)
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = count,
                transitionSpec = {
                    val direction = if (targetState > initialState) {
                        // Count increasing
                        (slideIn(initialOffset = { IntOffset(0, it.height) }) + fadeIn()) with
                                (slideOut(targetOffset = { IntOffset(0, -it.height) }) + fadeOut())
                    } else {
                        // Count decreasing
                        (slideIn(initialOffset = { IntOffset(0, -it.height) }) + fadeIn()) with
                                (slideOut(targetOffset = { IntOffset(0, it.height) }) + fadeOut())
                    }
                    direction.using(SizeTransform(clip = false))
                },
                label = "digit animation"
            ) { targetCount ->
                Text(
                    text = "$targetCount",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Decrement button with fixed functionality
            FloatingActionButton(
                onClick = onDecrement,
                containerColor = Color(0xFF5D5FEF),
                contentColor = Color.White,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Decrement",
                    modifier = Modifier.size(24.dp)
                )
            }

            // Increment button
            FloatingActionButton(
                onClick = onIncrement,
                containerColor = Color(0xFF5D5FEF),
                contentColor = Color.White,
                modifier = Modifier
                    .scale(scale)
                    .size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Increment",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun EnhancedExpandAnimation(expanded: Boolean, onToggle: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Expansion Animation",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        val infiniteTransition = rememberInfiniteTransition(label = "glow")
        val glowAlpha by infiniteTransition.animateFloat(
            initialValue = 0.2f,
            targetValue = 0.7f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500),
                repeatMode = RepeatMode.Reverse
            ),
            label = "glow alpha"
        )

        val rotationAngle by animateFloatAsState(
            targetValue = if (expanded) 360f else 0f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = EaseInOutQuart
            ),
            label = "rotation"
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            // Glow effect
            Box(
                modifier = Modifier
                    .size(if (expanded) 220.dp else 110.dp)
                    .graphicsLayer {
                        alpha = glowAlpha
                    }
                    .background(
                        color = if (expanded) Color(0xFF00FFA3) else Color(0xFF4269E1),
                        shape = CircleShape
                    )
            )

            // Main animated circle
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onToggle() }
                    .size(if (expanded) 200.dp else 100.dp)
                    .rotate(rotationAngle)
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                    .background(
                        brush = Brush.linearGradient(
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
                contentAlignment = Alignment.Center
            ) {
                val textTransition = updateTransition(expanded, label = "text animation")
                val textScale by textTransition.animateFloat(
                    label = "text scale",
                    transitionSpec = { spring(stiffness = Spring.StiffnessLow) }
                ) { isExpanded ->
                    if (isExpanded) 1.5f else 1f
                }

                val arrowRotation by textTransition.animateFloat(
                    label = "arrow rotation",
                    transitionSpec = { tween(500) }
                ) { isExpanded ->
                    if (isExpanded) 180f else 0f
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.graphicsLayer {
                        scaleX = textScale
                        scaleY = textScale
                    }
                ) {
                    Text(
                        text = if (expanded) "Expanded" else "Tap Me",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = if (expanded) 22.sp else 16.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Toggle",
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .rotate(arrowRotation)
                    )
                }
            }
        }
    }
}

@Composable
fun EnhancedCrossfadeAnimation() {
    var currentPage by remember { mutableStateOf("A") }
    val scope = rememberCoroutineScope()
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(currentPage) {
        // Rotate card when page changes
        rotation.snapTo(0f)
        rotation.animateTo(
            targetValue = 360f,
            animationSpec = tween(1200, easing = EaseOutBack)
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Crossfade Animation",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .graphicsLayer {
                    rotationY = rotation.value
                    cameraDistance = 12f * density
                }
                .size(200.dp)
        ) {
            Crossfade(
                targetState = currentPage,
                animationSpec = tween(800),
                label = "enhanced crossfade"
            ) { screen ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(
                        width = 2.dp,
                        color = if (screen == "A") Color(0xFFF43F5E) else Color(0xFF60A5FA)
                    ),
                    elevation = CardDefaults.cardElevation(6.dp),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .size(200.dp)
                        .graphicsLayer {
                            // Subtle continuous animation
                            val wobble = sin(rotation.value * 0.05f) * 2f
                            rotationZ = wobble
                        }
                ) {
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
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Page $screen",
                                color = Color.White,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold
                            )

                            var animatingDots by remember { mutableStateOf(0) }
                            LaunchedEffect(screen) {
                                while (true) {
                                    animatingDots = (animatingDots + 1) % 4
                                    delay(500)
                                }
                            }

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

        // Improved visibility for the switch button
        Button(
            onClick = {
                scope.launch {
                    currentPage = if (currentPage == "A") "B" else "A"
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5D5FEF)
            ),
            elevation = ButtonDefaults.buttonElevation(8.dp),
            modifier = Modifier.width(180.dp)
        ) {
            Text(
                "Switch to Page ${if (currentPage == "A") "B" else "A"}",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

enum class AnimationState {
    COUNT, EXPAND, CROSSFADE
}