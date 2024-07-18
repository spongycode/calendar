package com.spongycode.calendar.screen.calendar.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.spongycode.calendar.domain.model.Task
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.Year

@Composable
fun MonthView(
    modifier: Modifier = Modifier,
    year: Int,
    month: Int,
    selectedDay: Int?,
    onDateSelected: (Int) -> Unit = {},
    tasks: List<Task> = emptyList(),
    onNextMonthClick: () -> Unit,
    onPreviousMonthClick: () -> Unit,
    onMonthYearSelected: (Int, Int) -> Unit,
    onSync: () -> Unit = {}
) {
    val daysInMonth = Month.of(month).length(Year.isLeap(year.toLong()))
    val firstDayOfWeek = (LocalDate.of(year, month, 1).dayOfWeek.value + 6) % 7
    val currentDate = LocalDate.now()
    var showMonthYearPicker by remember { mutableStateOf(false) }


    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${Month.of(month).name} $year",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { showMonthYearPicker = true }
            )
            Row {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(30))
                        .clickable { onSync() }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Manual Sync"
                    )
                }

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(30))
                        .clickable { onPreviousMonthClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Previous Month"
                    )
                }

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(30))
                        .clickable { onNextMonthClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Next Month"
                    )
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            DayOfWeek.entries.forEach { day ->
                Text(
                    text = day.name.take(3),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(42) { index ->
                if (index < firstDayOfWeek || index >= firstDayOfWeek + daysInMonth) {
                    Spacer(modifier = Modifier.size(56.dp))
                } else {
                    val day = index - firstDayOfWeek + 1
                    val taskCount = tasks.count { it.day == day }
                    val isCurrentDate =
                        currentDate.year == year && currentDate.monthValue == month && currentDate.dayOfMonth == day
                    val isSelectedDate = selectedDay == day

                    DayCell(
                        day = day,
                        taskCount = taskCount,
                        isCurrentDate = isCurrentDate,
                        isSelectedDate = isSelectedDate,
                        onClick = { onDateSelected(day) })
                }
            }
        }
    }

    if (showMonthYearPicker) {
        MonthYearPickerDialog(
            year = year,
            month = month - 1,
            onDismissRequest = { showMonthYearPicker = false },
            onMonthYearSelected = { selectedMonth, selectedYear ->
                onMonthYearSelected(
                    selectedMonth,
                    selectedYear
                )
            }
        )
    }
}