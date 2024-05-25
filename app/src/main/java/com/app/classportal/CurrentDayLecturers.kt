
package com.app.classportal


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.classportal.FileUtil.loadTimetable
import com.app.classportal.ui.theme.RobotoMono
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentDayEventsScreen() {
    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val timetableData = remember { loadTimetable(context) }
    val lecturesToday = timetableData[dayOfWeek] ?: emptyList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row (modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center){
                        Text("Today's Lectures", fontFamily = RobotoMono, color = textColor, fontSize = 20.sp)
                    }


                        },
                actions = {
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryColor)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .background(backbrush)
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                itemsIndexed(lecturesToday) { index, item ->
                    TimetableItemRow(item,
                        onEdit = {
                            // No edit action for this screen
                        },
                        onDelete = {
                            // No delete action for this screen
                        }
                    )
                    Divider(color = Color.Gray, thickness = 1.dp)
                }
            }
        }
    }
}





@Preview
@Composable
 fun timetablePreview(){
     CurrentDayEventsScreen()
 }