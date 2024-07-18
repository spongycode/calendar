package com.spongycode.calendar.screen.calendar

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.spongycode.calendar.domain.model.Task
import com.spongycode.calendar.screen.calendar.components.AddTaskDialog
import com.spongycode.calendar.screen.components.TopBar
import com.spongycode.calendar.screen.calendar.components.MonthView
import com.spongycode.calendar.screen.calendar.components.TaskItem
import com.spongycode.calendar.screen.components.TaskDetailsDialog
import java.time.LocalDate

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val tasks by viewModel.tasks.collectAsState()
    val selectedDay by viewModel.selectedDay.collectAsState()

    val currentYear = remember { mutableIntStateOf(LocalDate.now().year) }
    val currentMonthIndex = remember { mutableIntStateOf(LocalDate.now().monthValue - 1) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<Task?>(null) }
    val context = LocalContext.current
    LaunchedEffect(currentYear.intValue, currentMonthIndex.intValue) {
        viewModel.loadTasksForCurrentMonth(currentYear.intValue, currentMonthIndex.intValue + 1)
    }

    val previousMonth = {
        if (currentMonthIndex.intValue == 0) {
            currentMonthIndex.intValue = 11
            currentYear.intValue--
        } else {
            currentMonthIndex.intValue--
        }
    }

    val nextMonth = {
        if (currentMonthIndex.intValue == 11) {
            currentMonthIndex.intValue = 0
            currentYear.intValue++
        } else {
            currentMonthIndex.intValue++
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(onClick = { navController.navigate("task") })
        MonthView(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                .height(450.dp)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    var startX: Float = 0f
                    var endX: Float = 0f
                    detectHorizontalDragGestures(
                        onDragStart = { offset ->
                            startX = offset.x
                        },
                        onDragEnd = {
                            if (endX - startX > 0) {
                                previousMonth()
                            } else {
                                nextMonth()
                            }
                        },
                        onHorizontalDrag = { change, _ ->
                            endX = change.position.x
                        },
                    )
                },
            year = currentYear.intValue,
            month = currentMonthIndex.intValue + 1,
            onNextMonthClick = {
                nextMonth()
            },
            onPreviousMonthClick = {
                previousMonth()
            },
            onDateSelected = { day ->
                viewModel.selectDay(day)
            },
            selectedDay = selectedDay,
            tasks = tasks.filter { it.year == currentYear.intValue && it.month == currentMonthIndex.intValue + 1 },
            onMonthYearSelected = { month, year ->
                currentMonthIndex.intValue = month
                currentYear.intValue = year
            },
            onSync = {
                Toast
                    .makeText(context, "Syncing started", Toast.LENGTH_SHORT)
                    .show()
                viewModel.manualSync {
                    Toast
                        .makeText(context, "Syncing completed", Toast.LENGTH_SHORT)
                        .show()

                }
            }
        )

        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Daily Tasks",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(30))
                    .clickable { showDialog = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(35.dp),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Task"
                )
            }
        }

        val todayTasks =
            tasks.filter { it.year == currentYear.intValue && it.month == currentMonthIndex.intValue + 1 && it.day == selectedDay }

        if (todayTasks.isNotEmpty()) {
            LazyColumn {
                items(todayTasks) { task ->
                    TaskItem(task, onClick = { selectedTask = it })
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20))
                        .clickable { showDialog = true },
                ) {
                    Text(
                        modifier = Modifier.padding(50.dp),
                        text = "No tasks today, tap to add more.",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }

        if (showDialog) {
            AddTaskDialog(
                onDismiss = { showDialog = false },
                onAddTask = { title, description ->
                    val newTask = Task(
                        title,
                        description,
                        currentYear.intValue,
                        currentMonthIndex.intValue + 1,
                        selectedDay ?: 1,
                        System.currentTimeMillis().toString()
                    )
                    viewModel.addTask(newTask)
                    showDialog = false
                }
            )
        }

        selectedTask?.let { task ->
            TaskDetailsDialog(
                task = task,
                onDismiss = { selectedTask = null },
                onDeleteTask = {
                    viewModel.deleteTask(task)
                    selectedTask = null
                }
            )
        }
    }
}