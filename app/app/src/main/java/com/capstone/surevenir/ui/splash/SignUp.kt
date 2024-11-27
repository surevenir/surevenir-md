package com.capstone.surevenir.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import com.capstone.surevenir.R
import com.capstone.surevenir.ui.CustomPasswordField
import com.capstone.surevenir.ui.CustomTextField
import com.capstone.surevenir.ui.SuccessDialog
import com.capstone.surevenir.ui.isValidEmail
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignUpScreen(navController: NavController){

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
            text = "Sign up now",
            fontFamily = sfui_semibold,
            fontSize = 25.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Please fill the details and create account",
            color = Color(0xFF7D848D),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        CustomTextField(
            value = name,
            onValueChange = {
                name = it
                nameError = if (name.isEmpty()) "Name is required" else ""
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
                emailError = if (email.isEmpty()) "Email is required" else if (!isValidEmail(email)) "Invalid email format" else ""
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
                passwordError = if (password.isEmpty()) "Password is required" else if (password.length < 8) "Password must be more than 8 chars." else ""
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
                emailError = if (email.isEmpty()) "Email is required" else if (!isValidEmail(email)) "Invalid email format" else ""
                passwordError = if (password.isEmpty()) "Password is required" else if (password.length < 8) "Password must be more than 8 chars." else ""

                if (nameError.isEmpty() && emailError.isEmpty() && passwordError.isEmpty()) {
                    isLoading = true
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            isLoading = false
                            if (it.isSuccessful) {
                                showDialog.value = true
                            } else {
                                errorMessage = it.exception?.message.toString()
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


        Spacer(modifier = Modifier.height(25.dp))


        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Already have an account? ",
                fontSize = 15.sp,
                fontFamily = sfui_text,
                color = Color(0xFFF707B81),
                style = MaterialTheme.typography.bodySmall,)
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
