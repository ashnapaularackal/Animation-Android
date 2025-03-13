package com.ashna.mapd721_a3_ashnapaul

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AnimatedContentScreen() {
    var currentPage by remember { mutableStateOf("A") }

    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = { currentPage = if (currentPage == "A") "B" else "A" }) {
            Text("Toggle Page")
        }

        Crossfade(targetState = currentPage, label = "page transition") { screen ->
            when (screen) {
                "A" -> Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red)) {
                    Text("Page A", color = Color.White)
                }
                "B" -> Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Blue)) {
                    Text("Page B", color = Color.White)
                }
            }
        }
    }
}
