package com.abhicoding.pdfconverter.pdf.screens

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.abhicoding.pdfconverter.R
import com.abhicoding.pdfconverter.pdf.PdfEvent
import com.abhicoding.pdfconverter.pdf.PdfViewModel
import com.abhicoding.pdfconverter.pdf.model.ImageData
import com.canhub.cropper.CropImageContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageScreen(navHostController: NavHostController, viewModel: PdfViewModel) {

    val context = LocalContext.current
    var imageList by remember { mutableStateOf(emptyList<ImageData>()) }
    var update by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var save by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }


    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        Log.d("tag", "ImageScreen: uri $uris")
        val list = arrayListOf<ImageData>()
        list.addAll(imageList)
        uris.forEach { uri ->
            list.add(ImageData(uri, false))
        }
        imageList = list
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = {
            val uri = it?.let {
                viewModel.saveBitmapToMediaStore(bitmap = it)
            }
            Log.d("msg", "ImageScreen: uri $uri")
            if (uri != null) {
                val list = arrayListOf<ImageData>()
                list.addAll(imageList)
                list.add(ImageData(uri, false))
                imageList = list
            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            imageUri = result.uriContent
        } else {
            val exception = result.error
            Log.e("error", exception.toString())
        }
    }


    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {

           Column (
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom,
               modifier = Modifier.padding(bottom = 10.dp)
            ){
                FloatingActionButton(
                    onClick = {
                        val permissionCheckResult =
                            ContextCompat.checkSelfPermission(
                                context,
                                android.Manifest.permission.CAMERA
                            )
                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            cameraLauncher.launch()
                        } else {
                            permissionLauncher.launch(android.Manifest.permission.CAMERA)
                        }
                    },
                    containerColor = Color.Magenta,
                    shape = CircleShape,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = "addPdf",
                        tint = Color.White,
                        modifier = Modifier.padding(8.dp).size(40.dp)
                    )
                }
                FloatingActionButton(
                    onClick = {
                        val permissionCheckResult =
                            ContextCompat.checkSelfPermission(
                                context,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE
                            ) // READ_MEDIA_IMAGES for latest
                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            galleryLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        } else {
                            permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE) // READ_MEDIA_IMAGES for latest
                        }
                    },
                    shape = CircleShape,
                    containerColor = Color.Cyan,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.gallery),
                        contentDescription = "addPdf",
                        tint = Color.Black,
                        modifier = Modifier.padding(8.dp).size(40.dp)
                    )
                }
               FloatingActionButton(
                   onClick = {
                       Log.d("msg", "ImageScreen: IconButton onClick")
                       if (imageList.isEmpty()) {
                           Toast.makeText(context, "No Image Selected", Toast.LENGTH_SHORT).show()
                       } else {
                           save = true
                       }
                   },
                   shape = CircleShape,
                   containerColor = Color.Green,
                   modifier = Modifier.size(55.dp)
               ) {
                   Icon(
                       painter = painterResource(id = R.drawable.pic2),
                       contentDescription = "addPdf",
                       tint = Color.Black
                   )
               }

            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Image Screen",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navHostController.navigateUp()
                    }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Green
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        Log.d("msg", "ImageScreen: IconButton onClick")
                        if (imageList.isEmpty()) {
                            Toast.makeText(context, "No Image Selected", Toast.LENGTH_SHORT).show()
                        } else {
                            save = true
                        }
                    }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.pic2),
                            contentDescription = "PDF",
                            tint = Color.Cyan
                        )
                    }
                    IconButton(onClick = {
                        if (imageList.isEmpty()) {
                            Toast.makeText(context, "Nothing to Delete", Toast.LENGTH_SHORT).show()
                        } else {
                            val list = arrayListOf<ImageData>()

                            imageList.forEach { imageData ->
                                if (!imageData.checked) {
                                    list.add(imageData)
                                }
                            }
                            if (list.isEmpty()) {
                                Toast.makeText(context, "Nothing Selected", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                imageList = list
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                    IconButton(onClick = {
                        imageList = emptyList()
                        Toast.makeText(context, "Page Cleared...", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Delete All",
                            tint = Color.Magenta
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (save) {
                PdfSaveDialog(
                    title,
                    onNameChanged = {
                        title = it
                    },
                    onDismissListener = {
                        save = false
                        loading = false
                    }
                ) {
                    loading = true
                    save = false
                    CoroutineScope(Dispatchers.IO).launch {
                        val list = arrayListOf<ImageData>()
                        imageList.forEach { imageData ->
                            if (imageData.checked) {
                                list.add(imageData)
                            }
                        }
                        if (list.size != 0) {
                            imageList = list
                        }
                        viewModel.onEvent(PdfEvent.CreatePDF(title, imageList) { })
                        Log.d("msg", "pdf Created")

                        withContext(Dispatchers.Main) {
                            loading = false
                            Toast.makeText(context, "PDF Converted", Toast.LENGTH_SHORT).show()
                            Log.d("msg", "going to main screen")
                            navHostController.navigateUp()
                        }
                    }
                }
            }
        }
        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                CircularProgressIndicator(
                    Modifier
                        .size(48.dp)
                        .align(Alignment.Center)
                )
            }
        }


        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
        ) {
            items(imageList) { imageData ->
                SelectedBox(
                    imageData,
                    selected = imageData.checked
                ) {
                    Log.d("msg", "ImageScreen: imageData.checked ${imageData.checked}")
                    imageData.checked = !imageData.checked
                    update = !update
                    Log.d("msg", "ImageScreen: imageData.checked ${imageData.checked}")
                }
            }
        }

    }
}

@Composable
fun SelectedBox(
    imageData: ImageData,
    selected: Boolean,
    onClick: () -> Unit
) {
    Log.d("msg", "SelectedBox: box created ")

    Box(
        modifier = Modifier
        .width(200.dp)
        .height(150.dp)
        .padding(12.dp)
        .clip(shape = RoundedCornerShape(25.dp))
        .clickable {
            onClick()
        },

    ) {
        Card(
            shape = RoundedCornerShape(15.dp),
            elevation = CardDefaults.cardElevation(10.dp),
            modifier = Modifier
                .background(Color.Cyan)
                .padding(12.dp)

        ) {
            AsyncImage(
                model = imageData.imageUri,
                contentDescription = null,
                Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentScale = ContentScale.FillBounds
            )
            Text(
                text = imageData.imageUri.toString(),
                modifier = Modifier.background(Color.LightGray),
                color = Color.Black
            )
        }

        if (selected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .background(color = Color(0x7700C6F7))
            )
        }
    }
}

@Composable
fun PdfSaveDialog(
    name: String,
    onNameChanged: (String) -> Unit,
    onDismissListener: () -> Unit,
    onSaveClickedListener: () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = "Details")
        },
        text = {
            OutlinedTextField(value = name, onValueChange = onNameChanged, placeholder = {
                Text(text = "Pdf Title")
            })
        },
        onDismissRequest = onDismissListener,
        confirmButton = {
            Button(onClick = onSaveClickedListener, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Convert to pdf ")
            }
        }
    )
}
