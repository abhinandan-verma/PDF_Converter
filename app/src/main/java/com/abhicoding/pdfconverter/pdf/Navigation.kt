package com.abhicoding.pdfconverter.pdf

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abhicoding.pdfconverter.pdf.screens.HomeScreen
import com.abhicoding.pdfconverter.pdf.screens.ImageScreen
import com.abhicoding.pdfconverter.pdf.screens.PdfViewer

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun Navigation() {
    val navHostController = rememberNavController()
    val viewModel: PdfViewModel = hiltViewModel()


    NavHost(navController = navHostController, startDestination = "Home Screen") {
        composable(route = "Home Screen") {
            HomeScreen(navHostController, viewModel)
        }
        composable(route = "Image Screen") {
            ImageScreen(navHostController, viewModel)
        }
        composable(route = "PDF View Screen") {
            PdfViewer(navHostController, viewModel)
        }
    }
}