package com.adamdawi.flashcardcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.adamdawi.flashcardcompose.ui.theme.FlashCardComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashCardComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MealCard(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}