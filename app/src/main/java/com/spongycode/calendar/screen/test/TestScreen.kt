package com.spongycode.calendar.screen.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TestScreen(
    modifier: Modifier = Modifier,
    viewModel: TestViewModel = hiltViewModel()
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(onClick = { viewModel.getAllTasksFromCloud() }) {
            Text("Get All Tasks from Cloud")
        }

        Button(onClick = { viewModel.storeTaskOnCloud() }) {
            Text("Store Task on Cloud")
        }

        Button(onClick = { viewModel.deleteTaskFromCloud("some_timestamp") }) {
            Text("Delete Task from Cloud")
        }

        Button(onClick = { viewModel.getAllTasksFromLocal() }) {
            Text("Get All Tasks from Local")
        }

        Button(onClick = { viewModel.storeTaskOnLocal() }) {
            Text("Store Task on Local")
        }

        Button(onClick = { viewModel.deleteTaskFromLocal("some_timestamp") }) {
            Text("Delete Task from Local")
        }

        Button(onClick = { viewModel.getTasksByMonthFromLocal(2024, 7) }) {
            Text("Get Tasks by Month from Local")
        }

        Button(onClick = { viewModel.getTasksByDayFromLocal(2024, 7, 17) }) {
            Text("Get Tasks by Day from Local")
        }
    }
}