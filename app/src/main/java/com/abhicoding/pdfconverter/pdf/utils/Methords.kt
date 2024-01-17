package com.abhicoding.pdfconverter.pdf.utils

import java.text.DateFormat
import java.util.Calendar
import java.util.Locale

object Methords {
    fun formatTimeStamp(timeStamp: Long): String {
        val calendar = Calendar.getInstance(Locale.US)
        calendar.timeInMillis = timeStamp

       return  DateFormat.getInstance().format("dd/MM/yyyy").toString()

    }
}