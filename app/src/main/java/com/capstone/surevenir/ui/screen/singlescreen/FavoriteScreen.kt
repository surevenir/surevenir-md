package com.capstone.surevenir.ui.screen.singlescreen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.capstone.surevenir.ui.viewmodel.FavoriteViewModel
import com.capstone.surevenir.ui.viewmodel.TokenViewModel

@Composable
fun FavoriteScreen(
    navController: NavHostController,
    tokenViewModel: TokenViewModel = hiltViewModel(),
    favoriteViewModel: FavoriteViewModel = hiltViewModel()
) {



}
