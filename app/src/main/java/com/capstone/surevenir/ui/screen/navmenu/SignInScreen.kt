package com.capstone.surevenir.ui.screen.navmenu

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.capstone.surevenir.R
import com.capstone.surevenir.helper.UserPreferences
import com.capstone.surevenir.ui.component.CustomPasswordField
import com.capstone.surevenir.ui.component.CustomTextField
import com.capstone.surevenir.ui.component.isValidEmail
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

private const val GOOGLE_SIGN_IN_CODE = 1001

@Composable
fun SignInScreen(navController: NavController){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var emailErrorMessage by remember { mutableStateOf("") }
    var passwordErrorMessage by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var isGoogleLoading by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()
    val googleSignInClient = GoogleSignIn.getClient(
        context,
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    )

    Column (modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Spacer(modifier = Modifier.height(40.dp))


        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Sign in now",
            fontFamily = sfui_semibold,
            fontSize = 25.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Please sign in to continue our app",
            color = Color(0xFF7D848D),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))


        CustomTextField(
            value = email,
            onValueChange = {
                email = it
                emailErrorMessage = if (email.isEmpty()) "Email is required" else if (!isValidEmail(email)) "Invalid email format" else ""
            },
            label = "Email",
            isError = emailErrorMessage.isNotEmpty(),
            errorMessage = emailErrorMessage
        )

        Spacer(modifier = Modifier.height(10.dp))

        CustomPasswordField(
            password = password,
            onPasswordChange = {
                password = it
                passwordErrorMessage = if (password.isEmpty()) "Password is required" else if (password.length < 8) "Password must be more than 8 chars." else ""
            },
            passwordVisibility = passwordVisibility,
            onVisibilityChange = { passwordVisibility = !passwordVisibility },
            isError = passwordErrorMessage.isNotEmpty(),
            errorMessage = passwordErrorMessage
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Forget Password?",
            fontFamily = sfui_text,
            color = Color(0xFFFF7029),
            modifier = Modifier
                .align(Alignment.End)
                .clickable {                         navController.navigate("forgotPassword")
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                emailErrorMessage = if (email.isEmpty()) "Email is required" else if (!isValidEmail(email)) "Invalid email format" else ""
                passwordErrorMessage = if (password.isEmpty()) "Password is required" else if (password.length < 8) "Password must be more than 8 chars." else ""

                if (emailErrorMessage.isEmpty() && passwordErrorMessage.isEmpty()) {
                    isLoading = true
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                val user = task.result?.user
                                val token = user?.uid

                                val userPreferences = UserPreferences(context)
                                (context as ComponentActivity).lifecycleScope.launch {
                                    userPreferences.saveLoginState(isLoggedIn = true, token = token ?: "", email = email)
                                }

                                navController.navigate("home") {
                                    popUpTo("signIn") { inclusive = true }
                                }
                            } else {
                                emailErrorMessage = task.exception?.message ?: "Login failed"
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
                Text(
                    text = "Sign In",
                    fontFamily = sfui_semibold,
                )
            }
        }


        Spacer(modifier = Modifier.height(25.dp))


        SignUpText(navController = navController)


        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "Or continue with",
            color = Color(0xFFF707B81),
            fontSize = 15.sp,
            fontFamily = sfui_text,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))


        Button(
            onClick = {
                isGoogleLoading = true
                val signInIntent = googleSignInClient.signInIntent
                (context as Activity).startActivityForResult(signInIntent, GOOGLE_SIGN_IN_CODE)
            },
            modifier = Modifier
                .width(500.dp)
                .border(0.2.dp, Color.Black, RoundedCornerShape(40.dp))
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            if (isGoogleLoading) {
                CircularProgressIndicator(
                    color = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.logo_google),
                    contentDescription = "Login Google",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Continue with Google", color = Color.Black)
            }
        }
    }


}


@Composable
fun SignUpText(navController: NavController) {
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color(0xFFF707B81))) {
            append("Don't have an account? ")
        }
        pushStringAnnotation(tag = "SignUp", annotation = "SignUp")
        withStyle(style = SpanStyle(color = Color(0xFFFFA726))) {
            append("Sign Up")
        }
        pop()
    }


    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){
        ClickableText(
            text = annotatedText,
            onClick = { offset ->
                annotatedText.getStringAnnotations(tag = "SignUp", start = offset, end = offset)
                    .firstOrNull()?.let {
                        navController.navigate("signUp")
                    }
            },
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 15.sp,
                fontFamily = sfui_text
            ),
        )

    }


}


val sfui_semibold = FontFamily(
    Font(R.font.poppinssemibold, FontWeight.SemiBold)
)

val sfui_text = FontFamily(
    Font(R.font.poppinslight, FontWeight.Normal)
)
val sfui_med = FontFamily(
    Font(R.font.poppinsmedium, FontWeight.Medium)
)
