package com.abhicoding.pdfconverter.cropscreen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView

fun saveImage(uri: Uri, context: Context){
   // val cropImageContractOptions = cropBitmap(uri)
    val bitmap = uri.asBitmap(context)
    val outputStream = context.contentResolver.openOutputStream(uri)
    bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream!!)
    outputStream.flush()
    outputStream.close()
}

fun cropBitmap(uri: Uri): CropImageContractOptions {
val cropOptions = CropImageOptions()
    cropOptions.apply {
        guidelines = CropImageView.Guidelines.ON
        outputCompressFormat = Bitmap.CompressFormat.JPEG
        outputCompressQuality = 100
        cropShape = CropImageView.CropShape.RECTANGLE
        outputRequestSizeOptions = CropImageView.RequestSizeOptions.RESIZE_FIT
        cornerShape = CropImageView.CropCornerShape.OVAL
    }
    return CropImageContractOptions(uri = uri,cropOptions)
}
fun Uri.asBitmap(context: Context): Bitmap{
val parcelFileDescriptor =
    context.contentResolver.openAssetFileDescriptor(this,"r",null)
    val fileDescriptor = parcelFileDescriptor?.fileDescriptor
    val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
    parcelFileDescriptor?.close()
    return image
}
