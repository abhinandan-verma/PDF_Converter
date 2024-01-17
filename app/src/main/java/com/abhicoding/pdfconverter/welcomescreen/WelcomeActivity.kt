package com.abhicoding.pdfconverter.welcomescreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.abhicoding.pdfconverter.ui.theme.PDFConverterTheme

class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PDFConverterTheme {

            }
        }
    }
}
