package com.capstone.surevenir.ui.screen.navmenu

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.capstone.surevenir.BuildConfig
import com.capstone.surevenir.R
import com.capstone.surevenir.data.network.response.ProductData
import com.capstone.surevenir.helper.UserPreferences
import com.capstone.surevenir.ui.components.ProductCard
import com.capstone.surevenir.model.BottomNavItem
import com.capstone.surevenir.model.Market
import com.capstone.surevenir.ui.camera.ImageCaptureVM
import com.capstone.surevenir.ui.camera.PermissionUtils
import com.capstone.surevenir.ui.components.MarketCard
import com.capstone.surevenir.ui.components.ScanHistoryCard
import com.capstone.surevenir.ui.components.SectionHeader
import com.capstone.surevenir.ui.viewmodel.GeocodingViewModel
import com.capstone.surevenir.ui.viewmodel.LeaderboardViewModel
import com.capstone.surevenir.ui.viewmodel.MarketViewModel
import com.capstone.surevenir.ui.viewmodel.NotificationViewModel
import com.capstone.surevenir.ui.viewmodel.ProductViewModel
import com.capstone.surevenir.ui.viewmodel.ScanHistoryViewModel
import com.capstone.surevenir.ui.viewmodel.TokenViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Home(navController: NavController, tokenViewModel: TokenViewModel = hiltViewModel(), marketViewModel: MarketViewModel = hiltViewModel(), geocodingViewModel: GeocodingViewModel = hiltViewModel(), productViewModel: ProductViewModel = hiltViewModel(),     notificationViewModel: NotificationViewModel = hiltViewModel(), leaderboardViewModel: LeaderboardViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val geocodingViewModel: GeocodingViewModel = hiltViewModel()
    var subDistrict by remember { mutableStateOf("Loading...") }
    val productList = remember { mutableStateOf<List<ProductData>?>(null) }
    val pagingProducts = productViewModel.productPagingFlow.collectAsLazyPagingItems()

    val userPreferences = remember { UserPreferences(context) }
    val username = userPreferences.userName.collectAsState(initial = "User")

    val markets = remember { mutableStateOf<List<Market>?>(null) }
    val scope = rememberCoroutineScope()

    val unreadCount by notificationViewModel.unreadCount.observeAsState(0)
    val notificationIcon = if (unreadCount > 0) R.drawable.notification_top_new else R.drawable.notification_top
    val userPoints by leaderboardViewModel.currentUserPoints.collectAsState()


    val token by tokenViewModel.token.observeAsState()

    LaunchedEffect(Unit) {
        tokenViewModel.fetchToken()
    }

    LaunchedEffect(token) {
        token?.let { productViewModel.getProducts(it) }
    }

    LaunchedEffect(token) {
        token?.let {
            leaderboardViewModel.fetchLeaderboard(it) // Fetch leaderboard data saat Home dibuka
        }
    }


    Scaffold(
    ) { padding ->

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(10.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

//            Sliding Username Text

            item {
                GamificationHeader(
                    username = "${username.value}",
                    points = userPoints, // Gunakan points dari leaderboard
                    onLeaderboardClick = {
                        navController.navigate("leaderboard")
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                val images = listOf(
                    R.drawable.bali3,
                    R.drawable.bali2,
                    R.drawable.bali1
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    HeroSlider(images = images)
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {

                Column {
                    SectionHeader(title = "Scan History", actionText = "See All History", navController)
                    Spacer(modifier = Modifier.height(10.dp))
                    ScanHistorySection()
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                androidx.compose.material3.Button(
                    onClick = {
                        navController.navigate("surevenirai")

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(0xFF2D1403)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "Try Our SurevenirGenAi",
                        color = Color.White,
                        fontFamily = sfui_semibold
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionHeader(title = "All Markets", actionText = "See All Markets", navController)
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                if (token != null) {
                    LaunchedEffect(token) {
                        marketViewModel.getMarkets("Bearer $token") { marketList ->
                            markets.value = marketList
                            Log.d("ShopScreen", "Markets fetched: $marketList")
                        }
                    }
                } else {
                    Log.d("ShopScreen", "Token is null")
                }
                MarketSection(markets = markets.value ?: emptyList(), navController)
                Spacer(modifier = Modifier.height(18.dp))
            }


            item {
                SectionHeader(title = "All Products", actionText = "See All Products", navController)
                HomeProductsSection(navController, pagingProducts)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun GamificationHeader(
    username: String,
    points: Int,
    onLeaderboardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 5.dp)
    ) {
        // First Row: Greeting and Leaderboard
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Greeting Text
            Text(
                text = "Hello, $username!",
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = sfui_semibold,
                color = Color(0xFF1E1E1E),
                fontSize = 17.sp, // Consider using responsiveFontSize if needed
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Leaderboard Text
            Text(
                text = "See Leaderboards",
                modifier = Modifier
                    .clickable(onClick = onLeaderboardClick)
                    .padding(bottom = 2.dp)
                    .semantics { contentDescription = "Navigate to Leaderboards" },
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFFFF5524),
                fontSize = 15.sp, // Consider using responsiveFontSize if needed
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(4.dp)) // Space between rows

        // Second Row: Animated Informational Text and Points
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top // Align to Top to accommodate multi-line text
        ) {
            // Informational Text: Animated
            AnimatedHighlightText(
                phrases = listOf(
                    "Get 100 Points per Scan You Make",
                    "Find nearby market or shop in Your Location",
                    "Having a question? Ask on our SurevenirGenAi",
                    "Click on market to see best shop",
                    "Click love icon to add your product to favorites"
                ),
                modifier = Modifier.weight(1f),
                highlightColor = Color(0xFFFFCC00), // Customize as needed
                normalColor = Color(0xFF1E1E1E),
                wordHighlightDuration = 500,       // Highlight duration per word
                phraseDisplayDuration = 3000       // Total display duration per phrase
            )

            Spacer(modifier = Modifier.width(8.dp)) // Space between AnimatedHighlightText and Points Text

            // Points Text
            Text(
                text = "$points Points",
                style = MaterialTheme.typography.titleMedium,
                fontFamily = sfui_semibold,
                color = Color(0xFF1E1E1E),
                fontSize = 16.sp, // Consider using responsiveFontSize if needed
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
fun HomeProductsSection(
    navController: NavController,
    pagingProducts: LazyPagingItems<ProductData>
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            count = minOf(pagingProducts.itemCount, 5)
        ) { index ->
            pagingProducts[index]?.let { product ->
                if (product.images.isNotEmpty() && product.name != null) {
                    ProductCard(
                        product = product,
                        modifier = Modifier
                            .width(200.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                            .clickable {
                                navController.navigate("product/${product.id}")
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedHighlightText(
    phrases: List<String>,
    modifier: Modifier = Modifier,
    highlightColor: Color = Color(0xFFFFCC00), // Yellow highlight
    normalColor: Color = Color(0xFF1E1E1E),    // Dark Gray normal text
    wordHighlightDuration: Long = 500,        // Duration each word is highlighted (ms)
    phraseDisplayDuration: Long = 3000        // Total duration each phrase is displayed (ms)
) {
    // State to keep track of the current phrase index
    var currentPhraseIndex by remember { mutableStateOf(0) }
    // State to keep track of the current word index (-1 means no word is highlighted)
    var currentWordIndex by remember { mutableStateOf(-1) }

    val currentPhrase = phrases[currentPhraseIndex]
    val words = currentPhrase.split(" ")

    LaunchedEffect(currentPhraseIndex) {
        // Highlight each word in the current phrase sequentially
        for (i in words.indices) {
            currentWordIndex = i
            delay(wordHighlightDuration)
        }
        // Reset highlight
        currentWordIndex = -1
        // Wait for the remaining time before switching to the next phrase
        delay(phraseDisplayDuration - wordHighlightDuration * words.size)
        // Move to the next phrase in the list
        currentPhraseIndex = (currentPhraseIndex + 1) % phrases.size
    }

    Row(modifier = modifier) {
        words.forEachIndexed { index, word ->
            // Animate the color change for highlighting
            val color by animateColorAsState(
                targetValue = if (index == currentWordIndex) highlightColor else normalColor,
                animationSpec = tween(durationMillis = 300) // Fade duration
            )
            Text(
                text = word,
                color = color,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.width(4.dp)) // Space between words
        }
    }
}


@Composable
fun MarketSection(
    markets: List<Market>,
    navController: NavController,
    geocodingViewModel: GeocodingViewModel = hiltViewModel()
) {
    val updatedMarkets = remember(markets) { mutableStateOf(markets) }
    val isLoading = remember { mutableStateOf(true) }
    val apiKey = BuildConfig.MAPS_API_KEY
    val remainingRequests = remember { mutableStateOf(markets.size) }

    Log.d("MarketSection", "Initial markets size: ${markets.size}")

    LaunchedEffect(markets) {
        Log.d("MarketSection", "LaunchedEffect triggered. Markets size: ${markets.size}")
        isLoading.value = true

        if (markets.isEmpty()) {
            Log.d("MarketSection", "No markets to process, setting loading to false")
            isLoading.value = false
            return@LaunchedEffect
        }

        markets.forEach { market ->
            Log.d("MarketValidation", "Processing market - ID: ${market.id}, Latitude: ${market.latitude}, Longitude: ${market.longitude}")
            val latitude = market.latitude?.toDoubleOrNull()
            val longitude = market.longitude?.toDoubleOrNull()

            if (latitude != null && longitude != null && latitude in -90.0..90.0 && longitude in -180.0..180.0) {
                geocodingViewModel.getSubDistrictFromCoordinates(longitude, latitude, apiKey) { subDistrict ->
                    Log.d("GeocodingResult", "Received subDistrict for Market ID ${market.id}: $subDistrict")

                    val currentMarkets = updatedMarkets.value.toMutableList()
                    val updatedIndex = currentMarkets.indexOfFirst { it.id == market.id }

                    if (updatedIndex != -1) {
                        val updatedMarket = currentMarkets[updatedIndex].copy(
                            marketLocation = subDistrict ?: "No Location"
                        )
                        currentMarkets[updatedIndex] = updatedMarket
                        updatedMarkets.value = currentMarkets

                        Log.d("MarketSection", "Updated market ${market.id} location to: ${subDistrict}")
                    }

                    remainingRequests.value -= 1
                    Log.d("MarketSection", "Remaining requests: ${remainingRequests.value}")

                    if (remainingRequests.value <= 0) {
                        Log.d("MarketSection", "All requests completed, setting loading to false")
                        isLoading.value = false
                    }
                }
            } else {
                Log.e("MarketSection", "Invalid coordinates for market ${market.id}")
                remainingRequests.value -= 1
                if (remainingRequests.value <= 0) {
                    isLoading.value = false
                }
            }
        }
    }

    if (isLoading.value) {
        SkeletonLoadingView()
    } else {
        if (updatedMarkets.value.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No markets available")
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(
                    items = updatedMarkets.value.take(5),
                    key = { it.id }
                ) { market ->
                    Log.d("MarketSection", "Rendering market card - ID: ${market.id}, Name: ${market.name}, Location: ${market.marketLocation}")
                    Log.d("MarketCardImage", "Profile image URL: ${market.profileImageUrl}")
                    MarketCard(
                        imageRes = market.profileImageUrl ?: "https://via.placeholder.com/150",
                        marketName = market.name ?: "Unknown Name",
                        marketLocation = market.marketLocation ?: "No Location",
                        marketDescription = market.description ?: "No description available",
                        modifier = Modifier
                            .height(250.dp)
                            .width(200.dp)
                            .padding(end = 10.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .clickable {
                                navController.navigate("market/${market.id}")
                            }

                    )
                }
            }
        }
    }
}

@Composable
fun SkeletonLoadingView() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(5) {
            SkeletonCard()
        }
    }
}

@Composable
fun SkeletonCard() {
    Box(
        modifier = Modifier
            .width(200.dp)
            .padding(end = 10.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
                    .shimmering()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth(0.6f)
                    .background(Color.LightGray)
                    .shimmering()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth(0.4f)
                    .background(Color.LightGray)
                    .shimmering()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .height(12.dp)
                    .fillMaxWidth(0.8f)
                    .background(Color.LightGray)
                    .shimmering()
            )
        }
    }
}

@Composable
fun Modifier.shimmering(): Modifier {
    return this.then(Modifier.placeholder(visible = true, highlight = PlaceholderHighlight.shimmer()))
}


@Composable
fun ScanHistorySection(
    scanHistoryViewModel: ScanHistoryViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel = hiltViewModel()
) {
    val histories by scanHistoryViewModel.scanHistory.collectAsState()
    val isLoading by scanHistoryViewModel.isLoading.collectAsState()
    val error by scanHistoryViewModel.error.collectAsState()
    val token by tokenViewModel.token.observeAsState()

    LaunchedEffect(token) {
        scanHistoryViewModel.fetchScanHistory(token)
    }


    when {
        isLoading -> {
            LazyRow(contentPadding = PaddingValues(horizontal = 4.dp)) {
                items(3) {
                    ShimmerScanHistoryCard(
                        modifier = Modifier
                            .width(350.dp)
                            .padding(end = 10.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
                    )
                }
            }
        }
        error != null -> Text(text = error ?: "Unknown error occurred")
        else -> {
            LazyRow(contentPadding = PaddingValues(horizontal = 4.dp)) {
                items(histories) { history ->
                    ScanHistoryCard(
                        imageUrl = history.image_url,
                        title = history.predict,
                        description = history.category_description,
                        price = history.category_range_price,
                        modifier = Modifier
                            .width(350.dp)
                            .padding(end = 10.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
                    )
                }
            }
        }
    }
}




@Composable
fun StickyTopBar(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .shadow(1.dp)
    ) {
        TopBar(geocodingViewModel = GeocodingViewModel(), navController)

    }
}

@Composable
fun TopBar(
    geocodingViewModel: GeocodingViewModel = hiltViewModel(),
    navController: NavController,
    notificationViewModel: NotificationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var subDistrict by remember { mutableStateOf<String?>(null) }
    var district by remember { mutableStateOf<String?>(null) }

    val unreadCount by notificationViewModel.unreadCount.observeAsState(0)
    val notificationIcon = if (unreadCount > 0) R.drawable.notification_top_new else R.drawable.notification_top


    LaunchedEffect(Unit) {
        geocodingViewModel.getCurrentLocationAndGetSubDistrict(
            context = context,
            apiKey = BuildConfig.MAPS_API_KEY,
            onResult = { result ->
                subDistrict = result
                district = result
            }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = notificationIcon),
            contentDescription = "Notification Icon",
            modifier = Modifier
                .size(32.dp)
                .clickable { navController.navigate("notification") }
        )


        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Your Location",
                    fontFamily = sfui_text,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = if (subDistrict != null && district != null) "$district ▼" else "Getting location...",
                    fontFamily = sfui_semibold,
                    color = Color(0xFF2D1403),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .clickable { navController.navigate("myLocation") }
                )
            }

            Image(
                painter = painterResource(id = R.drawable.favorite_top),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        navController.navigate("favorite")
                    }
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
            title = "Cart",
            iconActive = R.drawable.ic_carts_selected,
            iconInactive = R.drawable.ic_carts,
            route = "carts"
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
        } else {
            scope.launch {
                navController.navigate("camera")
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
                .offset(y = (-100).dp)
                .align(Alignment.BottomEnd)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        isExtended = false
                        if (PermissionUtils.hasRequiredPermissions(context)) {
                            scope.launch {
                                navController.navigate("camera?previousScreen=home")
                            }
                        } else {
                            permissionLauncher.launch(PermissionUtils.getRequiredPermissions())
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
                            permissionLauncher.launch(PermissionUtils.getRequiredPermissions())
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



@Composable
fun ProductDetailSkeleton() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(bottom = 80.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .shimmering()
                )
                Box(
                    modifier = Modifier
                        .width(150.dp)
                        .height(30.dp)
                        .shimmering()
                )
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmering()
            )
        }

        item {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(24.dp)
                        .shimmering()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .shimmering()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .shimmering()
                )
            }
        }

        // Details section
        item {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(24.dp)
                        .shimmering()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(20.dp)
                        .shimmering()
                )
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .shimmering()
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .height(24.dp)
                            .shimmering()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(20.dp)
                            .shimmering()
                    )
                }
            }
        }
    }
}

@Composable
fun ShimmerScanHistoryCard(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
            .shimmering()
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(20.dp)
                    .background(Color.LightGray, RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color.LightGray, RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(24.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    Home(navController = NavController(LocalContext.current))
}


