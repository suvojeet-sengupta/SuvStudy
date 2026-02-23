package com.suvojeet.suvstudy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.suvojeet.suvstudy.ui.viewmodel.FocusViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FocusScreen(
    viewModel: FocusViewModel = koinViewModel()
) {
    val recentSessions by viewModel.recentSessions.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Deep Work",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Stay focused, avoid distractions.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(64.dp))

        // Timer Display
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(280.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "25:00",
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 72.sp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(64.dp))

        Button(
            onClick = { /* TODO */ },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(56.dp)
        ) {
            Text("Start Pomodoro", style = MaterialTheme.typography.titleMedium)
        }
    }
}
