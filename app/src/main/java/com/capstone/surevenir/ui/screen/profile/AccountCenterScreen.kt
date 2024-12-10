package com.capstone.surevenir.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.capstone.surevenir.helper.UserPreferences
import com.capstone.surevenir.model.User
import com.capstone.surevenir.ui.components.TopBar
import com.capstone.surevenir.ui.viewmodel.TokenViewModel
import com.capstone.surevenir.ui.viewmodel.UserViewModel
import kotlinx.coroutines.flow.firstOrNull

@Composable
fun AccountCenterScreen(
    navController: NavHostController,
    userViewModel: UserViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }

    var userData by remember { mutableStateOf<User?>(null) }
    val currentUser by userViewModel.currentUser.observeAsState()
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

    LaunchedEffect(currentUser) {
        userData = currentUser
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        TopBar(
            title = "Account Center",
            onBackClick = { navController.navigateUp() }
        )

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFED8A00))
            }
        } else {
            Text(
                text = "SureVenir utilizes this data to authenticate your identity and ensure the safety of our community",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column {
                    AccountField(
                        label = "Email",
                        value = userData?.email ?: "Loading...",
                        onClick = { }
                    )
                    HorizontalDivider()
                    AccountField(
                        label = "Phone Number",
                        value = userData?.phone ?: "Not set",
                        onClick = {
                        }
                    )
                    HorizontalDivider()
                    AccountField(
                        label = "Password",
                        value = "••••••••",
                        onClick = {

                        }
                    )
                }
            }

            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun AccountField(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 18.sp
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Edit",
                tint = Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}