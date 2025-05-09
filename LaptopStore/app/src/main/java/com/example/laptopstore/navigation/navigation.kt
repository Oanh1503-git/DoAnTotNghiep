package com.example.laptopstore.navigation

import AccountScreens
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.laptopstore.views.HOMEPAGE
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.laptopstore.models.Screens
import com.example.laptopstore.views.Categories
import com.example.laptopstore.views.ProductDetail
import com.example.laptopstore.views.CartScreen
import com.example.laptopstore.views.CheckoutScreen

@Composable
fun NavigationGraph(navHostController: NavHostController) {
    NavHost(navController = navHostController,
        startDestination = Screens.HOMEPAGE.route) {
        composable(Screens.HOMEPAGE.route) {
            HOMEPAGE(navHostController)
        }
        composable(Screens.CATAGORIES.route) {
            Categories(navHostController)
        }
        composable(Screens.CARTSCREENS.route) {
            CartScreen(navHostController)
        }
        composable(Screens.ACCOUNTSCREENS.route) {
            AccountScreens(navHostController)
        }
        composable("product_detail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toInt() ?: 0
            ProductDetail(navController = navHostController, productId = productId)
        }
        composable("checkout/{totalPrice}/{cartItems}") { backStackEntry ->
            val totalPrice = backStackEntry.arguments?.getString("totalPrice")?.toInt() ?: 0
            val cartItemsJson = backStackEntry.arguments?.getString("cartItems") ?: ""
            CheckoutScreen(navController = navHostController, totalPrice = totalPrice, cartItemsJson = cartItemsJson)
        }
    }
}