package com.app.classportal

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.classportal.ui.theme.RobotoMono
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.google.accompanist.pager.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AttendanceReportScreen(context: Context, navController: NavController) {
    val students = FileUtil.loadStudents(context)
    val attendanceRecords = FileUtil.loadAttendanceRecords(context)
    val units = listOf("Calculus II", "Linear Algebra", "Statistics I", "Probability and Statistics")
    val pagerState = rememberPagerState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Attendance Report", fontFamily = RobotoMono,color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back", tint = color4)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)

            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                edgePadding = 0.dp
            ) {
                units.forEachIndexed { index, unit ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                pagerState.scrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                unit,
                                color = if (pagerState.currentPage == index) color else Color.Gray
                            )
                        },

                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.Gray,

                        modifier = Modifier
                            .background(Color.Black),
                        

                    )
                }
            }
            HorizontalPager(
                count = units.size,
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val filteredAttendanceRecords = attendanceRecords.filter { it.unit == units[page] }

                val studentAttendance = students.map { student ->
                    val totalPresent = filteredAttendanceRecords.count { it.studentId == student.registrationID && it.present }
                    val totalAbsent = filteredAttendanceRecords.count { it.studentId == student.registrationID && !it.present }
                    val totalSessions = totalPresent + totalAbsent
                    val attendancePercentage = if (totalSessions > 0) (totalPresent * 100 / totalSessions) else 0
                    StudentAttendance(student, totalPresent, totalAbsent, attendancePercentage)
                }.sortedByDescending { it.attendancePercentage }

                LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                    item {
                        Row(
                            modifier = Modifier
                                .background(color = Color.Black)
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Name", modifier = Modifier.weight(1f), textAlign = TextAlign.Start, fontFamily = RobotoMono, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Present", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontFamily = RobotoMono, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Absent", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontFamily = RobotoMono, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Percent", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontFamily = RobotoMono, fontWeight = FontWeight.Bold, color = Color.White)

                        }
                        Divider(color = Color.Gray, thickness = 1.dp)
                    }
                    itemsIndexed(studentAttendance) { index, studentAttendance ->

                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            val percentageColor = when {
                                studentAttendance.attendancePercentage >= 75 -> Color(0xff51b541)
                                studentAttendance.attendancePercentage >= 50 -> Color.Yellow
                                else -> Color.Red
                            }

                            Text(studentAttendance.student.firstName, modifier = Modifier.weight(1f), textAlign = TextAlign.Start, fontFamily = RobotoMono, color = Color.White)
                            Text("${studentAttendance.totalPresent}", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontFamily = RobotoMono, color = Color.White)
                            Text("${studentAttendance.totalAbsent}", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontFamily = RobotoMono, color = Color.White)
                            Text("${studentAttendance.attendancePercentage}%", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, color = percentageColor, fontFamily = RobotoMono, fontWeight = FontWeight.Bold)
                        }
                        Divider(color = Color.Gray, thickness = 1.dp)
                    }
                }
            }
        }
    }
}
data class StudentAttendance(
    val student: Student,
    val totalPresent: Int,
    val totalAbsent: Int,
    val attendancePercentage: Int
)

@Preview
@Composable
fun AttendanceReportScreenPreview() {
    val navController = rememberNavController()
    AttendanceReportScreen(LocalContext.current, navController)
}



