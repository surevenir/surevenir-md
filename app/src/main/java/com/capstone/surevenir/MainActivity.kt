
package com.capstone.surevenir

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.capstone.surevenir.data.network.response.CreateUserRequest
import com.capstone.surevenir.helper.UserPreferences
import com.capstone.surevenir.ui.camera.ImageCaptureVM
import com.capstone.surevenir.ui.camera.PreviewScreen
import com.capstone.surevenir.ui.screen.allscreen.AllCategoryScreen
import com.capstone.surevenir.ui.screen.allscreen.AllHistory
import com.capstone.surevenir.ui.screen.allscreen.AllProductScreen
import com.capstone.surevenir.ui.screen.allscreen.AllShopScreen
import com.capstone.surevenir.ui.screen.navmenu.BottomNavigationBar
import com.capstone.surevenir.ui.screen.navmenu.FavoritesScreen
import com.capstone.surevenir.ui.screen.navmenu.FloatingButtonWithIntent
import com.capstone.surevenir.ui.screen.forgotpassword.ForgotPassword
import com.capstone.surevenir.ui.screen.navmenu.Home
import com.capstone.surevenir.ui.screen.onboarding.OnBoardingScreen
import com.capstone.surevenir.ui.screen.navmenu.ProfileScreen
import com.capstone.surevenir.ui.screen.ResultScreen
import com.capstone.surevenir.ui.screen.navmenu.ScanScreen
import com.capstone.surevenir.ui.screen.navmenu.ShopScreen
import com.capstone.surevenir.ui.screen.navmenu.SignInScreen
import com.capstone.surevenir.ui.screen.signup.SignUpScreen
import com.capstone.surevenir.ui.screen.singlescreen.SingleCategoryScreen
import com.capstone.surevenir.ui.screen.singlescreen.SingleProductScreen
import com.capstone.surevenir.ui.screen.singlescreen.SingleShopScreen
import com.capstone.surevenir.ui.screen.splash.SplashScreen
import com.capstone.surevenir.ui.screen.navmenu.StickyTopBar
import com.capstone.surevenir.ui.screen.profile.AccountCenterScreen
import com.capstone.surevenir.ui.screen.profile.EditProfileScreen
import com.capstone.surevenir.ui.screen.profile.SettingsScreen
import com.capstone.surevenir.ui.theme.MyAppTheme
import com.capstone.surevenir.ui.viewmodel.UserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.UUID

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    companion object {
        private const val GOOGLE_SIGN_IN_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    navController = rememberNavController()
                    MainScreen(navController)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN_CODE) {
            try {
                val userViewModel: UserViewModel = ViewModelProvider(this)[UserViewModel::class.java]
                handleGoogleSignInResult(data, this, navController, userViewModel)
            } catch (e: ApiException) {
                println("Google Sign-In failed: ${e.statusCode}")
                println("Error message: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}


private fun handleGoogleSignInResult(
    data: Intent?,
    context: Context,
    navController: NavController,
    userViewModel: UserViewModel
) {
    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
    try {
        val account = task.getResult(ApiException::class.java)
        if (account != null) {
            val idToken = account.idToken
            val email = account.email
            val displayName = account.displayName

            if (!email.isNullOrEmpty()) {
                userViewModel.getUsers { users ->
                    val existingUser = users?.find { it.email == email }
                    if (existingUser == null) {
                        val firebaseUser = FirebaseAuth.getInstance().currentUser
                        val firebaseUid = firebaseUser?.uid ?: UUID.randomUUID().toString()

                        val request = CreateUserRequest(
                            id = firebaseUid,
                            fullName = displayName ?: "Unknown Name",
                            username = displayName ?: "Unknown Username",
                            email = email,
                            password = "",
                            phone = "1234567891012",
                            role = "USER",
                            provider = "GOOGLE",
                            longitude = "",
                            latitude = "",
                            address = ""
                        )
                        userViewModel.createUser(request)
                    } else {
                        navController.navigate("home") {
                            popUpTo("signIn") { inclusive = true }
                        }
                    }
                }
            }
        }
    } catch (e: ApiException) {
        e.printStackTrace()
    }
}


@Composable
fun MainScreen(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val imageCaptureViewModel: ImageCaptureVM = viewModel()

    Scaffold(
        topBar = {
            if (currentRoute in listOf("home", "shop", "scan", "favorites", "profile")) {
                StickyTopBar()
            }
        },
        bottomBar = {
            if (currentRoute in listOf("home", "shop", "scan", "favorites", "profile")) {
                BottomNavigationBar(navController)
            }
        },
        floatingActionButton = {
            if (currentRoute in listOf("home", "shop", "scan", "favorites", "profile")) {
                FloatingButtonWithIntent(
                    navController = navController,
                    imageCaptureViewModel = imageCaptureViewModel
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true

    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(padding)
        ) {
            composable("splash") {
                SplashScreen(
                    navigateToHome = { navController.navigate("home") { popUpTo("splash") { inclusive = true } } },
                    navigateToSignIn = { navController.navigate("signIn") { popUpTo("splash") { inclusive = true } } }
                )
            }
            composable("onboarding") {
                OnBoardingScreen(navController = navController)
            }
            composable("home") {
                Home(navController = navController)
            }
            composable("signIn") {
                SignInScreen(navController = navController)
            }
            composable("forgotPassword") {
                ForgotPassword(navController = navController)
            }
            composable("signUp") {
                SignUpScreen(navController = navController)
            }
            composable("shop") {
                ShopScreen(navController)
            }
            composable("scan") {
                ScanScreen(navController)
            }
            composable("favorites") {
                FavoritesScreen(navController)
            }
            composable("profile") {
                ProfileScreen(navController)
            }
            composable("allShop") {
                AllShopScreen(navController)
            }
            composable("allProduct") {
                AllProductScreen(navController)
            }
            composable("allCategory") {
                AllCategoryScreen(navController)
            }
            composable("allHistory") {
                AllHistory(navController)
            }
            composable("singleProduct") {
                SingleProductScreen(navController)
            }
            composable("preview") {
                PreviewScreen(
                    navController = navController,
                    imageCaptureViewModel = imageCaptureViewModel
                )
            }
            composable("result") {
                ResultScreen(
                    navController = navController,
                    imageCaptureViewModel = imageCaptureViewModel
                )
            }
            composable("accountCenter") {
                AccountCenterScreen(navController)
            }

            composable("editProfile") {
                EditProfileScreen(navController)
            }

            composable("settings") {
                SettingsScreen(navController)
            }
            composable("singleProduct") {
                SingleProductScreen(navController)
            }
            composable("singleShop") {
                SingleShopScreen(navController)
            }
            composable("singleCategory") {
                SingleCategoryScreen(navController)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyAppTheme {
    }
}
