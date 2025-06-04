package com.example.laptopstore.navigation

import AccountScreens
import LoginScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import com.example.laptopstore.views.HOMEPAGE
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.laptopstore.models.Screens
import com.example.laptopstore.viewmodels.HinhAnhViewModel
import com.example.laptopstore.viewmodels.KhachHangViewModels
import com.example.laptopstore.viewmodels.SanPhamViewModel
import com.example.laptopstore.viewmodels.TaiKhoanViewModel
import com.example.lapstore.viewmodels.DiaChiViewmodel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laptopstore.AddressScreens
import com.example.laptopstore.viewmodels.GioHangViewModel
import com.example.laptopstore.views.*

@Composable
fun NavigationGraph(
    navHostController: NavHostController,
    sanphamViewModel: SanPhamViewModel,
    hinhAnhViewModel: HinhAnhViewModel,
    khachHangViewModel: KhachHangViewModels,
    taiKhoanViewModel: TaiKhoanViewModel,
    diaChiViewModel: DiaChiViewmodel = viewModel(),
    gioHangViewModel: GioHangViewModel
) {
    NavHost(
        navController = navHostController,
        startDestination = Screens.HOMEPAGE.route
    ) {
        composable(Screens.HOMEPAGE.route) {
            HOMEPAGE(navHostController)
        }

        composable(Screens.ACCOUNTSCREENS.route) {backStackEntry->
            val savedStateHandle = backStackEntry.savedStateHandle
            AccountScreens(navHostController,
                taiKhoanViewModel,
                khachHangViewModel,
                savedStateHandle = backStackEntry.savedStateHandle)
        }

        composable(
            "homepage?searchQuery={searchQuery}&brand={brand}&price={price}&usage={usage}&chip={chip}&screen={screen}",
            arguments = listOf(
                navArgument("searchQuery") { defaultValue = "" },
                navArgument("brand") { defaultValue = "" },
                navArgument("price") { defaultValue = "" },
                navArgument("usage") { defaultValue = "" },
                navArgument("chip") { defaultValue = "" },
                navArgument("screen") { defaultValue = "" }
            )
        ) { backStackEntry ->
            HOMEPAGE(navHostController)
        }

        composable(Screens.CATAGORIES.route) {
            Categories(navHostController)
        }

        composable(Screens.CARTSCREENS.route) {backStackEntry->
            val savedStateHandle = backStackEntry.savedStateHandle
            CartScreen(navHostController,gioHangViewModel,sanphamViewModel,taiKhoanViewModel,
                savedStateHandle = backStackEntry.savedStateHandle )
        }

        composable(
            route = "product_detail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            ProductDetail(navController = navHostController, productId = productId)
        }

        composable(Screens.Login_Screens.route) {
            LoginScreen(navHostController)
        }

        composable(Screens.REGISTERSCREEN.route) {
            Register_Screen(
                navController = navHostController,
                taiKhoanViewModel = taiKhoanViewModel,
                khachHangViewModel = khachHangViewModel
            )
        }

        composable(
            route = Screens.ACCOUNTDETAIL.route,
            arguments = listOf(navArgument("maKhachHang") { defaultValue = "" })
        ) { backStackEntry ->
            val maKhachHang = backStackEntry.arguments?.getString("maKhachHang") ?: ""
            AccountDetails(
            navHostController,khachHangViewModel,maKhachHang
            )
        }

        composable(
            route = Screens.ADDRESS.route,
            arguments = listOf(
                navArgument("maKhachHang") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val maKhachHang = backStackEntry.arguments?.getString("maKhachHang") ?: return@composable
            AddressScreens(
                navHostController,diaChiViewModel,maKhachHang
            )
        }
    }
}