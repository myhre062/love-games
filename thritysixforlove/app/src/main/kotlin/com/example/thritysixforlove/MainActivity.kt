package com.example.thritysixforlove

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.thritysixforlove.compose.MainScreen
import com.example.thritysixforlove.compose.NavigationComponent
import com.example.thritysixforlove.ui.theme.ThritysixforloveTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThritysixforloveTheme {
                val navController = rememberNavController()
                NavigationComponent(navController)
            }
        }
    }
}


@Preview
@Composable
fun DefaultPreview() {
    ThritysixforloveTheme {
        MainScreen(navController = rememberNavController())
    }
}