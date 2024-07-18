package com.spongycode.calendar.screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    text: String = "Calendar",
    onClick: () -> Unit = {},
    navigateIcon: ImageVector? = null,
    onNavigate: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        navigateIcon?.let {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(30))
                    .clickable { onNavigate() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(35.dp),
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "back"
                )
            }
        }
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            modifier = Modifier
                .weight(1f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onClick() },
            text = text,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = W600
        )
    }
}