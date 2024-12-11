package com.capstone.surevenir.ui.screen.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.capstone.surevenir.R
import com.capstone.surevenir.helper.UserPreferences
import com.capstone.surevenir.model.OnboardingPage
import com.capstone.surevenir.ui.screen.navmenu.sfui_med
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold
import com.capstone.surevenir.ui.screen.navmenu.sfui_text
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(
    navController: NavController,
    userPreferences: UserPreferences
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val pages = listOf(
        OnboardingPage(
            imageRes = R.drawable.onboard_1,
            title = "Discover Your ",
            description = "Use your phone's camera to instantly detect and recognize Bali souvenirs. Access rich cultural insights on each item with just one click!",
            highlightText = "Bali Souvenir!",
            belowText = "Unforgettable Experience"
        ),
        OnboardingPage(
            imageRes = R.drawable.onboard_2,
            title = "Explore Culture ",
            description = "Every souvenir has a story! Uncover the history, meaning, and symbolism behind each crafted piece you come across.",
            highlightText = "in Every Souvenir",
            belowText = "More Than Just an Object"
        ),
        OnboardingPage(
            imageRes = R.drawable.onboard_3,
            title = "Get the Best ",
            description = "Find the top souvenirs with our expert recommendations and shop with confidence. Enjoy a deeper connection to Bali.",
            highlightText = "Recommendations",
            belowText = "Quick and Easy"
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            HorizontalPager(
                count = pages.size,
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                OnboardingPageScreen(page = pages[page])
            }
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            activeColor = Color(0xFFCC5B14),
            inactiveColor = Color(0xFFED8A00)
        )
        Button(
            onClick = {
                if (pagerState.currentPage == pages.size - 1) {
                    scope.launch(Dispatchers.Main) {
                        userPreferences.saveOnboardingState(true)
                        navController.navigate("signIn") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                } else {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (pagerState.currentPage == pages.size - 1) Color(0xFFED8A00) else Color.Gray
            ),
            enabled = pagerState.currentPage == pages.size - 1 || pagerState.currentPage < pages.size - 1
        ) {
            Text(
                text = if (pagerState.currentPage == pages.size - 1) "Get Started" else "Next",
                color = if (pagerState.currentPage == pages.size - 1) Color.White else Color.DarkGray
            )
        }
    }
}

@Composable
fun OnboardingPageScreen(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(20.dp))

        Text(
            text = buildAnnotatedString {
                append(page.title)
                withStyle(style = SpanStyle(Color(0xFFFFA726))) {
                    append(" ${page.highlightText}")
                }
            },
            fontFamily = sfui_semibold,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black,
            fontSize = 20.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(20.dp))

        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f)
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = page.belowText,
            fontFamily = sfui_med,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Black,
            fontSize = 18.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            fontFamily = sfui_text,
            text = page.description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            lineHeight = 24.sp,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}




