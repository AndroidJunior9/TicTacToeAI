package com.androidjunior9.tictactoeai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.androidjunior9.tictactoeai.ui.theme.TicTacToeAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeAITheme {
                val viewModel = MainViewModel()
                GameScreen(
                    viewModel = viewModel,
                )
            }
        }
    }
}

