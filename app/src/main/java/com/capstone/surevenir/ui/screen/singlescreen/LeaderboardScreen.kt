package com.capstone.surevenir.ui.screen.singlescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.capstone.surevenir.R
import com.capstone.surevenir.data.network.response.LeaderboardUser
import com.capstone.surevenir.helper.UserPreferences
import com.capstone.surevenir.ui.screen.allscreen.ShopSectionAll
import com.capstone.surevenir.ui.screen.navmenu.sfui_med
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.ui.screen.navmenu.sfui_text
import com.capstone.surevenir.ui.viewmodel.LeaderboardViewModel
import com.capstone.surevenir.ui.viewmodel.TokenViewModel

@Composable
fun LeaderboardScreen(navController: NavHostController, tokenViewModel : TokenViewModel = hiltViewModel()) {
    val token by tokenViewModel.token.observeAsState()

    LaunchedEffect(Unit) {
        tokenViewModel.fetchToken()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFE7E7E9), CircleShape)
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
                text = "Leaderboard",
                fontSize = 25.sp,
                fontFamily = sfui_semibold,
                color = Color(0xFFCC5B14)
            )
        }

        SectionLeaderboards()
    }
}

@Composable
fun SectionLeaderboards(
    modifier: Modifier = Modifier,
    viewModel: LeaderboardViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val leaderboardUsers by viewModel.leaderboardState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val token by tokenViewModel.token.observeAsState()
    val userEmail by userPreferences.userEmail.collectAsState(initial = null)

    LaunchedEffect(token) {
        token?.let { viewModel.fetchLeaderboard(it) }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFFCC5B14))
        }
    } else if (error != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Error: $error")
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            itemsIndexed(leaderboardUsers) { index, user ->
                val isCurrentUser = user.email == userEmail

                LeaderboardItem(
                    rank = index + 1,
                    user = user,
                    isCurrentUser = isCurrentUser
                )

                if (index < leaderboardUsers.lastIndex) {
                    Divider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color(0xFFE7E7E9)
                    )
                }
            }
        }
    }
}


@Composable
fun LeaderboardItem(
    rank: Int,
    user: LeaderboardUser,
    isCurrentUser: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "#$rank",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = if (isCurrentUser) 18.sp else 16.sp,
                    fontWeight = if (isCurrentUser) FontWeight.Bold else FontWeight.Normal,
                            fontFamily = sfui_semibold,

                ),
                color = Color(0xFF1E1E1E)
            )

            AsyncImage(
                model = if (user.profile_image_url != "Unknown") {
                    user.profile_image_url
                } else {
                    R.drawable.ic_profile
                },
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Text(
                text = user.user_name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = if (isCurrentUser) 18.sp else 16.sp,
                    fontWeight = if (isCurrentUser) FontWeight.Bold else FontWeight.Normal
                ),
                fontFamily = sfui_text,
                color = Color(0xFF1E1E1E)
            )
        }

        Text(
            text = "${user.points} pts",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = if (isCurrentUser) 18.sp else 16.sp,
                fontWeight = if (isCurrentUser) FontWeight.Bold else FontWeight.Normal,
                fontFamily = sfui_semibold
            ),
            color = Color(0xFFCC5B14),
            fontFamily = sfui_med
        )
    }
}