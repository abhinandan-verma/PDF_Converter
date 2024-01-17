package com.abhicoding.pdfconverter.pdf.screens

import android.os.Build
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Add
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
import androidx.navigation.NavHostController
import com.abhicoding.pdfconverter.R
import com.abhicoding.pdfconverter.pdf.PdfEvent
import com.abhicoding.pdfconverter.pdf.PdfViewModel
import com.abhicoding.pdfconverter.pdf.model.PdfData
import com.abhicoding.pdfconverter.pdf.utils.Methords

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun HomeScreen(navHostController: NavHostController, viewModel: PdfViewModel) {

    val context = LocalContext.current
    val pdfList = viewModel.getPdfEventFlow.value

    viewModel.onEvent(PdfEvent.GetPDF)
    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehaviour.nestedScrollConnection),

        floatingActionButton = {
            FloatingActionButton(onClick = {
                navHostController.navigate(route = "Image Screen")
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Image"
                )
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Home")
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (pdfList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.pim),
                        contentDescription = "logo",
                        modifier = Modifier.align(Alignment.Center)
                            .size(200.dp)
                    )
                    Text(
                        text = "No Pdf Created",
                        modifier = Modifier.align(Alignment.BottomCenter)
                            .padding(bottom = 20.dp),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Magenta
                        )
                    )
                }
            } else {
                LazyColumn {
                    items(pdfList) {
                        PdfBox(pdf = it, viewModel, onBoxClicked = {
                            // navigate to pdf view page
                            viewModel.setPdf(it)
                            navHostController.navigate(route = "PDF View Screen")
                        },
                            onMoreClicked = {
                                Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show()
                            })
                    }
                }
            }
        }

    }
}

@Composable
fun PdfBox(
    pdf: PdfData,
    viewModel: PdfViewModel,
    onBoxClicked: () -> Unit,
    onMoreClicked: () -> Unit
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(13.dp)
        .padding(12.dp)
        .background(color = Color.LightGray, shape = RoundedCornerShape(8.dp))
        .clickable {
            onBoxClicked()
        }
    ) {
        val name = pdf.file.name
        val timestamp = pdf.file.lastModified()
        val date = Methords.formatTimeStamp(timestamp)
        val size = viewModel.loadFileSize(pdf)

        Row(
            Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.pim),
                contentDescription = "pdf",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            )

            Column(modifier = Modifier.weight(4f)) {
                Row(Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = name,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )

                        IconButton(
                            onClick = onMoreClicked,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "more"
                            )
                        }
                    }
                }

                Row(Modifier.fillMaxWidth()) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp, 2.dp)
                    ) {
                        Text(
                            text = size,
                            Modifier.align(Alignment.CenterStart)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(text = date, Modifier.align(Alignment.CenterEnd))
                    }
                }
            }
        }
    }
}
