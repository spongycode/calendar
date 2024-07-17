package com.spongycode.calendar.screen.calendar.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.spongycode.calendar.screen.components.CustomTextField


@Composable
fun AddTaskDialog(onDismiss: () -> Unit, onAddTask: (String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add Task",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        },
        text = {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = "Title",
                    singleLine = true
                )
                CustomTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = "Description"
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isEmpty() || description.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Title and description cannot be empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        onAddTask(title, description)
                        title = ""
                        description = ""
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Cancel")
            }
        }
    )
}