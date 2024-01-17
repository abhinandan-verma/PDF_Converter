package com.abhicoding.pdfconverter.pdf

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.abhicoding.pdfconverter.R
import com.abhicoding.pdfconverter.pdf.model.PdfData
import com.abhicoding.pdfconverter.pdf.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class PdfViewModel @Inject constructor(private val application: Application) : ViewModel() {
    private val pdfData = mutableStateOf<PdfData?>(null)

    fun setPdf(pdf: PdfData) {
        pdfData.value = pdf
    }

    fun getPdf(): PdfData? {
        return pdfData.value
    }

    private val _getPdfEventFlow = mutableStateOf(emptyList<PdfData>())
    val getPdfEventFlow: State<List<PdfData>> = _getPdfEventFlow

    @RequiresApi(Build.VERSION_CODES.R)
    fun onEvent(event: PdfEvent) {
        when (event) {
            PdfEvent.GetPDF -> {
                val folder = File(application.getExternalFilesDir(null), Constants.PDF_FOLDER)
                if (folder.exists()) {
                    val files = folder.listFiles()
                    val list = arrayListOf<PdfData>()
                    for (fileEntry in files!!) {
                        val uri = Uri.fromFile(fileEntry)
                        val pdfData = PdfData(fileEntry, uri)

                        list.add(pdfData)
                    }
                    _getPdfEventFlow.value = list
                } else {
                    Log.d("msg", "loadPdfDocument: no files in folder")
                    Toast.makeText(application, "No pdf File", Toast.LENGTH_SHORT).show()
                }
            }

            is PdfEvent.CreatePDF -> {
                try {

                    val root = File(application.getExternalFilesDir(null), Constants.PDF_FOLDER)
                    root.mkdir()
                    Log.d("msg", "generatePdfFromImage; try")

                    val timestamp = System.currentTimeMillis()
                    val fileName = if (event.title.isNotEmpty()) {
                        "${event.title}.pdf"
                    } else {
                        "PDF_$timestamp.pdf"
                    }
                    val file = File(root, fileName)

                    Log.d("tag", "generatePdfFromImages: file $file")
                    val fileOutputStream = FileOutputStream(file)
                    val pdfDocument = PdfDocument()

                    for ((index, image) in event.imageList.withIndex()) {
                        try {
                            var bitmap = ImageDecoder.decodeBitmap(
                                ImageDecoder.createSource(
                                    application.contentResolver,
                                    image.imageUri
                                )
                            )
                            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false)

                            //Get the screen width
                            val displayMetrics = DisplayMetrics()
                            (application.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
                                .defaultDisplay.getMetrics(displayMetrics)

                            val screenWidth = displayMetrics.widthPixels

                            // calculate the new height to maintain the aspect ratio based on the screen width

                            val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
                            val newHeight = (screenWidth / aspectRatio).toInt()

                            // Resize the bitmap to have the screen width
                            bitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, newHeight, true)

                            val pageInfo = PdfDocument.PageInfo
                                .Builder(bitmap.width, bitmap.height, index + 1).create()

                            val page = pdfDocument.startPage(pageInfo)
                            val paint = Paint()

                            paint.color = ContextCompat.getColor(application, R.color.white)

                            val canvas = page.canvas
                            canvas.drawPaint(paint)
                            canvas.drawBitmap(bitmap, 0f, 0f, null)

                            pdfDocument.finishPage(page)
                            bitmap.recycle()
                        } catch (e: Exception) {
                            Log.d("error", "e: ${e.localizedMessage}")
                            e.printStackTrace()
                            event.pdfConverted()
                        }
                    }
                    pdfDocument.writeTo(fileOutputStream)
                    pdfDocument.close().let {
                        event.pdfConverted()
                    }
                } catch (e: Exception) {
                    Log.d("error", "e: ${e.localizedMessage}")
                    e.printStackTrace()
                }
            }

            is PdfEvent.OpenPDF -> {

            }

            is PdfEvent.DeletePDF -> {
                val fileToDelete = event.pdf.file
                event.isDeleted(fileToDelete.delete())
            }

        }
    }
    fun loadFileSize(pdf : PdfData): String{
        val bytes: Double = pdf.file.length().toDouble()
        val kb = bytes / 1024
        val mb = kb / 1024
        val size: String = if (mb >= 1){
            String.format("%.2f", mb) + "MB"
        }else if (kb >= 1){
            String.format("%.2f", kb) + "KB"
        }else{
            String.format("%.2f", bytes) + "bytes"
        }
        return  size
    }

    fun loadThumbNailFromPDF(pdf: PdfData, page: (Int) -> Unit): Bitmap?{
        Log.d("msg", "loadThumbNailFromPdf: ")
        val executorService = Executors.newSingleThreadExecutor()
        var thumbNailBitmap : Bitmap? = null
        val handler = Handler(Looper.getMainLooper())
        var pageCount = 0

        executorService.execute{
            try {
                val parcelFileDescriptor = ParcelFileDescriptor.open(pdf.file, ParcelFileDescriptor.MODE_READ_ONLY)
                val pdfReader = PdfRenderer(parcelFileDescriptor)
                pageCount = pdfReader.pageCount
                if (pageCount <= 0){
                    Log.d("msg","loadThumbNailFromPdf: No Page")
                }else{
                    val currentPage = pdfReader.openPage(0)
                    thumbNailBitmap = Bitmap.createBitmap(
                        currentPage.width,
                        currentPage.height,
                        Bitmap.Config.ARGB_8888
                    )
                    currentPage.render(
                        thumbNailBitmap!!,
                        null,
                        null,
                        PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                    )
                }
            }catch (e: Exception){
                Log.d("msg","loadThumbNailFromPdf: e ${e.localizedMessage}")
            }
            page(pageCount)
        }
        return thumbNailBitmap
    }

    fun saveBitmapToMediaStore(bitmap: Bitmap) : Uri? {
        val contentResolver = application.contentResolver
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME,"my_image.jpeg")
        contentValues.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg")

        val imageUri = contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            if (imageUri != null){
                val outputStream: OutputStream? = contentResolver.openOutputStream(imageUri)
                if (outputStream != null){
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream)
                    outputStream.flush()
                    outputStream.close()
                    Toast.makeText(application,"Image Saved Successfully",Toast.LENGTH_SHORT).show()
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
            Log.d("msg","saveBitmap to MediaStore: e ${e.localizedMessage}")
            return null
        }
        return  imageUri

    }

}