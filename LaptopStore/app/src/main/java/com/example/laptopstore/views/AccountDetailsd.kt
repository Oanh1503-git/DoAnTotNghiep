package com.example.laptopstore.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.laptopstore.viewmodels.KhachHangViewModels
import com.example.laptopstore.viewmodels.TaiKhoanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetails(
    navHostController: NavHostController,
    khachHangViewModel: KhachHangViewModels,
    maKhachHang: String  // truyền vào từ username đang đăng nhập
) {
    // Gọi API khi khởi tạo composable
    LaunchedEffect(maKhachHang) {
        khachHangViewModel.getKhachHangById(maKhachHang)
    }

    val khachHang = khachHangViewModel.khachhang

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thông tin tài khoản") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        }
    ) { padding ->
        khachHang?.let { kh ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoRow(label = "Mã khách hàng", value = kh.MaKhachHang.toString())
                InfoRow(label = "Họ tên", value = kh.HoTen)
                InfoRow(label = "Giới tính", value = kh.GioiTinh)
                InfoRow(label = "Ngày sinh", value = kh.NgaySinh)
                InfoRow(label = "Email", value = kh.Email)
                InfoRow(label = "Số điện thoại", value = kh.SoDienThoai)
            }
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column {
        Text(text = label, fontWeight = FontWeight.Bold)
        Text(text = value, modifier = Modifier.padding(start = 8.dp))
        Divider(modifier = Modifier.padding(vertical = 4.dp))
    }
}
