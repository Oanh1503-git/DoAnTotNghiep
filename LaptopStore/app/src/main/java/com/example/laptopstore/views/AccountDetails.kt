package com.example.laptopstore.views

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.laptopstore.models.KhachHang
import com.example.laptopstore.models.Screens
import com.example.laptopstore.viewmodels.KhachHangViewModels

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetails(
    navController: NavHostController,
    khachHangViewModels: KhachHangViewModels,
    maKhachHang: String
) {

    val khachHang by khachHangViewModels.khachhang.collectAsState()
    var maKhachHangState by remember { mutableStateOf("") }
    var hoTen by remember { mutableStateOf("") }
    var sdt by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var gioiTinh by remember { mutableStateOf("") }
    var ngaySinh by remember { mutableStateOf("") }

    var isEditing by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(maKhachHang) {
        Log.d("KhachHang", "mã khách hàng on accoutdetals $maKhachHang")
        if (maKhachHang.isNotEmpty()) {
            isLoading = true
            try {
                Log.d("KhachHang", "Đang tải thông tin khách hàng: $maKhachHang")
                khachHangViewModels.getKhachHangById(maKhachHang)
            } catch (e: Exception) {
                Log.e("KhachHang", "Lỗi khi tải thông tin: ${e.message}")
                error = "Không thể tải thông tin khách hàng"
            } finally {
                isLoading = false
            }
        }
    }

    // Cập nhật state khi có dữ liệu khách hàng mới
    LaunchedEffect(khachHang) {
        Log.d("KhachHang", "Dữ liệu khách hàng nhận được: $khachHang")
        khachHang?.let {
            maKhachHangState = maKhachHang
            hoTen = it.HoTen
            sdt = it.SoDienThoai
            email = it.Email
            gioiTinh = it.GioiTinh
            ngaySinh = it.NgaySinh
            Log.d("KhachHang", "Đã cập nhật thông tin: Họ tên=$hoTen, SDT=$sdt")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thông tin tài khoản") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screens.ACCOUNTSCREENS.route)
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay về")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        isEditing = !isEditing
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Chỉnh sửa")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {

            OutlinedTextField(
                value = hoTen,
                onValueChange = { if (isEditing) hoTen = it },
                label = { Text("Họ tên") },
                enabled = isEditing,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ngaySinh,
                onValueChange = { if (isEditing) ngaySinh = it },
                label = { Text("Ngày sinh") },
                enabled = isEditing,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = gioiTinh,
                onValueChange = { if (isEditing) gioiTinh = it },
                label = { Text("Giới tính") },
                enabled = isEditing,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { if (isEditing) email = it },
                label = { Text("Email") },
                enabled = isEditing,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = sdt,
                onValueChange = { if (isEditing) sdt = it },
                label = { Text("Số điện thoại") },
                enabled = isEditing,
                modifier = Modifier.fillMaxWidth()
            )

            if (isEditing) {
                Button(
                    onClick = {
                        val updatedKhachHang = KhachHang(
                            MaKhachHang = maKhachHangState,
                            HoTen = hoTen,
                            SoDienThoai = sdt,
                            Email = email,
                            GioiTinh = gioiTinh,
                            NgaySinh = ngaySinh
                        )
                        khachHangViewModels.updateKhachHang(updatedKhachHang)
                        isEditing = false
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Lưu thông tin")
                }
            }

            // Hiển thị lỗi nếu có
            error?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

