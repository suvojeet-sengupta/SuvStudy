package com.suvojeet.suvstudy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.suvojeet.suvstudy.ui.viewmodel.FocusViewModel
import com.suvojeet.suvstudy.ui.viewmodel.TimerState
import org.koin.androidx.compose.koinViewModel

@Composable
fun FocusScreen(
    viewModel: FocusViewModel = koinViewModel()
) {
    val recentSessions by viewModel.recentSessions.collectAsState()
    val timerState by viewModel.timerState.collectAsState()
    val timeLeft by viewModel.timeLeftInSeconds.collectAsState()

    val minutes = String.format("%02d", timeLeft / 60)
    val seconds = String.format("%02d", timeLeft % 60)
    
    val progress = timeLeft.toFloat() / (25 * 60)

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
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    strokeWidth = 8.dp
                )
                Text(
                    text = "$minutes:$seconds",
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 72.sp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(64.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (timerState == TimerState.RUNNING) {
                FloatingActionButton(
                    onClick = { viewModel.pauseTimer() },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Icon(Icons.Filled.Pause, contentDescription = "Pause")
                }
            } else {
                FloatingActionButton(
                    onClick = { viewModel.startTimer() },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = "Start")
                }
            }
            
            if (timerState != TimerState.IDLE) {
                FloatingActionButton(
                    onClick = { viewModel.stopTimer() },
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ) {
                    Icon(Icons.Filled.Stop, contentDescription = "Stop", tint = MaterialTheme.colorScheme.onErrorContainer)
                }
            }
        }
    }
}
