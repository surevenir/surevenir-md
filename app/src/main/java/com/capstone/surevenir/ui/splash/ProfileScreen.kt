package com.capstone.surevenir.ui.splash

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.capstone.surevenir.helper.UserPreferences
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavHostController) {

    val context = LocalContext.current


    Column {
        Text("Profile Screen")
        Button(
            onClick = {
                val userPreferences = UserPreferences(context)
                (context as ComponentActivity).lifecycleScope.launch {
                    userPreferences.clearLoginData()
                }
                navController.navigate("signIn")

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFED8A00)),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = "Log Out",
                fontFamily = sfui_semibold,
            )
        }
    }
}