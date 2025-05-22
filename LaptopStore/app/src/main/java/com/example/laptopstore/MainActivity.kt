package com.example.laptopstore


import HinhAnhViewModel
import SanPhamViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.laptopstore.navigation.NavigationGraph
import com.example.laptopstore.ui.theme.LaptopStoreTheme
import com.example.laptopstore.viewmodels.KhachHangViewModels
import com.example.laptopstore.viewmodels.TaiKhoanViewModel
import androidx.lifecycle.viewmodel.compose.viewModel



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var NavHostController: NavHostController
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LaptopStoreTheme {
                val navController = rememberNavController()


                // Dùng viewModel() để Compose tự quản lý vòng đời ViewModel
                val sanPhamViewModel: SanPhamViewModel = viewModel()
                val hinhAnhViewModel: HinhAnhViewModel = viewModel()
                val khachHangViewModel: KhachHangViewModels = viewModel()
                val taiKhoanViewModel: TaiKhoanViewModel = viewModel()

                NavigationGraph(
                    navHostController = navController,
                    sanphamViewModel = sanPhamViewModel,
                    hinhAnhViewModel = hinhAnhViewModel,
                    khachHangViewModel = khachHangViewModel,
                    taiKhoanViewModel = taiKhoanViewModel
                )
            }
        }
    }
}
