package com.abhicoding.pdfconverter.mainscreen

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.graphics.vector.ImageVector
import com.abhicoding.pdfconverter.ui.theme.PDFConverterTheme

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null
)

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PDFConverterTheme {

                /*Surface(
                    modifier = Modifier.wrapContentSize(Alignment.TopStart),
                    contentColor = Color.Blue,
                    color = Color.White
                ) {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    var selectedItemIndex by rememberSaveable {
                        mutableIntStateOf(0)
                    }
                    ModalNavigationDrawer(
                        drawerContent = {
                            ModalDrawerSheet {
                                Spacer(modifier = Modifier.height(16.dp))
                                items.forEachIndexed { index, navigationItem ->
                                    NavigationDrawerItem(
                                        label = { Text(text = navigationItem.title) },
                                        selected = index == selectedItemIndex,
                                        onClick = {
                                            selectedItemIndex = index
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (index == selectedItemIndex) {
                                                    navigationItem.selectedIcon
                                                } else {
                                                    navigationItem.unselectedIcon
                                                }, contentDescription = navigationItem.title
                                            )
                                        },
                                        badge = {
                                            navigationItem.badgeCount?.let {
                                                navigationItem.badgeCount.toString()
                                                    .let { it1 -> Text(text = it1) }
                                            }
                                        },
                                        modifier = Modifier
                                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                                            .wrapContentWidth(Alignment.Start)
                                    )
                                }
                            }
                        },
                        drawerState = drawerState
                    ) {
                    }
                }*/

            }
        }
    }
}


