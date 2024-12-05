
package com.capstone.surevenir

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.capstone.surevenir.data.network.response.CreateUserRequest
import com.capstone.surevenir.helper.UserPreferences
import com.capstone.surevenir.ui.camera.CameraScreen
import com.capstone.surevenir.ui.camera.ImageCaptureVM
import com.capstone.surevenir.ui.camera.PreviewScreen
import com.capstone.surevenir.ui.screen.allscreen.AllCategoryScreen
import com.capstone.surevenir.ui.screen.allscreen.AllHistory
import com.capstone.surevenir.ui.screen.allscreen.AllProductScreen
import com.capstone.surevenir.ui.screen.allscreen.AllShopScreen
import com.capstone.surevenir.ui.screen.navmenu.BottomNavigationBar
import com.capstone.surevenir.ui.screen.navmenu.TransactionScreen
import com.capstone.surevenir.ui.screen.navmenu.FloatingButtonWithIntent
import com.capstone.surevenir.ui.screen.forgotpassword.ForgotPassword
import com.capstone.surevenir.ui.screen.navmenu.Home
import com.capstone.surevenir.ui.screen.onboarding.OnBoardingScreen
import com.capstone.surevenir.ui.screen.navmenu.ProfileScreen
import com.capstone.surevenir.ui.screen.ResultScreen
import com.capstone.surevenir.ui.screen.allscreen.AllMarket
import com.capstone.surevenir.ui.screen.mylocation.MyLocationScreen
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.UUID

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    private lateinit var fusedLocationClient: FusedLocationProviderClient

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

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
            val email = account.email
            val displayName = account.displayName
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            val firebaseUid = firebaseUser?.uid ?: UUID.randomUUID().toString()

            val dummyToken = "b1025941-5a1d-4d86-bef0-d05e9118befb"

            Log.d("DEBUG", "Using Dummy Token: $dummyToken")

            userViewModel.getUsers(dummyToken) { users ->
                val existingUser = users?.find { it.email == email }
                Log.d("DEBUG", "Fetched Users: $users")
                Log.d("DEBUG", "Existing User: $existingUser")

                val userPreferences = UserPreferences(context)
                (context as ComponentActivity).lifecycleScope.launch {
                    if (existingUser != null) {
                        userPreferences.saveLoginState(
                            isLoggedIn = true,
                            token = existingUser.id,
                            email = email ?: ""
                        )
                        Log.d("DEBUG", "Existing user found. ID: ${existingUser.id}")
                    } else {
                        val request = CreateUserRequest(
                            id = firebaseUid,
                            fullName = displayName ?: "Unknown Name",
                            username = displayName ?: "Unknown Username",
                            email = email ?: "unknown_email@gmail.com",
                            password = "google_login",
                            phone = "",
                            role = "USER",
                            provider = "GOOGLE",
                            longitude = "",
                            latitude = "",
                            address = ""
                        )
                        userViewModel.createUser(request)

                        userPreferences.saveLoginState(
                            isLoggedIn = true,
                            token = firebaseUid,
                            email = email ?: ""
                        )
                        Log.d("DEBUG", "New user created with ID: $firebaseUid")
                    }

                    navController.navigate("home") {
                        popUpTo("signIn") { inclusive = true }
                    }
                }
            }
        }
    } catch (e: ApiException) {
        e.printStackTrace()
        Log.e("DEBUG", "Google Sign-In failed: ${e.message}")
    }
}




@Composable
fun MainScreen(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val imageCaptureViewModel: ImageCaptureVM = viewModel()
    val context = LocalContext.current
    val cameraExecutor = remember { ContextCompat.getMainExecutor(context) }

    Scaffold(
        topBar = {
            if (currentRoute in listOf("home", "shop", "scan", "transaction", "profile", "myLocation")) {
                StickyTopBar(navController)
            }
        },
        bottomBar = {
            if (currentRoute in listOf("home", "shop", "scan", "transaction", "profile")) {
                BottomNavigationBar(navController)
            }
        },
        floatingActionButton = {
            if (currentRoute in listOf("home", "shop", "scan", "transaction", "profile")) {
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
            composable("myLocation") {
                MyLocationScreen(navController)
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
            composable("transaction") {
                TransactionScreen(navController)
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
            composable("allMarket") {
                AllMarket(navController)
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
            composable("singleCategory") {
                SingleCategoryScreen(navController)
            }
            composable(
                "product/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getInt("productId") ?: 0
                SingleProductScreen(productId, navController)
            }
            composable(
                "merchant/{merchantId}",
                arguments = listOf(navArgument("merchantId") { type = NavType.IntType })
            ) { backStackEntry ->
                val merchantId = backStackEntry.arguments?.getInt("merchantId") ?: 0
                SingleShopScreen(merchantId, navController)
            }

            composable("camera") {
                CameraScreen(
                    navController = navController,
                    imageCaptureViewModel = imageCaptureViewModel,
                    executor = cameraExecutor
                )
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
