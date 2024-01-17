package com.abhicoding.pdfconverter.cropscreen

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import java.io.File
import java.io.FileOutputStream


@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun ImagePicker() {
    var i = 0
    val context = LocalContext.current
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            imageUri = result.uriContent
        } else {
            val exception = result.error
            Log.e("error", exception.toString())
        }
    }
    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            val cropOptions = CropImageContractOptions(uri, CropImageOptions())
            imageCropLauncher.launch(cropOptions)
        }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Row {
            Button(
                onClick = {
                    imagePickerLauncher.launch("*/*")
                },
                colors = ButtonDefaults.textButtonColors(Color.Magenta)
            ) {
                Text(
                    text = "Choose Image",
                    color = Color.White
                )
            }

            IconButton(onClick = {
                Log.e("error","imageBtn is Clicked")
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Rotate",
                )
            }

            Button(
                onClick = {
                   // saveImage(imageUri!!, context = context)
                    i++
                 //   imageUri?.let { saveCroppedImage(context, it) }
                    imageUri?.let { saveImg(it, context) }
                },
                colors = ButtonDefaults.textButtonColors(Color.Cyan)
            ) {
                Text(
                    text = "Crop Image",
                    color = Color.Black,
                    fontFamily = FontFamily.Cursive
                )

            }
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxSize(1f)

                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
fun saveCroppedImage(context: Context, uri: Uri){
    try {
        val originalBitmap = BitmapFactory.decodeFile(uri.toString())
        val file = createImageFile()
        val outputStream = FileOutputStream(file)

        originalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        // adding image to gallery
        addImageToGallery(context, file)
        Toast.makeText(context, "Image Saved, path: ${file.absolutePath}", Toast.LENGTH_SHORT)
            .show()
        Log.e("msg", "File Saved: ${file.absolutePath}")
    }catch (e: Exception){
        e.printStackTrace()
        Log.e("error","Error saving image ${e.message}")
    }
}

fun addImageToGallery(context: Context, file: File) {
    val values = ContentValues().apply {
        put(MediaStore.Images.Media.TITLE,"Cropped Image")
        put(MediaStore.Images.Media.DESCRIPTION,"Cropped Image Description")
        put(MediaStore.Images.Media.AUTHOR,"Abhinandan")
        put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg")
        put(MediaStore.Images.Media.DATA,file.absolutePath)
    }
    context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        values
    )
}

@RequiresApi(Build.VERSION_CODES.R)
fun createImageFile(): File {
    val timeStamp: String = System.currentTimeMillis().toString()
    val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

    return File.createTempFile("Cropped_image_${timeStamp}",".jpeg",storageDir)
}




