package com.spongycode.calendar.screen.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MonthYearPicker(
    currentYear: Int = 2023,
    currentMonth: Int = 7,
    onMonthYearSelected: (Int, Int) -> Unit = { _, _ -> },
    onDismissRequest: () -> Unit
) {
    var yearSelected by remember { mutableIntStateOf(currentYear) }
    var monthSelected by remember { mutableIntStateOf(currentMonth) }
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(5))
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Select year and month",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.W600
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            InfiniteCircularList(
                width = 60.dp,
                itemHeight = 70.dp,
                items = (0..11).toList(),
                initialItem = currentMonth,
                textStyle = TextStyle(fontSize = 20.sp),
                textColor = Color.Black,
                selectedTextColor = Color.Black,
                onItemSelected = { _, month ->
                    monthSelected = month
                }
            )
            Spacer(modifier = Modifier.width(30.dp))
            InfiniteCircularList(
                width = 60.dp,
                itemHeight = 70.dp,
                items = (1950..2070).toList(),
                initialItem = currentYear,
                textStyle = TextStyle(fontSize = 20.sp),
                textColor = Color.Black,
                selectedTextColor = Color.Black,
                onItemSelected = { _, year ->
                    yearSelected = year
                }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        Row {
            Button(onClick = { onDismissRequest() }) {
                Text(text = "Cancel")
            }
            Spacer(modifier = Modifier.width(20.dp))
            Button(onClick = {
                onMonthYearSelected(monthSelected, yearSelected)
                onDismissRequest()
            }) {
                Text(text = "Submit")
            }
        }
        Spacer(modifier = Modifier.height(25.dp))

    }
}