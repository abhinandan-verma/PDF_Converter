package com.abhicoding.pdfconverter.pdf.model

import android.net.Uri
import java.io.File

data class ImageData(val imageUri: Uri, var checked: Boolean)

data class PdfData(val file: File, val uri: Uri)