
package com.capstone.surevenir

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.capstone.surevenir.ui.splash.Home
import com.capstone.surevenir.ui.splash.OnBoardingScreen
import com.capstone.surevenir.ui.splash.SignInScreen
import com.capstone.surevenir.ui.splash.SignUpScreen
import com.capstone.surevenir.ui.splash.SplashScreen
import com.capstone.surevenir.ui.theme.MyAppTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavController

    companion object {
        private const val GOOGLE_SIGN_IN_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                ) {
                  MainScreen()
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
                firebaseAuthWithGoogle(account.idToken!!, navController)
            } catch (e: ApiException) {
                println("Google Sign-In failed: ${e.statusCode}")
                println("Error message: ${e.message}")
                e.printStackTrace()
            }
        }
    }

}



private fun firebaseAuthWithGoogle(idToken: String, navController: NavController) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    FirebaseAuth.getInstance().signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("Sign-In Successful")
                navController.navigate("home")
            } else {
                println("Sign-In Failed: ${task.exception?.message}")
            }
        }
}


@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen {
                navController.navigate("onboarding") {
                    popUpTo("splash") { inclusive = true }
                }
            }
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
        composable("signUp") { SignUpScreen(navController = navController) }
    }

}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyAppTheme {
    }
}
