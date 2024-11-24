
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.capstone.surevenir.helper.UserPreferences
import com.capstone.surevenir.ui.camera.ImageCaptureVM
import com.capstone.surevenir.ui.camera.PreviewScreen
import com.capstone.surevenir.ui.splash.AllCategoryScreen
import com.capstone.surevenir.ui.splash.AllHistory
import com.capstone.surevenir.ui.splash.AllProductScreen
import com.capstone.surevenir.ui.splash.AllShopScreen
import com.capstone.surevenir.ui.splash.BottomNavigationBar
import com.capstone.surevenir.ui.splash.FavoritesScreen
import com.capstone.surevenir.ui.splash.FloatingButtonWithIntent
import com.capstone.surevenir.ui.splash.ForgotPassword
import com.capstone.surevenir.ui.splash.Home
import com.capstone.surevenir.ui.splash.OnBoardingScreen
import com.capstone.surevenir.ui.splash.ProfileScreen
import com.capstone.surevenir.ui.splash.ResultScreen
import com.capstone.surevenir.ui.splash.ScanScreen
import com.capstone.surevenir.ui.splash.ShopScreen
import com.capstone.surevenir.ui.splash.SignInScreen
import com.capstone.surevenir.ui.splash.SignUpScreen
import com.capstone.surevenir.ui.splash.SingleProductScreen
import com.capstone.surevenir.ui.splash.SplashScreen
import com.capstone.surevenir.ui.splash.StickyTopBar
import com.capstone.surevenir.ui.splash.profile.AccountCenterScreen
import com.capstone.surevenir.ui.splash.profile.EditProfileScreen
import com.capstone.surevenir.ui.splash.profile.SettingsScreen
import com.capstone.surevenir.ui.theme.MyAppTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

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
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                println("Google Sign-In Success: ${account.displayName}")
                firebaseAuthWithGoogle(account.idToken!!, navController, this)
            } catch (e: ApiException) {
                println("Google Sign-In failed: ${e.statusCode}")
                println("Error message: ${e.message}")
                e.printStackTrace()
            }
        }
    }

}



private fun firebaseAuthWithGoogle(idToken: String, navController: NavHostController, context: Context) {


    val credential = GoogleAuthProvider.getCredential(idToken, null)
    FirebaseAuth.getInstance().signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result?.user
                val token = user?.uid

                val userPreferences = UserPreferences(context)
                (context as ComponentActivity).lifecycleScope.launch {
                    userPreferences.saveLoginState(isLoggedIn = true, token = token ?: "", email = user?.email ?: "")
                }

                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            } else {
                println("Sign-In Failed: ${task.exception?.message}")
                task.exception?.printStackTrace()
            }
        }
}


@Composable
fun MainScreen(navController: NavHostController) {
    // Mendapatkan rute saat ini
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
            // Only show FAB on main screens
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
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyAppTheme {
    }
}
