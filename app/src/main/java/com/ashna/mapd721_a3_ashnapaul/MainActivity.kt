package com.ashna.mapd721_a3_ashnapaul
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "main") {
                        composable("main") { MainScreen(navController) }
                        composable("screen1") { AnimatedContentScreen() }
                        composable("screen2") { ValueBasedAnimation1Screen() }
                        composable("screen3") { ValueBasedAnimation2Screen() }
                        composable("screen4") { GestureBasedAnimationScreen() }
                    }
                }
            }
        }
    }
}
