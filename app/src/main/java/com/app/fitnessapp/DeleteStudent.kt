package com.app.fitnessapp

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.fitnessapp.ui.theme.RobotoMono

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteStudentScreen(context: Context, navController: NavController) {
    var students by remember { mutableStateOf(FileUtil.loadStudents(context)) }
    var studentIdToDelete by remember { mutableStateOf("") }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var studentNameToDelete by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Delete Student",
                    fontWeight = FontWeight.Bold,
                    fontFamily = RobotoMono) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = color
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .background(background)
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn (modifier = Modifier
                .border(1.dp, Color.Black,
                    RoundedCornerShape(8.dp))){
                item {
                    Text("Total Students: ${students.size}", modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
                }
                itemsIndexed(students) { index, student ->
                    val rowlist = if (index % 2 == 0) Color(0xffA0E9FF) else Color(0xff89CFF3)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()

                            .height(50.dp)
                            .background(rowlist)
                            .clickable {
                                studentIdToDelete = student.studentid
                                studentNameToDelete = student.name
                                showConfirmationDialog = true
                            },
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(student.studentid,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            fontFamily = RobotoMono,)
                        Text(student.name)
                    }
                }
            }
            Text("Select Student to Delete", modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
            if (showConfirmationDialog) {
                AlertDialog(
                    onDismissRequest = { showConfirmationDialog = false },
                    confirmButton = {
                        Button(onClick = {
                            FileUtil.deleteStudent(context, studentIdToDelete)
                            showConfirmationDialog = false
                            students = FileUtil.loadStudents(context) // Reload the list of students after deletion
                        }) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showConfirmationDialog = false }) {
                            Text("Cancel")
                        }
                    },
                    title = { Text("Delete Student") },
                    text = { Text("Are you sure you want to delete $studentNameToDelete?") }
                )
            }
        }
    }
}


@Preview
@Composable
fun DeleteStudentScreenPreview() {
    val navController = rememberNavController()
    DeleteStudentScreen(LocalContext.current, navController)
}





