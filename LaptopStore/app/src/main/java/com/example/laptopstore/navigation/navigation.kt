package com.example.laptopstore.navigation

import AccountScreens
import CartScreen
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import com.example.app_e_commerce.views.HOMEPAGE
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.laptopstore.models.Screens


@Composable
fun NavigationGraph(navHostController: NavHostController){
    NavHost(navController = navHostController,
        startDestination = Screens.HOMEPAGE.route) {
        composable(Screens.HOMEPAGE.route) {
            HOMEPAGE(navHostController)
        }
//        composable(Screens.CATAGORIES.route) {
//            Categories(navHostController)
//        }
        composable(Screens.CARTSCREENS.route) {
            CartScreen(navHostController)
        }
        composable(Screens.ACCOUNTSCREENS.route) {
            AccountScreens(navHostController)
        }
    }

}