package com.suvojeet.suvstudy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.suvojeet.suvstudy.ui.viewmodel.InsightsViewModel
import org.koin.androidx.compose.koinViewModel

// A simplistic color palette for bar charts
val ChartColors = listOf(
    Color(0xFF6C63FF),
    Color(0xFFFF6584),
    Color(0xFF4CAF50),
    Color(0xFFFF9800),
    Color(0xFF03A9F4),
    Color(0xFF9C27B0)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    viewModel: InsightsViewModel = koinViewModel()
) {
    val stats by viewModel.weeklyStats.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Insights", fontWeight = FontWeight.Bold) }
            )
        }
    ) { paddingValues ->
        if (stats.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("No study data yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            val maxMinutes = stats.maxOfOrNull { it.totalMinutes } ?: 1
            // Prevent division by zero if all are 0
            val safeMax = if (maxMinutes > 0) maxMinutes else 1

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Text(
                        "Last 7 Days Activity",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
                
                items(stats) { stat ->
                    val color = ChartColors[stat.colorIndex % ChartColors.size]
                    val fraction = stat.totalMinutes.toFloat() / safeMax.toFloat()
                    
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(stat.subjectName, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyLarge)
                            Text("${stat.totalMinutes} mins", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(fraction = Math.max(0.02f, fraction))
                                    .fillMaxHeight()
                                    .background(color = color)
                            )
                        }
                    }
                }
                
                item {
                    val totalStudyTime = stats.sumOf { it.totalMinutes }
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Total Study Time", style = MaterialTheme.typography.bodyLarge)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "${totalStudyTime / 60}h ${totalStudyTime % 60}m",
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}
