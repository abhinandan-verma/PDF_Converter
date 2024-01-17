package com.abhicoding.pdfconverter.cropscreen

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.abhicoding.pdfconverter.cropscreen.ui.theme.PDFConverterTheme

class CropActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PDFConverterTheme {
                ImagePicker()
            }
        }

    }
}



