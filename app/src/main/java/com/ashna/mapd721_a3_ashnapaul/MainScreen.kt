package com.ashna.mapd721_a3_ashnapaul

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MainScreen(navController: NavController) {
    // Define gradient colors
    val gradientColors = listOf(Color(0xFF67e6dc), Color(0xFF7d5fff))

    // Main column with gradient background
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(colors = gradientColors)
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Student information
        Text("Student Name: Ashna Paul", style = MaterialTheme.typography.headlineMedium)
        Text("Student ID: 301479554", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(32.dp))

        // Navigation buttons
        NavigationButton(navController, "screen1", "Animated Content")
        NavigationButton(navController, "screen2", "Value Based Animation 1")
        NavigationButton(navController, "screen3", "Value Based Animation 2")
        NavigationButton(navController, "screen4", "Gesture Based Animation")
    }
}

// Reusable navigation button composable
@Composable
fun NavigationButton(navController: NavController, route: String, text: String) {
    Button(
        onClick = { navController.navigate(route) },
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(text)
    }
}
