package com.spongycode.calendar.screen.calendar.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.W600

@Composable
fun AppName(
    onClick: () -> Unit = {}
) {
    Text(
        modifier = Modifier.clickable { onClick() },
        text = "Calendar",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = W600
    )
}