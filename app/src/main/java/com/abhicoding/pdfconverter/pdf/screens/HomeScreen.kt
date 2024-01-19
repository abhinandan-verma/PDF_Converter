package com.abhicoding.pdfconverter.pdf.screens

import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.abhicoding.pdfconverter.R
import com.abhicoding.pdfconverter.pdf.PdfEvent
import com.abhicoding.pdfconverter.pdf.PdfViewModel
import com.abhicoding.pdfconverter.pdf.model.PdfData
import com.abhicoding.pdfconverter.pdf.utils.Methords
import java.io.File


@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navHostController: NavHostController, viewModel: PdfViewModel) {

    val context = LocalContext.current
    val pdfList = viewModel.getPdfEventFlow.value
    var fileList by remember { mutableStateOf(emptyList<File>()) }
    viewModel.onEvent(PdfEvent.GetPDF)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()){uris ->
        Log.d("tag", "HomeScreen: uri $uris")
        val list = arrayListOf<File>()
        list.addAll(fileList)
        uris.forEach { uri ->
            list.add(File(uri.toString()))
        }
        fileList = list
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),

        floatingActionButton = {

            FloatingActionButton(onClick = {
                navHostController.navigate(route = "Image Screen")
                },
                modifier = Modifier.padding(bottom = 20.dp, end = 20.dp),
                containerColor = Color.Cyan) {
               Icon(painter = painterResource(id = R.drawable.pic2),
                   contentDescription = "addPdf",
                   modifier = Modifier.size(55.dp)
               )
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Home ")
                },
                scrollBehavior = scrollBehavior
            )
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            if (pdfList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.pim),
                        contentDescription = "logo",
                        modifier = Modifier.align(Alignment.Center)

                    )
                    Text(
                        text = "No Pdf created",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.LightGray
                        )
                    )
                }
            } else {

                val documentsDirectory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                val pdfFiles = documentsDirectory?.listFiles { file ->
                    file.isFile && file.extension.equals("pdf", ignoreCase = true)
                }?.toList() ?: emptyList()
                LazyColumn{
                    items(pdfFiles){pdfFile ->
                        PdfBox(pdf = pdfFile,
                            onBoxClicked = {
                                //navigate to pdf view page
                                val pdfData = PdfData(pdfFile, pdfFile.toUri())
                                viewModel.setPdf(pdfData)
                                navHostController.navigate(route = "PDF View Screen")
                            }) {
                            Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
               /* LazyColumn {

                    items(pdfList) {
                        PdfBox(pdf = it, viewModel, onBoxClicked = {
                            //navigate to pdf view page
                            viewModel.setPdf(it)
                            navHostController.navigate(route = "PDF View Screen")
                        }, onMoreClicked = {
                            Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show()
                        })
                    }
                }*/
            }
        }
    }
}

@Composable
fun PdfBox(
    pdf: File,
    onBoxClicked: () -> Unit,
    onMoreClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(12.dp)
            .background(color = Color.LightGray, shape = RoundedCornerShape(14.dp))
            .clickable {
                onBoxClicked()
            }
    ) {
        val name = pdf.name
        val timestamp = pdf.lastModified()
        val date = Methords.formatTimeStamp(timestamp)
        val bytes: Double = pdf.length().toDouble()
        val kb = bytes / 1024
        val mb = kb / 1024
        val size: String = if (mb >= 1){
            String.format("%.2f", mb) + "MB"
        }else if (kb >= 1){
            String.format("%.2f", kb) + "KB"
        }else{
            String.format("%.2f", bytes) + "bytes"
        }

        Row(
            Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.pic2),
                contentDescription = "pdf",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            )

            Column(modifier = Modifier.weight(4f)) {

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                )
                {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(text = name,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            onClick = onMoreClicked,
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "more",
                            )
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth())
                {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp, 2.dp)
                    ) {
                        Text(text = size,
                            Modifier.align(Alignment.CenterStart),
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = date,
                            Modifier.align(Alignment.CenterEnd),
                            color = Color.Black)
                    }
                }
            }
        }
    }
}



