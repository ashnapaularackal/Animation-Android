package com.ashna.mapd721_a3_ashnapaul

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.animation.ExperimentalAnimationApi

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedTabSelector(
    currentState: AnimationState,
    onStateChanged: (AnimationState) -> Unit
) {
    // Get all possible animation states
    val options = AnimationState.values()

    // Create a card to contain the tab selector
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1D1E33).copy(alpha = 0.7f)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        // Row to hold all tab options
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Create a tab for each animation state option
            options.forEach { state ->
                val selected = state == currentState
                // Create a transition for smooth animations between selected and unselected states
                val transition = updateTransition(selected, label = "tab transition")

                // Animate the background color
                val backgroundColor by transition.animateColor(
                    label = "background color",
                    transitionSpec = { tween(300) }
                ) { isSelected ->
                    if (isSelected) Color(0xFF5D5FEF) else Color.Transparent
                }

                // Animate the text color
                val textColor by transition.animateColor(
                    label = "text color",
                    transitionSpec = { tween(300) }
                ) { isSelected ->
                    if (isSelected) Color.White else Color.White.copy(alpha = 0.6f)
                }

                // Animate the scale of the tab
                val scale by transition.animateFloat(
                    label = "scale",
                    transitionSpec = { spring(stiffness = Spring.StiffnessLow) }
                ) { isSelected ->
                    if (isSelected) 1.1f else 1f
                }

                // Create the individual tab
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
                    // Tab text
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
