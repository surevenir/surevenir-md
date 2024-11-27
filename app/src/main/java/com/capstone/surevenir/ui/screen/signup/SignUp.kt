package com.capstone.surevenir.ui.screen.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.capstone.surevenir.R
import com.capstone.surevenir.data.network.response.CreateUserRequest
import com.capstone.surevenir.ui.SuccessDialog
import com.capstone.surevenir.ui.component.CustomPasswordField
import com.capstone.surevenir.ui.component.CustomTextField
import com.capstone.surevenir.ui.component.isValidEmail
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.ui.screen.navmenu.sfui_text
import com.capstone.surevenir.ui.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignUpScreen(navController: NavController, userViewModel: UserViewModel = hiltViewModel()) {
    val auth = FirebaseAuth.getInstance()
    val showDialog = remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    SuccessDialog(navController, showDialog)

    val userResponse by userViewModel.userResponse.observeAsState()
    userResponse?.let {
        if (it.success == true) {
            userViewModel.clearState()
        }
    }

    SuccessDialog(navController, showDialog)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFE7E7E9))
                .clickable { navController.popBackStack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow),
                contentDescription = "Back",
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Sign up now",
            fontFamily = sfui_semibold,
            fontSize = 25.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Please fill the details and create account",
            color = Color(0xFF7D848D),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        CustomTextField(
            value = name,
            onValueChange = {
                name = it
                nameError = if (it.isEmpty()) "Name is required" else ""
            },
            label = "Name",
            isError = nameError.isNotEmpty(),
            errorMessage = nameError
        )

        Spacer(modifier = Modifier.height(10.dp))

        CustomTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = when {
                    it.isEmpty() -> "Email is required"
                    !isValidEmail(it) -> "Invalid email format"
                    else -> ""
                }
            },
            label = "Email",
            isError = emailError.isNotEmpty(),
            errorMessage = emailError
        )

        Spacer(modifier = Modifier.height(10.dp))

        CustomPasswordField(
            password = password,
            onPasswordChange = {
                password = it
                passwordError = when {
                    it.isEmpty() -> "Password is required"
                    it.length < 8 -> "Password must be at least 8 characters"
                    else -> ""
                }
            },
            passwordVisibility = passwordVisibility,
            onVisibilityChange = { passwordVisibility = !passwordVisibility },
            isError = passwordError.isNotEmpty(),
            errorMessage = passwordError
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                nameError = if (name.isEmpty()) "Name is required" else ""
                emailError = when {
                    email.isEmpty() -> "Email is required"
                    !isValidEmail(email) -> "Invalid email format"
                    else -> ""
                }
                passwordError = when {
                    password.isEmpty() -> "Password is required"
                    password.length < 8 -> "Password must be at least 8 characters"
                    else -> ""
                }

                if (nameError.isEmpty() && emailError.isEmpty() && passwordError.isEmpty()) {
                    isLoading = true

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                val firebaseUser = task.result?.user
                                val firebaseUid = firebaseUser?.uid ?: ""

                                val request = CreateUserRequest(
                                    id = firebaseUid,
                                    fullName = name,
                                    username = name,
                                    email = email,
                                    password = password,
                                    phone = "",
                                    role = "USER",
                                    provider = "EMAIL",
                                    longitude = "",
                                    latitude = "",
                                    address = ""
                                )
                                showDialog.value = true
                                userViewModel.createUser(request)
                                isLoading = false
                            } else {
                                isLoading = false
                                errorMessage = task.exception?.message.toString()
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
                Text(text = "Sign Up", fontFamily = sfui_semibold)
            }
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 8.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Already have an account? ",
                fontSize = 15.sp,
                fontFamily = sfui_text,
                color = Color(0xFF707B81),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Sign in",
                fontSize = 15.sp,
                fontFamily = sfui_text,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFFFA726),
                modifier = Modifier.clickable {
                    navController.navigate("signIn")
                }
            )
        }

        Spacer(modifier = Modifier.height(15.dp))
    }
}

