package com.example.laptopstore.screens

import DonHangDayDuResponse
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.laptopstore.viewmodels.ChiTietHoaDonViewmodel
import com.example.laptopstore.viewmodels.DataStoreManager
import com.example.laptopstore.viewmodels.SanPhamViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderStatusScreen(
    viewModel: HoaDonViewModel,
    sanPhamViewModel: SanPhamViewModel,
    chitietdonhang: ChiTietHoaDonViewmodel,
    navController: NavHostController
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Chờ xác nhận", "Đã duyệt", "Đang vận chuyển", "Đã hủy")
    val donHangList by viewModel.donHangDayDu.collectAsState(initial = emptyList())
    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }
    val customerId by dataStoreManager.customerId.collectAsState(initial = null)
    val maKhachHang = customerId
    val currencyFormatter = NumberFormat.getInstance(Locale("vi", "VN"))

    LaunchedEffect(maKhachHang) {
        if (maKhachHang != null) {
            viewModel.getDonHangDayDu(maKhachHang)
            Log.d("OrderStatusScreen", "Fetching orders for customerId: $maKhachHang")
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trạng thái đơn hàng", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {  IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }

                }
            )
        }
    ){innerPadding ->
    Column(modifier = Modifier.padding(innerPadding)
        .fillMaxSize()
        .padding(16.dp)) {
        Text("Trạng thái đơn hàng", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        TabRow(selectedTabIndex = selectedTabIndex) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val trangThaiMapping = mapOf(0 to "0", 1 to "1", 2 to "2", 3 to "4")
        val filteredDonHang = donHangList.filter { it.TrangThai == trangThaiMapping[selectedTabIndex] }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(filteredDonHang) { donHang ->
                OrderCard(donHang, selectedTabIndex, viewModel, navController)
            }
        }
    }
}
}
@Composable
fun OrderCard(
    donHang: DonHangDayDuResponse,
    tabIndex: Int,
    viewModel: HoaDonViewModel,
    navController: NavHostController
) {

    val currencyFormatter = NumberFormat.getInstance(Locale("vi", "VN")).apply {
        maximumFractionDigits = 0 // Không hiển thị phần thập phân
    }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (tabIndex == 0) {
                Text("Ngày đặt: ${donHang.NgayDatHang}")
            }

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

            if (tabIndex == 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    viewModel.updateTrangThai(donHang.MaHoaDon, 4)
                }) {
                    Text("Hủy đơn hàng")
                }
            }
        }
    }
}
