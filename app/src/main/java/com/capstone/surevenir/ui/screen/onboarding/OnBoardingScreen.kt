package com.capstone.surevenir.ui.screen.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.capstone.surevenir.R
import com.capstone.surevenir.helper.UserPreferences
import com.capstone.surevenir.model.OnboardingPage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(navController: NavController,
                     userPreferences: UserPreferences // Tambahkan parameter ini
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope() // Tambahkan ini untuk coroutine scope
    val pages = listOf(
        OnboardingPage(
            imageRes = R.drawable.onboard_1,
            title = "Discover Your " +
                    "",
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

    Column (modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
    ){
        Box(modifier = Modifier
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
        HorizontalPagerIndicator(pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            activeColor = Color(0xFFCC5B14),
            inactiveColor = Color(0xFFED8A00)
        )
        Button(
            onClick = {
                if (pagerState.currentPage == pages.size - 1) {
                    navController.navigate("signIn")
                    scope.launch {
                        userPreferences.saveOnboardingState(true)
                        navController.navigate("signIn") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                } else {
                }
            },            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFED8A00))
        ) {
            Text(text = if (pagerState.currentPage == pages.size  - 1) "Get Started" else "Get Started")
        }
        Spacer(modifier = Modifier.height(5.dp))
    }
}

@Composable
fun OnboardingPageScreen (page: OnboardingPage){
    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(Modifier.height(20.dp))
        Text(
            text = buildAnnotatedString {
                append(page.title)
                withStyle(style = SpanStyle(Color(0xFFFFA726))){
                    append("${page.highlightText}")
                }
            },
            fontFamily = geometrFont,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp)
        )

        Text(
            text = buildAnnotatedString {
                append(page.belowText)
            },
            fontFamily = geometrFont,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            fontFamily = gilisansFont,
            text = page.description,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
            lineHeight = 24.sp,
            fontSize = 19.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

    }
}

val geometrFont = FontFamily(
    Font(R.font.poppinssemibold, FontWeight.Bold)
)

val gilisansFont = FontFamily(Font(R.font.poppinssemibold, FontWeight.Normal))