package com.abhicoding.pdfconverter.mainscreen.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Create
import com.abhicoding.pdfconverter.mainscreen.Cards

val cardList = mutableListOf(
    Cards("Crop Image", Icons.Default.Create),
    Cards("Create PDF", Icons.Filled.AccountBox),
    Cards("Compress | Crop", Icons.Default.Create)
)