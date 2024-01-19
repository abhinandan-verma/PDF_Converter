package com.abhicoding.pdfconverter.pdf

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.abhicoding.pdfconverter.pdf.ui.theme.PDFConverterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PdfActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PDFConverterTheme {
                Navigation()
            }
        }
    }
}

