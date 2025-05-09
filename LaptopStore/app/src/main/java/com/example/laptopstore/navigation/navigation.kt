package com.example.laptopstore.navigation

import AccountScreens
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.laptopstore.views.HOMEPAGE
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.laptopstore.models.Screens
import com.example.laptopstore.views.Categories



@Composable
fun NavigationGraph(navHostController: NavHostController){
    NavHost(navController = navHostController,
        startDestination = Screens.HOMEPAGE.route) {
        composable(Screens.HOMEPAGE.route) {
            HOMEPAGE(navHostController)
        }
        composable(Screens.CATAGORIES.route) {
            Categories(navHostController)
        }
//        composable(Screens.CARTSCREENS.route) {
//            CartScreen(navHostController)
//        }
        composable(Screens.ACCOUNTSCREENS.route) {
            AccountScreens(navHostController)
        }
    }

}