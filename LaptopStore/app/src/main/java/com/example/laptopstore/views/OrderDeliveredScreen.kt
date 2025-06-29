package com.example.laptopstore.views

import DonHangDayDuResponse
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.lapstore.viewmodels.HoaDonViewModel

@Composable
fun OrderDeliveredScreen(
    viewModel: HoaDonViewModel,
    maKhachHang: String,
    navController: NavHostController
) {
    val donHangList by viewModel.donHangDayDu.collectAsState()

    LaunchedEffect(maKhachHang) {
        viewModel.getDonHangDayDu(maKhachHang)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Đơn hàng đã giao", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        val deliveredList = donHangList.filter { it.TrangThai == "5" }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(deliveredList) { donHang ->
                DeliveredOrderCard(donHang, navController)
            }
        }
    }
}

@Composable
fun DeliveredOrderCard(
    donHang: DonHangDayDuResponse,
    navController: NavHostController
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Ngày giao: ${donHang.NgayDatHang}")

            donHang.SanPham.forEach { sp ->
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
                        Text("Giá: ${sp.Gia / 1000}.000 VNĐ")
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
