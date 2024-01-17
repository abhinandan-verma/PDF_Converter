package com.abhicoding.pdfconverter.pdf

import com.abhicoding.pdfconverter.pdf.model.ImageData
import com.abhicoding.pdfconverter.pdf.model.PdfData

sealed class PdfEvent {
    data object GetPDF : PdfEvent()
    data class  CreatePDF(
        val title: String,
        val imageList: List<ImageData>,
        var pdfConverted: () -> Unit
    ) : PdfEvent()

    data class OpenPDF(val pdf: PdfData) : PdfEvent()
    data class DeletePDF(val pdf: PdfData, var isDeleted:(Boolean) -> Unit) : PdfEvent()
}