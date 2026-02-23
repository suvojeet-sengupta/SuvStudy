package com.suvojeet.suvstudy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.suvojeet.suvstudy.ui.viewmodel.GoalsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun GoalsScreen(
    viewModel: GoalsViewModel = koinViewModel()
) {
    val goals by viewModel.goals.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Reality Check",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        if (goals.isEmpty()) {
            Text(
                text = "No goals set yet.",
                modifier = Modifier.padding(top = 16.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        } else {
            goals.forEach { goal ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = goal.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                        
                        // Mock progress bar
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Target: ${goal.targetScore}${goal.unit}", fontWeight = FontWeight.Medium)
                                Text("Actual: ${goal.currentScore}${goal.unit}")
                            }
                            LinearProgressIndicator(
                                progress = { goal.progress },
                                modifier = Modifier.fillMaxWidth().height(12.dp).padding(vertical = 2.dp),
                                color = MaterialTheme.colorScheme.tertiary,
                                trackColor = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.2f),
                            )
                        }
                    }
                }
            }
        }
    }
}
