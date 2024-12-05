package com.capstone.surevenir.ui.screen.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import com.capstone.surevenir.R
import com.capstone.surevenir.ui.components.TopBar
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.capstone.surevenir.data.network.response.UpdateUserRequest
import com.capstone.surevenir.helper.UserPreferences
import com.capstone.surevenir.model.User
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.ui.viewmodel.TokenViewModel
import com.capstone.surevenir.ui.viewmodel.UserViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(
    navController: NavHostController,
    userViewModel: UserViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userPreferences = remember { UserPreferences(context) }

    var userData by remember { mutableStateOf<User?>(null) }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    val isLoading by userViewModel.isLoading.observeAsState(initial = false)
    val errorMessage by userViewModel.errorMessage.observeAsState()
    val updateSuccess by userViewModel.updateSuccess.observeAsState()
    val currentUser by userViewModel.currentUser.observeAsState()

    val token by tokenViewModel.token.observeAsState()
    LaunchedEffect(token) {
        tokenViewModel.fetchToken()
        val userEmail = userPreferences.userEmail.firstOrNull()
        if (token != null && userEmail != null) {
            userViewModel.getUserById(userEmail, token!!)
        }
    }

    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            userData = user
            fullName = user.fullName
            username = user.username
            address = user.address ?: ""
            phone = user.phone ?: ""
            profileImageUri = user.profileImageUrl?.let { Uri.parse(it) }
        }
    }

    LaunchedEffect(updateSuccess) {
        if (updateSuccess == true) {
            Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            navController.navigateUp()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        TopBar(
            title = "Edit Profile",
            onBackClick = { navController.navigateUp() },
            actions = {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFFED8A00)
                    )
                } else {
                    TextButton(
                        onClick = {
                            scope.launch {
                                val token = tokenViewModel.token.value
                                if (token != null && userData != null) {
                                    val request = UpdateUserRequest(
                                        fullName = fullName,
                                        username = username,
                                        address = address,
                                        phone = phone,
                                        profileImageUrl = profileImageUri?.toString()
                                    )
                                    userViewModel.updateUser(userData!!.id, token, request)
                                }
                            }
                        }
                    ) {
                        Text("Save", color = Color(0xFFED8A00), fontSize = 18.sp)
                    }
                }
            }
        )

        // Profile Image Section
        ProfileImageSection(
            profileImageUrl = profileImageUri?.toString(),
            onImageChange = { uri -> profileImageUri = uri }
        )

        // Form Fields
        ProfileField(
            label = "Full Name",
            value = fullName,
            onValueChange = { fullName = it }
        )
        ProfileField(
            label = "Username",
            value = username,
            onValueChange = { username = it }
        )
        ProfileField(
            label = "Phone",
            value = phone,
            onValueChange = { phone = it }
        )
        ProfileField(
            label = "Address",
            value = address,
            onValueChange = { address = it }
        )

        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun ProfileImageSection(
    profileImageUrl: String?,
    onImageChange: (Uri?) -> Unit
) {

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageChange(uri)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .border(6.dp, Color.Gray, CircleShape)
        ) {
            val imagePainter = if (profileImageUrl != null) {
                rememberAsyncImagePainter(model = profileImageUrl)
            } else {
                painterResource(id = R.drawable.user_placeholder)
            }

            Image(
                painter = imagePainter,
                contentDescription = "Profile Picture",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.BottomCenter)
                .offset(x = 40.dp, y = (-20).dp)
                .background(Color(0xFFED8A00), CircleShape)
                .clickable {  galleryLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Change Picture",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


@Composable
private fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = sfui_semibold),
            color = if (isFocused) Color(0xFFED8A00) else Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color(0xFFED8A00),
                unfocusedBorderColor = Color.Gray
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = Color.Black,
                fontFamily = sfui_semibold
            ),
            singleLine = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    val navController = rememberNavController()
    EditProfileScreen(navController = navController)
}