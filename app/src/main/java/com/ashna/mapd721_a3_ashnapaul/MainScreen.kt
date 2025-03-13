package com.ashna.mapd721_a3_ashnapaul

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Student Info: Ashna Paul", style = MaterialTheme.typography.headlineMedium)
        Text("Student ID: 301479554", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { navController.navigate("screen1") }) {
            Text("Animated Content")
        }
        Button(onClick = { navController.navigate("screen2") }) {
            Text("Value Based Animation 1")
        }
        Button(onClick = { navController.navigate("screen3") }) {
            Text("Value Based Animation 2")
        }
        Button(onClick = { navController.navigate("screen4") }) {
            Text("Gesture Based Animation")
        }
    }
}
