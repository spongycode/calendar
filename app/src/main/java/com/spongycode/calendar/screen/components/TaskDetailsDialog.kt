package com.spongycode.calendar.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.font.FontWeight.Companion.W800
import androidx.compose.ui.unit.dp
import com.spongycode.calendar.domain.model.Task


@Composable
fun TaskDetailsDialog(task: Task, onDismiss: () -> Unit, onDeleteTask: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "${task.day}/${task.month}/${task.year}",
                style = MaterialTheme.typography.headlineSmall, fontWeight = W400
            )

        },
        text = {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = W800
                )
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = W500
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDeleteTask,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Close")
            }
        }
    )
}