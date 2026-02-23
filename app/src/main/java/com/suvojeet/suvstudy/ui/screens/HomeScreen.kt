package com.suvojeet.suvstudy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.suvojeet.suvstudy.domain.model.Subject
import com.suvojeet.suvstudy.ui.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val upcomingTasks by viewModel.upcomingTasks.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val timerState by viewModel.timerState.collectAsState()
    
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var newTaskTitle by remember { mutableStateOf("") }
    var newTaskDesc by remember { mutableStateOf("") }
    var expandedSubjectDropdown by remember { mutableStateOf(false) }
    var selectedSubject by remember { mutableStateOf<Subject?>(null) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddTaskDialog = true },
                icon = { Icon(Icons.Filled.Add, contentDescription = "Add Task") },
                text = { Text("Add Task") }
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
        // Welcoming Greeting
        Column {
            Text(
                text = "Good Morning,",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = "Suvojeet \uD83D\uDC4B", // wave
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Active Timer Card
        if (timerState.activeTaskId != null) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Active: ${timerState.taskTitle}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        val mins = timerState.elapsedSeconds / 60
                        val secs = timerState.elapsedSeconds % 60
                        val timeString = "${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}"
                        Text(timeString, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
                    }
                    Row {
                        IconButton(onClick = { 
                            if (timerState.isRunning) viewModel.pauseTimer() 
                            else viewModel.resumeTimer()
                        }) {
                            Icon(if (timerState.isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow, contentDescription = "Pause/Resume")
                        }
                        IconButton(onClick = { viewModel.stopTimer() }) {
                            Icon(Icons.Filled.Stop, contentDescription = "Stop")
                        }
                    }
                }
            }
        }

        // Quick List / Upcoming
        Text(
            text = "Upcoming Deadlines",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )
        
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (upcomingTasks.isEmpty()) {
                Text(
                    text = "No upcoming deadlines! 🎉",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            } else {
                Column(modifier = Modifier.padding(16.dp)) {
                    upcomingTasks.forEach { task ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(task.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                Text("Due: ${task.dueDate?.toLocalDate() ?: "Anytime"}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
                            }
                            Row {
                                IconButton(onClick = { viewModel.startTimer(task) }) {
                                    Icon(Icons.Filled.PlayArrow, contentDescription = "Start Timer", tint = MaterialTheme.colorScheme.primary)
                                }
                                IconButton(onClick = { viewModel.toggleTaskCompletion(task) }) {
                                    Icon(
                                        imageVector = if (task.isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                                        contentDescription = "Toggle Completion",
                                        tint = if (task.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

    if (showAddTaskDialog) {
        ModalBottomSheet(
            onDismissRequest = { showAddTaskDialog = false },
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
                    text = "New Task",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = newTaskTitle,
                    onValueChange = { newTaskTitle = it },
                    label = { Text("Task Title (e.g., Assignment 1)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = newTaskDesc,
                    onValueChange = { newTaskDesc = it },
                    label = { Text("Description (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                ExposedDropdownMenuBox(
                    expanded = expandedSubjectDropdown,
                    onExpandedChange = { expandedSubjectDropdown = !expandedSubjectDropdown }
                ) {
                    OutlinedTextField(
                        value = selectedSubject?.name ?: "Select a Subject",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSubjectDropdown) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        label = { Text("Subject") }
                    )
                    ExposedDropdownMenu(
                        expanded = expandedSubjectDropdown,
                        onDismissRequest = { expandedSubjectDropdown = false }
                    ) {
                        if (subjects.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("No subjects available. Please create one.") },
                                onClick = { expandedSubjectDropdown = false }
                            )
                        } else {
                            subjects.forEach { subject ->
                                DropdownMenuItem(
                                    text = { Text(subject.name) },
                                    onClick = {
                                        selectedSubject = subject
                                        expandedSubjectDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        if (newTaskTitle.isNotBlank() && selectedSubject != null) {
                            viewModel.addTask(newTaskTitle, newTaskDesc, selectedSubject!!.id)
                            showAddTaskDialog = false
                            newTaskTitle = ""
                            newTaskDesc = ""
                            selectedSubject = null
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = newTaskTitle.isNotBlank() && selectedSubject != null
                ) {
                    Text("Save Task")
                }
            }
        }
    }
}
