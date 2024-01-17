package com.abhicoding.pdfconverter.mainscreen.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.rounded.Build
import com.abhicoding.pdfconverter.mainscreen.NavigationItem


val items = listOf(
    NavigationItem("Home",
        Icons.Default.Home,
        Icons.Outlined.Home),
    NavigationItem(
        "Create PDF",
        Icons.Filled.Favorite,
        Icons.Outlined.Favorite
    ),
    NavigationItem(
        "View Files",
        Icons.Default.ThumbUp,
        Icons.Outlined.ThumbUp
    ),
    NavigationItem(
        "Merge PDF",
        Icons.Filled.Add,
        Icons.Outlined.AddCircle
    ),
    NavigationItem(
        "History",
        Icons.Filled.Check,
        Icons.Rounded.Build
    ),
    NavigationItem(
        "About Us",
        Icons.Outlined.ExitToApp,
        Icons.Filled.Add
    ),
    NavigationItem(
        "Share",
        Icons.Filled.Share,
        Icons.Outlined.Share
    )
)

