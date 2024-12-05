package com.capstone.surevenir.ui.screen.navmenu

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.capstone.surevenir.R
import com.capstone.surevenir.helper.UserPreferences
import com.capstone.surevenir.ui.viewmodel.TokenViewModel
import com.capstone.surevenir.ui.viewmodel.UserViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    navController: NavHostController,
    userViewModel: UserViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userPreferences = remember { UserPreferences(context) }

    val userData by userViewModel.currentUser.observeAsState()
    val isLoading by userViewModel.isLoading.observeAsState(initial = false)
    val errorMessage by userViewModel.errorMessage.observeAsState()

    val token by tokenViewModel.token.observeAsState()
    LaunchedEffect(token) {
        tokenViewModel.fetchToken()
        val userEmail = userPreferences.userEmail.firstOrNull()
        if (token != null && userEmail != null) {
            userViewModel.getUserById(userEmail, token!!)
        }
    }

    Scaffold(
        content = { paddingValues ->
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFED8A00))
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.onboard_1),
                            contentDescription = "Profile Background",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        )

                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(6.dp, Color(0xFFED8A00), CircleShape)
                                .align(Alignment.BottomCenter)
                        ) {
                            val imagePainter = rememberAsyncImagePainter(
                                model = userData?.profileImageUrl ?: R.drawable.user_placeholder
                            )

                            Image(
                                painter = imagePainter,
                                contentDescription = "Profile Picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = userData?.username ?: "Loading...",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = sfui_semibold
                    )
                    Text(
                        text = userData?.email ?: "Loading...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        fontFamily = sfui_med,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    MenuButton(
                        icon = Icons.Default.Email,
                        text = "Account Center",
                        onClick = { navController.navigate("accountCenter") }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    MenuButton(
                        icon = Icons.Default.Person,
                        text = "Edit Profile",
                        onClick = { navController.navigate("editProfile") }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    MenuButton(
                        icon = Icons.Default.Settings,
                        text = "Settings",
                        onClick = { navController.navigate("settings") }
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    MenuButton(
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        text = "Log Out",
                        onClick = {
                            scope.launch {
                                userPreferences.clearLoginData()
                                navController.navigate("signIn") {
                                    popUpTo("profile") { inclusive = true }
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                }
            }

            errorMessage?.let {
                Log.e("ProfileScreen", it)
                Toast(message = it)
            }
        }
    )
}

@Composable
private fun Toast(message: String) {
    val context = LocalContext.current
    LaunchedEffect(message) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun MenuButton(icon: ImageVector, text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFED8A00),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 20.dp,
            bottomEnd = 0.dp,
            bottomStart = 20.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(vertical = 10.dp)
            .padding(horizontal = 50.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text)
        }
    }
}