package com.abhicoding.pdfconverter.cropscreen

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.R)
fun saveImg(uri: Uri, context: Context){
    try {
        val croppedUri = cropBitmap(uri).uri
        val outputStream = context.contentResolver.openOutputStream(uri)
        val bitmap = croppedUri?.asBitmap(context)
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream!!)
        outputStream?.flush()
        outputStream?.close()

        val imageFile = createImageFile()
        addImageToGallery(context = context, file = imageFile)
        Toast.makeText(context,"image saved successfully",Toast.LENGTH_SHORT).show()
        Log.e("msg","image path: ${imageFile.absolutePath}")
    }catch (e: Exception){
        e.printStackTrace()
        Log.e("error",e.message.toString())
        Toast.makeText(context,"Error: ${e.message.toString()}",Toast.LENGTH_SHORT).show()
    }
}
