package com.example.laptopstore.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.laptopstore.models.Screens
import com.example.lapstore.viewmodels.HoaDonBanViewModel
import com.example.laptopstore.viewmodels.DataStoreManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderStatusScreen(
    navController: NavHostController,
    hoaDonBanViewModel: HoaDonBanViewModel
) {
    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }
    val customerId by dataStoreManager.customerId.collectAsState(initial = null)
    val hoaDonList by hoaDonBanViewModel.danhSachHoaDonCuaKhachHang.collectAsState() // cần exposed list từ ViewModel

    val tabs = listOf("Chờ xác nhận", "Đang giao", "Đã giao", "Đã hủy")
    val selectedTab = remember { androidx.compose.runtime.mutableStateOf(0) }

    LaunchedEffect(customerId) {
        customerId?.let {
            hoaDonBanViewModel.getHoaDonTheoKhachHang(it,selectedTab.value)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Trạng thái đơn hàng") })
        }
    ) { paddingValues ->

        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
        ) {
            TabRow(
                selectedTabIndex = selectedTab.value
            ) {
                tabs.forEachIndexed { index, title ->
                    androidx.compose.material3.Tab(
                        selected = selectedTab.value == index,
                        onClick = { selectedTab.value = index },
                        text = { Text(title) }
                    )
                }
            }

            val filteredList = hoaDonList.filter { it.TrangThai == selectedTab.value }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(filteredList.size) { index ->
                    val hoaDon = filteredList[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Mã hóa đơn: ${hoaDon.MaHoaDonBan}", fontWeight = FontWeight.Bold)
                            Text("Ngày đặt: ${hoaDon.NgayDatHang}")
                            Text("Tổng tiền: ${hoaDon.TongTien / 1000}.000 VNĐ")
                            Text("Phương thức thanh toán: ${hoaDon.PhuongThucThanhToan}")
                            Text("Trạng thái: ${getTrangThaiText(hoaDon.TrangThai)}", color = Color.Blue)
                        }
                    }
                }
            }
        }
    }

}

fun getTrangThaiText(trangThai: Int): String {
    return when (trangThai) {
        0 -> "Chờ xác nhận"
        1 -> "Đang giao"
        2 -> "Đã giao"
        3 -> "Đã hủy"
        else -> "Không xác định"
    }
}
