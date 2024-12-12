package com.capstone.surevenir.ui.camera

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.capstone.surevenir.R
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.ui.viewmodel.TokenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PreviewScreen(
    navController: NavController,
    imageCaptureVM: ImageCaptureVM,
    tokenViewModel: TokenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val imageUri = imageCaptureVM.currentImageUri
    var showDialog by remember { mutableStateOf(false) }
    val isLoading by imageCaptureVM.isLoading.collectAsState()
    val predictionResult by imageCaptureVM.predictionResult.collectAsState()
    val token by tokenViewModel.token.observeAsState()

    var showResultPopup by remember { mutableStateOf(false) }
    var isResultSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(predictionResult) {
        predictionResult?.fold(
            onSuccess = {
                isResultSuccess = true
                showResultPopup = true
                delay(2000)
                navController.navigate("result") {
                    popUpTo("preview") { inclusive = true }
                }
            },
            onFailure = { error ->
                isResultSuccess = false
                showResultPopup = true
                delay(2000)
                Toast.makeText(
                    context,
                    error.message ?: "Prediction failed",
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }

    LaunchedEffect(Unit) {
        if (token == null) {
            tokenViewModel.fetchToken()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageCaptureVM.setImageUri(it)
            scope.launch {
                navController.navigate("preview") {
                    popUpTo("home") { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (!allGranted) {
            Toast.makeText(
                context,
                "Permissions required to use camera and access gallery",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFED8A00))
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.navigateUp() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = "Image Preview",
                fontSize = 24.sp,
                fontFamily = sfui_semibold,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Image(
                painter = painterResource(id = R.drawable.surevenir_logo_home),
                contentDescription = "Logo",
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(30.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .padding(top = 10.dp, bottom = 50.dp)
            ) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Preview",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            token?.let { currentToken ->
                                imageCaptureVM.predictImage(currentToken, context)
                            } ?: run {
                                Toast.makeText(
                                    context,
                                    "Please wait, authenticating...",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFED8A00)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Analyze",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontFamily = sfui_semibold,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Retake",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontFamily = sfui_semibold,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80000000))
                .clickable(enabled = false) { },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(24.dp)
                    .width(200.dp)
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = Color(0xFFED8A00)
                    )
                    Text(
                        text = "Analyzing image...",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 16.sp,
                        fontFamily = sfui_semibold,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFED8A00)
                    )
                }
            }
        }
    }

    if (showResultPopup) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80000000)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (isResultSuccess) Color(0xFFED8A00)
                        else Color.Red)
                    .padding(24.dp)
                    .width(200.dp)
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = if (isResultSuccess) "Analyze Success" else "Analyze Failed",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 24.sp,
                        fontFamily = sfui_semibold,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    if (showDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x99000000))
                .clickable(enabled = false) { },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(280.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Choose Image Source",
                        color = Color(0xFFED8A00),
                        fontSize = 18.sp,
                        fontFamily = sfui_semibold,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    TextButton(
                        onClick = {
                            showDialog = false
                            navController.navigate("camera?previousScreen=preview")
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Camera",
                                tint = Color(0xFFED8A00),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Camera",
                                color = Color(0xFFED8A00),
                                fontSize = 14.sp,
                                fontFamily = sfui_semibold
                            )
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    TextButton(
                        onClick = {
                            showDialog = false
                            if (PermissionUtils.hasRequiredPermissions(context)) {
                                galleryLauncher.launch("image/*")
                            } else {
                                permissionLauncher.launch(PermissionUtils.getRequiredPermissions())
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "Gallery",
                                tint = Color(0xFFED8A00),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Gallery",
                                color = Color(0xFFED8A00),
                                fontSize = 14.sp,
                                fontFamily = sfui_semibold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    TextButton(
                        onClick = { showDialog = false },
                        modifier = Modifier
                            .fillMaxWidth()

                    ) {
                        Text(
                            "Cancel",
                            color = Color.Gray,
                            fontSize = 16.sp,
                            fontFamily = sfui_semibold,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}