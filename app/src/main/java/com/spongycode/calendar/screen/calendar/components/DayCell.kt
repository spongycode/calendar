package com.spongycode.calendar.screen.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.spongycode.calendar.ui.theme.DarkBlue
import com.spongycode.calendar.ui.theme.DarkGreen
import com.spongycode.calendar.ui.theme.LightBlue


@Composable
fun DayCell(
    day: Int,
    taskCount: Int,
    isCurrentDate: Boolean,
    isSelectedDate: Boolean,
    onClick: () -> Unit
) {

    val backgroundColor = when {
        isSelectedDate -> DarkBlue
        isCurrentDate -> LightBlue
        else -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .size(56.dp)
            .padding(6.dp)
            .clip(RoundedCornerShape(10))
            .border(1.dp, Color.LightGray, RoundedCornerShape(10))
            .background(backgroundColor)
            .clickable { onClick() }
    ) {
        Text(
            color = if (isSelectedDate) Color.White else Color.Black,
            text = day.toString(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.W800,
            modifier = Modifier.align(Alignment.Center)
        )
        if (taskCount > 0) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(15.dp)
                    .align(Alignment.TopEnd)
                    .background(DarkGreen)
                    .clip(RoundedCornerShape(10))
            ) {
                Text(
                    text = taskCount.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
            }
        }
    }
}