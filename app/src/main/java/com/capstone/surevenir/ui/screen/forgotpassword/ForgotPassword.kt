package com.capstone.surevenir.ui.screen.forgotpassword

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.capstone.surevenir.R
import com.capstone.surevenir.ui.component.CustomTextField
import com.capstone.surevenir.ui.component.isValidEmail
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.ui.screen.navmenu.sfui_text
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ForgotPassword(navController: NavController){
    var email by remember { mutableStateOf("") }
    var emailErrorMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }


    val auth = FirebaseAuth.getInstance()

    Column (modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFE7E7E9))
                .clickable(onClick = { /* Handle back action */ }),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow),
                contentDescription = "Back",
                modifier = Modifier
                    .size(48.dp)
                    .clickable(onClick = ({navController.popBackStack() }))
            )

        }
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Forgot password",
            fontFamily = sfui_semibold,
            fontSize = 25.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Enter your email account to reset  your password",
            color = Color(0xFF7D848D),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        CustomTextField(
            value = email,
            onValueChange = {
                email = it
                emailErrorMessage = if (email.isEmpty()) "Email is required"
                else if (!isValidEmail(email)) "Invalid email format"
                else ""
            },
            label = "Email",
            isError = emailErrorMessage.isNotEmpty(),
            errorMessage = emailErrorMessage
        )

        Spacer(modifier = Modifier.height(20.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                emailErrorMessage = if (email.isEmpty()) "Email is required"
                else if (!isValidEmail(email)) "Invalid email format"
                else ""

                if (emailErrorMessage.isEmpty()) {
                    isLoading = true
                    auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                showDialog = true
                            } else {
                                emailErrorMessage = task.exception?.message ?: "Failed to send reset email"
                            }
                        }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFED8A00)),
            shape = RoundedCornerShape(20.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(text = "Reset Password", fontFamily = sfui_semibold)
            }
        }


        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        navController.navigate("signIn")
                    }) {
                        Text("Done")
                    }
                },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Check your email", fontSize = 20.sp, fontFamily = sfui_semibold)
                        Text(
                            text = "We have sent password recovery instructions to your email.",
                            fontFamily = sfui_text,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ForgotPasswordPreview(){
    ForgotPassword(navController = rememberNavController())
}



