package com.spongycode.calendar.screen.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.spongycode.calendar.domain.model.Task
import com.spongycode.calendar.screen.calendar.components.TaskItem
import com.spongycode.calendar.screen.components.TopBar
import com.spongycode.calendar.screen.components.TaskDetailsDialog

@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = hiltViewModel(),
    navController: NavHostController
) {
    var selectedTask by remember { mutableStateOf<Task?>(null) }

    val tasks by viewModel.tasks.collectAsState()

    Column(Modifier.fillMaxSize()) {
        TopBar(
            text = "All tasks",
            navigateIcon = Icons.Default.KeyboardArrowLeft,
            onNavigate = { navController.navigateUp() }
        )
        LazyColumn(modifier = modifier.fillMaxSize()) {
            itemsIndexed(tasks) { _, task ->
                TaskItem(task = task) {
                    selectedTask = it
                }
            }
        }
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