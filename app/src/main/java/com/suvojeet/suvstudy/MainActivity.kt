package com.suvojeet.suvstudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.suvojeet.suvstudy.ui.navigation.MainScreen
import com.suvojeet.suvstudy.ui.theme.SuvStudyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuvStudyTheme {
                MainScreen()
            }
        }
    }
}