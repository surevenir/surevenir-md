package com.capstone.surevenir.ui.screen.navmenu

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.capstone.surevenir.R
import com.capstone.surevenir.helper.UserPreferences
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavHostController) {

    val context = LocalContext.current

    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Background Image
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
                            .border(4.dp, Color(0xFFED8A00), CircleShape)
                            .align(Alignment.BottomCenter)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_google),
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                        )
                    }
                }

                // Profile Name and Email
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Audry Jenner",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
                Text(
                    text = "audryjenner@gmail.com",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Buttons
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
                Spacer(modifier = Modifier.height(6.dp))
                MenuButton(
                    icon = Icons.Default.Person,
                    text = "Log Out",
                    onClick = {
                        val userPreferences = UserPreferences(context)
                        (context as ComponentActivity).lifecycleScope.launch {
                            userPreferences.clearLoginData()
                        }
                        navController.navigate("signIn")
                    }
                )
            }
        }
    )
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
