package com.ashna.mapd721_a3_ashnapaul

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ValueBasedAnimation1Screen() {
    var selected by remember { mutableStateOf(false) }
    val transition = updateTransition(selected, label = "selection")

    val borderColor by transition.animateColor(label = "border color") { isSelected ->
        if (isSelected) Color.Magenta else Color.White
    }
    val elevation by transition.animateDp(label = "elevation") { isSelected ->
        if (isSelected) 10.dp else 2.dp
    }

    Surface(
        onClick = { selected = !selected },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, borderColor),
        shadowElevation = elevation
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Click me to animate!")
            transition.AnimatedVisibility(
                visible = { it },
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Text("Additional content when selected")
            }
        }
    }
}
