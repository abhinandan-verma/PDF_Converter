package com.abhicoding.pdfconverter.pdf.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import com.abhicoding.pdfconverter.pdf.PdfEvent
import com.abhicoding.pdfconverter.pdf.PdfViewModel
import com.abhicoding.pdfconverter.pdf.model.PdfData
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import java.io.File

@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewer(navHostController: NavHostController, viewModel: PdfViewModel) {

    val context = LocalContext.current
    val pdf: PdfData? = viewModel.getPdf()
    var share by remember {
        mutableStateOf(false)
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = pdf!!.file.name,
                        fontSize = 17.sp)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navHostController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (pdf != null) {
                            sharePdf(pdf.file, context)
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.Green)
                    }
                    IconButton(onClick = {
                        viewModel.onEvent(PdfEvent.DeletePDF(pdf!!) { delete ->
                            if (delete) {
                                Toast.makeText(context, "Pdf Deleted", Toast.LENGTH_SHORT).show()
                                navHostController.navigateUp()
                            } else {
                                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        })
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            if (pdf != null) {
             PdfView(pdf.file)

                /* Parameters:
- path: File path or URI of the local PDF.
- fromAssets: Set to true when loading from assets.
*/
               /* PdfViewerActivity.launchPdfFromPath(
                    context = context,
                    path = pdf.uri.toString(),
                    pdfTitle = pdf.file.name,
                    fromAssets = true,
                    saveTo = saveTo.ASK_EVERYTIME
                )
                PdfRendererViewCompose(
                    url = pdf.uri.toString(),
                    lifecycleOwner = LocalLifecycleOwner.current,
                    file = pdf.file
                )*/

            } else {
                Toast.makeText(context, "null", Toast.LENGTH_SHORT).show()
            }
            if (share) {
                if (pdf != null) {
                    SharePdf(pdfFile = pdf.file)
                }

            }
        }

    }

}


@Composable
fun PdfView(pdfFile: File) {
    val context = LocalContext.current
    val pdfView = remember {
        PDFView(context, null)
    }
    var currentPage by remember { mutableIntStateOf(0) }
    var pageCount by remember { mutableIntStateOf(0) }

    DisposableEffect(Unit) {
        pdfView.fromFile(pdfFile)
            .defaultPage(0)
            .onPageChange { page, newPageCount ->
                currentPage = page
                pageCount = newPageCount
            }
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .autoSpacing(true)
            .fitEachPage(true)
            .linkHandler {

            }
            .onError {
                Toast.makeText(context,"Something went Wrong ",Toast.LENGTH_SHORT).show()
            }
            .onLoad { numberOfPages ->
                pageCount = numberOfPages
            }
            .scrollHandle(DefaultScrollHandle(context))
            .load()

        onDispose {
            pdfView.recycle()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        if (pageCount > 0) {
            Text(
                text = "Page ${currentPage + 1} of $pageCount",
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.Black)
                    .padding(4.dp)
                    .clickable {
                        // Handle click if needed
                    },
                color = Color.White
            )
        }
        AndroidView(
            factory = { pdfView },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun SharePdfButton(pdfUri: Uri, onButtonClick: () -> Unit) {
    val context = LocalContext.current
    val activity = (context as? ComponentActivity)
    val shareLauncher = remember {
        activity?.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            // Handle the result if needed
        }
    }

    Button(
        onClick = {
            onButtonClick()
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "application/pdf"
            intent.putExtra(Intent.EXTRA_STREAM, pdfUri)
            shareLauncher?.launch(Intent.createChooser(intent, "Share PDF"))

        },
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Share PDF")
    }
}

@Composable
fun SharePdf(pdfFile: File) {
    val context = LocalContext.current
    FileProvider
        .getUriForFile(context, "${context.packageName}.fileprovider", pdfFile)

    val sharePdfLauncher =
        rememberLauncherForActivityResult(CreateDocument("document/pdf")) { uri ->
            if (uri != null) {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, uri)
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share PDF"))
            }
        }
    
    Column (
        modifier = Modifier.padding(16.dp)
    ){
        Text(text = "Share PDF Example")
        Spacer(modifier = Modifier.padding(8.dp))
        
        Button(onClick = {
            sharePdfLauncher.launch("document.pdf")
        }) {
            Text(text = "Share PDF")
        }
    }
}

private fun sharePdf(pdfFile: File, context: Context) {


    val uri2 = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", pdfFile)
    val shareIntent2 = Intent(Intent.ACTION_SEND)
    shareIntent2.type = "application/pdf"
    shareIntent2.putExtra(Intent.EXTRA_STREAM, uri2)
    shareIntent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    context.startActivity(Intent.createChooser(shareIntent2, "Share PDF"))

}

