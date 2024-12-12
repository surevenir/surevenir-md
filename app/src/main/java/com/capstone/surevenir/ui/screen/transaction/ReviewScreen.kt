package com.capstone.surevenir.ui.screen.transaction

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.capstone.surevenir.R
import com.capstone.surevenir.helper.formatCurrency
import com.capstone.surevenir.model.ProductCheckout
import com.capstone.surevenir.ui.components.ImagePickerDialog
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.ui.viewmodel.ReviewsViewModel
import com.capstone.surevenir.ui.viewmodel.TokenViewModel

@Composable
fun ReviewScreen(
    navController: NavController,
    product: ProductCheckout,
    reviewsViewModel: ReviewsViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel = hiltViewModel()
) {
    var rating by remember { mutableIntStateOf(0) }
    var review by remember { mutableStateOf("") }
    var selectedImages by remember { mutableStateOf<List<String>>(emptyList()) }
    var showImageSourceDialog by remember { mutableStateOf(false) }
    val reviewState by reviewsViewModel.reviewState.observeAsState()

    val token by tokenViewModel.token.observeAsState()

    LaunchedEffect(Unit) {
        if (token == null) {
            tokenViewModel.fetchToken()
        }
    }

    LaunchedEffect(token) {
        token?.let {
            Log.d("ReviewScreen", "Current token: $it")
        }
    }

    BackHandler {
        navController.popBackStack()
    }

    LaunchedEffect(reviewState) {
        when (reviewState) {
            is ReviewsViewModel.ReviewState.Success -> {
                navController.previousBackStackEntry?.savedStateHandle?.set("reviewSubmitted", true)
                reviewsViewModel.resetReviewState()
                navController.popBackStack()
            }
            is ReviewsViewModel.ReviewState.Error -> {
                if ((reviewState as ReviewsViewModel.ReviewState.Error).message.contains("already reviewed")) {
                    navController.popBackStack()
                }
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 33.dp, top = 20.dp, bottom = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE7E7E9))
                            .clickable(onClick = { navController.popBackStack() }),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow),
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = "Review Product",
                        fontSize = 25.sp,
                        fontFamily = sfui_semibold,
                        color = Color(0xFFCC5B14)
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 30.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                            .height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = product.images.firstOrNull()?.url,
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(24.dp))
                        Column {
                            Text(
                                text = product.name,
                                fontWeight = FontWeight.Bold,
                                fontFamily = sfui_semibold,
                                fontSize = 16.sp,
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = formatCurrency(product.price),
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_shop),
                                    contentDescription = null,
                                    modifier = Modifier.size(10.dp),
                                    colorFilter = ColorFilter.tint(Color.Gray)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = product.merchant.name,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Rate Product",
                    fontSize = 16.sp,
                    fontFamily = sfui_semibold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    repeat(5) { index ->
                        Icon(
                            painter = painterResource(
                                id = if (index < rating) R.drawable.ic_star
                                else R.drawable.ic_star_outline
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .clickable { rating = index + 1 },
                            tint = Color(0xFFED8A00)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Add some photos",
                    fontSize = 16.sp,
                    fontFamily = sfui_semibold
                )
                PhotoUploadSection(
                    selectedImages = selectedImages,
                    onAddClick = { showImageSourceDialog = true },
                    onRemoveImage = { index ->
                        selectedImages = selectedImages.filterIndexed { i, _ -> i != index }
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Write your review",
                    fontSize = 16.sp,
                    fontFamily = sfui_semibold
                )

                OutlinedTextField(
                    value = review,
                    onValueChange = { review = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    placeholder = {
                        Text(
                            text = "Share your review and help others make the best choice!",
                            color = Color.Gray
                        )
                    },
                    shape = RoundedCornerShape(8.dp)
                )

                Button(
                    onClick = {
                        token?.let { tokenValue ->
                            reviewsViewModel.postReview(
                                token = tokenValue,
                                rating = rating,
                                comment = review,
                                productId = product.id,
                                imageUris = selectedImages
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    enabled = rating > 0 && review.isNotBlank() && selectedImages.isNotEmpty() && token != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFED8A00)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (reviewsViewModel.isLoading.value == true) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = "SEND REVIEW",
                            modifier = Modifier.padding(vertical = 8.dp),
                            fontFamily = sfui_semibold
                        )
                    }
                }
            }
        }
        ImagePickerDialog(
            showDialog = showImageSourceDialog,
            onDismiss = { showImageSourceDialog = false },
            onImageSelected = { uri ->
                selectedImages = selectedImages + uri.toString()
            }
        )
    }
}

@Composable
fun PhotoUploadSection(
    selectedImages: List<String>,
    onAddClick: () -> Unit,
    onRemoveImage: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F9FD)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        if (selectedImages.isEmpty()) {
            Column(
                modifier = Modifier
                    .clickable(onClick = onAddClick)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_photo),
                    contentDescription = "Upload",
                    modifier = Modifier.size(48.dp),
                    tint = Color(0xFF9E9E9E)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Upload your photos",
                    color = Color(0xFF9E9E9E),
                    fontSize = 14.sp,
                    fontFamily = sfui_semibold
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    selectedImages.forEachIndexed { index, imageUri ->
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                        ) {
                            AsyncImage(
                                model = imageUri,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = { onRemoveImage(index) },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .size(30.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_close),
                                    contentDescription = "Remove",
                                    tint = Color.Red,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }

                    if (selectedImages.size < 4) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .clickable(onClick = onAddClick),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_add_photo),
                                    contentDescription = "Add More",
                                    tint = Color(0xFF9E9E9E),
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "Add more",
                                    color = Color(0xFF9E9E9E),
                                    fontSize = 12.sp,
                                    fontFamily = sfui_semibold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}