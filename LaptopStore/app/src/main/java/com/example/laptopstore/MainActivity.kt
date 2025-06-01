package com.example.laptopstore



import TaiKhoanAPIService
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
import com.example.lapstore.viewmodels.DiaChiViewmodel
import com.example.laptopstore.RetrofitClient.LaptopStoreRetrofitClient
import com.example.laptopstore.viewmodels.GioHangViewModel
import com.example.laptopstore.viewmodels.HinhAnhViewModel
import com.example.laptopstore.viewmodels.SanPhamViewModel
import com.example.laptopstore.viewmodels.TaiKhoanViewsModelsFactory


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
                val taiKhoanViewModel: TaiKhoanViewModel = viewModel(
                    factory = TaiKhoanViewsModelsFactory(this)
                )
                val diaChiViewmodel:DiaChiViewmodel= viewModel()
                val gioHangViewModel:GioHangViewModel= viewModel()
                NavigationGraph(
                    navHostController = navController,
                    sanphamViewModel = sanPhamViewModel,
                    hinhAnhViewModel = hinhAnhViewModel,
                    khachHangViewModel = khachHangViewModel,
                    taiKhoanViewModel = taiKhoanViewModel,
                    diaChiViewModel = diaChiViewmodel,
                    gioHangViewModel=gioHangViewModel
                )
            }
        }
    }
}
