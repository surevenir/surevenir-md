package com.capstone.surevenir.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController

@Composable
fun SuccessDialog(navController: NavController, showDialog: MutableState<Boolean>) {
    if (showDialog.value) {
        Dialog(onDismissRequest = { showDialog.value = false }) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sign Up Successful!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            showDialog.value = false
                            navController.navigate("signIn")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFED8A00))
                    ) {
                        Text(text = "Login")
                    }
                }
            }
        }
    }
}
