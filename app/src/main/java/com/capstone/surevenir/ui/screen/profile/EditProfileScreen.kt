package com.capstone.surevenir.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import com.capstone.surevenir.R
import com.capstone.surevenir.ui.components.TopBar

@Composable
fun EditProfileScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("audryjenner_") }
    var dateOfBirth by remember { mutableStateOf("January 1, 2001") }
    var gender by remember { mutableStateOf("Unknown") }
    var address by remember { mutableStateOf("Universitas Udayana") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar(
            title = "Edit Profile",
            onBackClick = { navController.navigateUp() },
            actions = {
                TextButton(onClick = { /* Save changes */ }) {
                    Text("Done", color = Color(0xFFED8A00))
                }
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color(0xFFED8A00), CircleShape)
                    .align(Alignment.Center)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_google),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Change Picture",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .background(Color(0xFFED8A00), CircleShape)
                        .padding(4.dp),
                    tint = Color.White
                )
            }
        }

        ProfileField(label = "Username", value = username) { username = it }
        ProfileField(label = "Date of Birth", value = dateOfBirth) { dateOfBirth = it }
        ProfileField(label = "Gender", value = gender) { gender = it }
        ProfileField(label = "Address", value = address) { address = it }
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFED8A00),
                unfocusedBorderColor = Color.Gray
            )
        )
    }
}
