package com.capstone.surevenir.ui.splash

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.capstone.surevenir.R
import com.capstone.surevenir.components.ProductCard
import com.capstone.surevenir.components.ScanHistoryCard
import com.capstone.surevenir.model.BottomNavItem
import com.capstone.surevenir.model.Product
import com.capstone.surevenir.ui.camera.ComposeFileProvider
import com.capstone.surevenir.ui.camera.ImageCaptureVM
import com.capstone.surevenir.ui.camera.PermissionUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Home(navController: NavController) {
    Scaffold(
    ) { padding ->

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                val images = listOf(
                    R.drawable.bali3,
                    R.drawable.bali2,
                    R.drawable.bali1
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    HeroSlider(images = images)
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                val scanHistoryProducts = listOf(
                    Product(
                        R.drawable.product_image,
                        "Dream Catcher",
                        "Authentic and beautiful souvenir for your home decor.",
                        "IDR 50.000 - 150.000"
                    ),
                    Product(
                        R.drawable.product_image,
                        "Handicraft",
                        "Handmade craft from local artisans in Bali.",
                        "IDR 100.000 - 250.000"
                    )
                )
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "Scan History",
                            fontSize = 20.sp,
                            fontFamily = sfui_semibold,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                        )
                        Text(
                            text = "See All History",
                            fontSize = 20.sp,
                            fontFamily = sfui_semibold,
                            color = Color(0xFFCC5B14),
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .clickable { navController.navigate("allHistory") }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    ScanHistorySection(products = scanHistoryProducts)
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "All Products",
                            fontSize = 20.sp,
                            fontFamily = sfui_semibold,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                        )
                        Text(
                            text = "See All Products",
                            fontSize = 20.sp,
                            fontFamily = sfui_semibold,
                            color = Color(0xFFCC5B14),
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .clickable{ navController.navigate("allProduct") }
                        )
                    }
                    }
            }

            val allProducts = listOf(
                Product(
                    R.drawable.product_image,
                    "Bali Hand Magnet",
                    "Magnet souvenir for your fridge.",
                    "IDR 25.000"
                ),
                Product(
                    R.drawable.product_image,
                    "Keychain",
                    "Customizable keychains for your loved ones.",
                    "IDR 15.000"
                ),
                Product(
                    R.drawable.product_image,
                    "T-shirt Bali",
                    "High-quality Bali-themed T-shirt.",
                    "IDR 150.000"
                )
            )

            items(allProducts.chunked(2)) { rowProducts ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowProducts.forEach { product ->
                        ProductCard(
                            imageRes = product.imageRes,
                            title = product.title,
                            price = product.price,
                            rating = "5",
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 8.dp)
                                .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                        )
                    }
                    if (rowProducts.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}



@Composable
fun ScanHistorySection(products: List<Product>) {
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
        items(products) { product ->
            ScanHistoryCard(
                imageRes = product.imageRes,
                title = product.title,
                description = product.description,
                price = product.price,
                modifier = Modifier
                    .width(350.dp)
                    .padding(end = 10.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
            )
        }

    }
}




@Composable
fun StickyTopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .shadow(1.dp)
    ) {
        TopBar()

    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(32.dp)
        )

        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = "Your Location",
                fontFamily = sfui_text,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Ubud ▼",
                fontFamily = sfui_semibold,
                color = Color(0xFFFFA726),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


@Composable
fun HeroSlider(images: List<Int>) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { images.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % images.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) { page ->
        HeroSection(imageRes = images[page])
    }
}

@Composable
fun HeroSection(imageRes: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
        )
    }
}




@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(
            title = "Home",
            iconActive = R.drawable.ic_home_selected,
            iconInactive = R.drawable.ic_home,
            route = "home"
        ),
        BottomNavItem(
            title = "Shop",
            iconActive = R.drawable.ic_shop_selected,
            iconInactive = R.drawable.ic_shop,
            route = "shop"
        ),
        BottomNavItem(
            title = "Favorites",
            iconActive = R.drawable.ic_favorite_selected,
            iconInactive = R.drawable.ic_favorite,
            route = "favorites"
        ),
        BottomNavItem(
            title = "Profile",
            iconActive = R.drawable.ic_profile_selected,
            iconInactive = R.drawable.ic_profile,
            route = "profile"
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box {
        BottomNavigation(
            backgroundColor = Color.White,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            items.forEachIndexed { index, item ->
                if (index == 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }

                BottomNavigationItem(
                    icon = {
                        Image(
                            painter = painterResource(
                                id = if (currentRoute == item.route) item.iconActive else item.iconInactive
                            ),
                            contentDescription = item.title,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            fontSize = 10.sp,
                            color = if (currentRoute == item.route) Color(0xFFCC5B14) else Color.Gray
                        )
                    },
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    alwaysShowLabel = true,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun FloatingButtonWithIntent(
    navController: NavController,
    imageCaptureViewModel: ImageCaptureVM
) {
    var isExtended by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
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

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            scope.launch {
                navController.navigate("preview") {
                    popUpTo("home") { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageCaptureViewModel.setImageUri(it)
            scope.launch {
                navController.navigate("preview") {
                    popUpTo("home") { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }

    Box {
        AnimatedVisibility(
            visible = isExtended,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier
                .offset(y =(-100).dp)
                .align(Alignment.BottomEnd)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        isExtended = false
                        if (PermissionUtils.hasRequiredPermissions(context)) {
                            val uri = ComposeFileProvider.getImageUri(context)
                            imageCaptureViewModel.setImageUri(uri)
                            cameraLauncher.launch(uri)
                        } else {
                            launcher.launch(PermissionUtils.getRequiredPermissions())
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFED8A00),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .widthIn(min = 180.dp, max = 240.dp)
                        .align(Alignment.End),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Camera",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Camera")
                }

                Button(
                    onClick = {
                        isExtended = false
                        if (PermissionUtils.hasRequiredPermissions(context)) {
                            galleryLauncher.launch("image/*")
                        } else {
                            launcher.launch(PermissionUtils.getRequiredPermissions())
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFED8A00),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .widthIn(min = 180.dp, max = 240.dp)
                        .align(Alignment.End),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Gallery",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Gallery")
                }
            }
        }

        FloatingActionButton(
            onClick = { isExtended = !isExtended },
            containerColor = Color(0xFFED8A00),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
                .offset(y = 20.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_scan),
                contentDescription = "Scan",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    Home(navController = NavController(LocalContext.current))
}


