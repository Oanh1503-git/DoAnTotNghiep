package com.example.laptopstore.views

import DonHangDayDuResponse
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.lapstore.viewmodels.HoaDonViewModel
import com.example.laptopstore.viewmodels.ChiTietHoaDonViewmodel
import com.example.laptopstore.viewmodels.DataStoreManager
import com.example.laptopstore.viewmodels.SanPhamViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDeliveredScreen(
    viewModel: HoaDonViewModel,
    sanPhamViewModel: SanPhamViewModel,
    chitietdonhang: ChiTietHoaDonViewmodel,
    navController: NavHostController
) {
    val donHangList by viewModel.donHangDayDu.collectAsState(initial = emptyList())
    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }
    val customerId by dataStoreManager.customerId.collectAsState(initial = null)
    val maKhachHang = customerId
    val currencyFormatter = NumberFormat.getInstance(Locale("vi", "VN")).apply {
        maximumFractionDigits = 0
    }

    LaunchedEffect(maKhachHang) {
        if (maKhachHang != null) {
            viewModel.getDonHangDayDu(maKhachHang)
            Log.d("OrderDeliveredScreen", "Fetching orders for customerId: $maKhachHang")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Đơn hàng đã giao", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            val donHangDaGiao = donHangList.filter { it.TrangThai == "3" }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(donHangDaGiao) { donHang ->
                    DeliveredOrderCard(donHang, viewModel, navController, currencyFormatter)
                }
            }
        }
    }
}

@Composable
fun DeliveredOrderCard(
    donHang: DonHangDayDuResponse,
    viewModel: HoaDonViewModel,
    navController: NavHostController,
    currencyFormatter: NumberFormat
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            donHang.SanPham?.forEach { sp ->
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(sp.HinhAnh)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp)
                    )
                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(sp.TenSanPham, fontWeight = FontWeight.SemiBold)
                        Text("Giá: ${currencyFormatter.format(sp.Gia)} VNĐ")
                        Text("Số lượng: ${sp.SoLuong}")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                navController.navigate("review/${donHang.MaHoaDon}")
            }) {
                Text("Đánh giá sản phẩm")
            }
        }
    }
}
