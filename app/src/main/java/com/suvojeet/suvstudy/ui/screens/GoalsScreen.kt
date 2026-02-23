package com.suvojeet.suvstudy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.suvojeet.suvstudy.ui.viewmodel.GoalsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    viewModel: GoalsViewModel = koinViewModel()
) {
    val goals by viewModel.goals.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var newGoalTitle by remember { mutableStateOf("") }
    var newGoalTarget by remember { mutableStateOf("") }
    var newGoalUnit by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = { Icon(Icons.Filled.Add, contentDescription = "Add Goal") },
                text = { Text("Add Goal") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                var sliderValue by remember(goal.currentScore) { mutableStateOf(goal.currentScore) }
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { dismissValue ->
                        if (dismissValue == SwipeToDismissBoxValue.EndToStart || dismissValue == SwipeToDismissBoxValue.StartToEnd) {
                            viewModel.deleteGoal(goal)
                            true
                        } else {
                            false
                        }
                    }
                )

                SwipeToDismissBox(
                    state = dismissState,
                    backgroundContent = {
                        val color = MaterialTheme.colorScheme.errorContainer
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(color, RoundedCornerShape(24.dp)),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete Goal",
                                modifier = Modifier.padding(end = 24.dp),
                                tint = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                ) {
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
                                    Text("Actual: ${String.format("%.1f", sliderValue)}${goal.unit}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary)
                                    Text("Target: ${goal.targetScore}${goal.unit}", fontWeight = FontWeight.Medium)
                                }
                                
                                Slider(
                                    value = sliderValue,
                                    onValueChange = { newValue -> sliderValue = newValue },
                                    onValueChangeFinished = { viewModel.updateProgress(goal, sliderValue) },
                                    valueRange = 0f..goal.targetScore,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

    if (showAddDialog) {
        ModalBottomSheet(
            onDismissRequest = { showAddDialog = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "New Goal",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = newGoalTitle,
                    onValueChange = { newGoalTitle = it },
                    label = { Text("Goal Title (e.g., Target Score)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = newGoalTarget,
                        onValueChange = { newGoalTarget = it },
                        label = { Text("Target") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = newGoalUnit,
                        onValueChange = { newGoalUnit = it },
                        label = { Text("Unit (%, pts)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Button(
                    onClick = {
                        if (newGoalTitle.isNotBlank() && newGoalTarget.isNotBlank()) {
                            val target = newGoalTarget.toFloatOrNull() ?: 0f
                            val unit = newGoalUnit.ifBlank { "" }
                            viewModel.addGoal(newGoalTitle, target, unit)
                            showAddDialog = false
                            newGoalTitle = ""
                            newGoalTarget = ""
                            newGoalUnit = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = newGoalTitle.isNotBlank() && newGoalTarget.isNotBlank()
                ) {
                    Text("Save Goal")
                }
            }
        }
    }
}
