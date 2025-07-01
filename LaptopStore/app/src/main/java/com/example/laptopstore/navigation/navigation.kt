package com.example.laptopstore.navigation

import AccountScreens
import LoginScreen
import ResetPasswordScreen
import android.util.Log
import androidx.compose.runtime.Composable
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
import com.example.lapstore.viewmodels.HoaDonViewModel
import com.example.laptopstore.AddressScreen
import com.example.laptopstore.screens.OrderStatusScreen
import com.example.laptopstore.viewmodels.ChiTietHoaDonViewmodel

import com.example.laptopstore.viewmodels.GioHangViewModel
import com.example.laptopstore.viewmodels.OTPViewModel
import com.example.laptopstore.viewmodels.SanPhamYeuThichViewModel
import com.example.laptopstore.views.*

@Composable
fun NavigationGraph(
    navHostController: NavHostController,
    sanphamViewModel: SanPhamViewModel,
    hinhAnhViewModel: HinhAnhViewModel,
    khachHangViewModel: KhachHangViewModels,
    taiKhoanViewModel: TaiKhoanViewModel,
    diaChiViewModel: DiaChiViewmodel = viewModel(),
    gioHangViewModel: GioHangViewModel,
    sanPhamYeuThichViewModel: SanPhamYeuThichViewModel = viewModel(),
    otpViewModel: OTPViewModel,
    hoaDonBanViewModel: HoaDonViewModel,
    chiTietHoaDonViewmodel: ChiTietHoaDonViewmodel
){
    NavHost(
        navController = navHostController,
        startDestination = Screens.HOMEPAGE.route
    ) {
        composable(Screens.HOMEPAGE.route) {
            HOMEPAGE(navHostController,sanphamViewModel,gioHangViewModel=gioHangViewModel)
        }

        composable(Screens.ACCOUNTSCREENS.route) {backStackEntry->
            val savedStateHandle = backStackEntry.savedStateHandle
            AccountScreens(navHostController,
                khachHangViewModel,
                savedStateHandle = backStackEntry.savedStateHandle)
        }

//        composable(
//            "homepage?searchQuery={searchQuery}&brand={brand}&price={price}&usage={usage}&chip={chip}&screen={screen}",
//            arguments = listOf(
//                navArgument("searchQuery") { defaultValue = "" },
//                navArgument("brand") { defaultValue = "" },
//                navArgument("price") { defaultValue = "" },
//                navArgument("usage") { defaultValue = "" },
//                navArgument("chip") { defaultValue = "" },
//                navArgument("screen") { defaultValue = "" }
//            )
//        ) { backStackEntry ->
//            HOMEPAGE(navHostController,sanphamViewModel,hinhAnhViewModel,gioHangViewModel)
//        }

        composable(Screens.CATAGORIES.route) {
            Categories(navHostController,sanphamViewModel,gioHangViewModel)
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
            ProductDetail(
                navController = navHostController,
                productId = productId,
                taiKhoanViewModel = taiKhoanViewModel,
                // bạn nên truyền thêm các ViewModel khác nếu ProductDetail cần
            )
        }

        composable(Screens.Login_Screens.route) {
            LoginScreen(navHostController)
        }

        composable(Screens.REGISTERSCREEN.route) {
            RegisterScreen(
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
            navHostController,khachHangViewModel,taiKhoanViewModel
            )
        }

        composable(
            route = "address?fromCheckout={fromCheckout}",
            arguments = listOf(
                navArgument("fromCheckout") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val fromCheckout = backStackEntry.arguments?.getBoolean("fromCheckout") ?: false
            AddressScreen(
                navHostController,
                diaChiViewModel,
                taiKhoanViewModel,
                fromCheckout = fromCheckout
            )
        }

        composable(
            route = Screens.CHECKOUTSCREENS.route,
            arguments = listOf(
                navArgument("totalPrice") { type = NavType.IntType },
                navArgument("cartItems") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val totalPrice = backStackEntry.arguments?.getInt("totalPrice") ?: 0
            val cartItems = backStackEntry.arguments?.getString("cartItems") ?: ""

            CheckoutScreen(
                navController = navHostController,
                totalPrice = totalPrice,
                cartItemsJson = cartItems,
                taiKhoanViewModel = taiKhoanViewModel,
                khachHangViewModels = khachHangViewModel,
                diaChiViewmodel = diaChiViewModel,
                hoaDonBanViewModel = hoaDonBanViewModel,
                chiTietHoaDonViewmodel = chiTietHoaDonViewmodel,
                sanPhamViewModel = sanphamViewModel,
                gioHangViewModel = gioHangViewModel
            )
        }

        composable(Screens.FAVORITEPRODUCTS.route) {
            FavoriteProductsScreen(
                navController = navHostController,
                sanPhamYeuThichViewModel = sanPhamYeuThichViewModel,
                taiKhoanViewModel = taiKhoanViewModel
            )
        }
            composable("checkout/{totalPrice}/{cartItems}",
                arguments = listOf(
                    navArgument("totalPrice") { type = NavType.IntType },
                    navArgument("cartItems") { type = NavType.StringType }
                )){ backStackEntry ->
                val totalPrice = backStackEntry.arguments?.getInt("totalPrice") ?: 0
                val cartItemsJson = backStackEntry.arguments?.getString("cartItems") ?: ""

                CheckoutScreen(
                    navController = navHostController,
                    totalPrice = totalPrice,
                    cartItemsJson = cartItemsJson,
                    taiKhoanViewModel = taiKhoanViewModel,
                    khachHangViewModels = khachHangViewModel,
                    diaChiViewmodel = diaChiViewModel,
                    hoaDonBanViewModel = hoaDonBanViewModel,
                    chiTietHoaDonViewmodel = chiTietHoaDonViewmodel,
                    sanPhamViewModel = sanphamViewModel,
                    gioHangViewModel = gioHangViewModel
                )
            }
        composable(
            route = Screens.VERIFYEMAILSCREEN.route,
            arguments = listOf(
                navArgument("email") { type = NavType.StringType },
                navArgument("username") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val username = backStackEntry.arguments?.getString("username") ?: ""

            VerifyEmailScreen(
                navController = navHostController,
                email = email,
                username = username
            )
        }
        composable(Screens.VERIFYOTPSCREENFORGOTPASSWORD.route) {
            VerifyEmailScreenForGotPassWord(navHostController, otpViewModel)
        }
        composable(
            route = Screens.RESETPASSWORDSCREEN.route,
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            Log.d("NavCheck", "Received username: $username")
            ResetPasswordScreen(navController = navHostController, username = username)
        }
        composable(Screens.ORDERSUCCESSSCREEN.route) {
            OrderSuccessScreen(navHostController)
        }
        composable(
            route = Screens.ORDERSTATUSSCREEN.route

        ) {

            OrderStatusScreen(
                viewModel = hoaDonBanViewModel,
                sanPhamViewModel = sanphamViewModel,
                chiTietHoaDonViewmodel,
                navController = navHostController
            )
        }
        composable(
            "SEACHSCREENS?searchQuery={searchQuery}&brand={brand}&price={price}&usage={usage}&chip={chip}&screen={screen}",
            arguments = listOf(
                navArgument("searchQuery") { defaultValue = "" },
                navArgument("brand") { defaultValue = "" },
                navArgument("price") { defaultValue = "" },
                navArgument("usage") { defaultValue = "" },
                navArgument("chip") { defaultValue = "" },
                navArgument("screen") { defaultValue = "" }
            )
        ) { backStackEntry ->
            SeachSanphamScreen(navHostController,sanphamViewModel,hinhAnhViewModel,gioHangViewModel)
        }
        composable(
            route = Screens.ORDERDELIVEREDSCREEN.route
        ) {
            OrderDeliveredScreen(
                viewModel = hoaDonBanViewModel,
                sanPhamViewModel = sanphamViewModel,
                chiTietHoaDonViewmodel,
                navController = navHostController
            )
        }



    }
}