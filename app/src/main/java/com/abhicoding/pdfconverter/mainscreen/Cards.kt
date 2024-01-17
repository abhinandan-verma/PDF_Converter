package com.abhicoding.pdfconverter.mainscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Cards(val text: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cards(cards: MutableList<Cards>) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Functions",
                        color = Color.Magenta
                    )
                }
            )
        },
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = it ) {
            items(cards) {
                ElevatedCard(
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .padding(16.dp)
                        .border(2.dp, Color.Green)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Image(
                            imageVector = it.icon,
                            colorFilter = ColorFilter.tint(Color.Cyan),
                            contentDescription = it.text,
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = it.text,
                        fontWeight = FontWeight.Bold,
                        color = Color.Yellow,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

