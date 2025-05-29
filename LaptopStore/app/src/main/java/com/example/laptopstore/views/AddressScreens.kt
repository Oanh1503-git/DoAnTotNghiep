package com.example.laptopstore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lapstore.viewmodels.DiaChiViewmodel
import com.example.laptopstore.models.DiaChi
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController

@Composable
fun AddressScreens(
    navHostController: NavHostController,
    diaChiViewmodel: DiaChiViewmodel,
    maKhachHang: Int
) {
    val listDiaChi = diaChiViewmodel.listDiacHi
    var showDialog by remember { mutableStateOf(false) }
    var diaChiDangSua by remember { mutableStateOf<DiaChi?>(null) }

    // Load địa chỉ khi mở màn hình
    LaunchedEffect(maKhachHang) {
        diaChiViewmodel.getDiaChiKhachHang(maKhachHang)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Danh sách địa chỉ", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(listDiaChi) { diaChi ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Người nhận: ${diaChi.TenNguoiNhan}")
                        Text("SĐT: ${diaChi.SoDienThoai}")
                        Text("Địa chỉ: ${diaChi.ThongTinDiaChi}")

                        Row(modifier = Modifier.padding(top = 8.dp)) {
                            Button(onClick = {
                                diaChiDangSua = diaChi
                                showDialog = true
                            }) {
                                Text("Sửa")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    diaChiViewmodel.deleteDiaChi(diaChi.MaDiaChi)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text("Xóa", color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                diaChiDangSua = null
                showDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Thêm địa chỉ")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Thông báo khi thêm hoặc cập nhật
        if (diaChiViewmodel.diachiAddResult.isNotEmpty()) {
            Text(diaChiViewmodel.diachiAddResult, color = Color.Green)
        }
        if (diaChiViewmodel.diachiUpdateResult.isNotEmpty()) {
            Text(diaChiViewmodel.diachiUpdateResult, color = Color.Blue)
        }
    }

    // Dialog thêm/sửa địa chỉ
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(modifier = Modifier.padding(16.dp)) {
                DiaChiForm(
                    diaChi = diaChiDangSua,
                    onSubmit = { diaChi ->
                        if (diaChiDangSua == null) {
                            diaChiViewmodel.addDiaChi(diaChi.copy(MaKhachHang = maKhachHang))
                        } else {
                            diaChiViewmodel.updateDiaChi(diaChi)
                        }
                        diaChiViewmodel.getDiaChiKhachHang(maKhachHang) // Load lại danh sách
                        showDialog = false
                    },
                    onCancel = { showDialog = false }
                )
            }
        }
    }
}


@Composable
fun DiaChiForm(
    diaChi: DiaChi?,
    onSubmit: (DiaChi) -> Unit,
    onCancel: () -> Unit
) {
    var tenNguoiNhan by remember { mutableStateOf(diaChi?.TenNguoiNhan ?: "") }
    var soDienThoai by remember { mutableStateOf(diaChi?.SoDienThoai ?: "") }
    var thongTinDiaChi by remember { mutableStateOf(diaChi?.ThongTinDiaChi ?: "") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = if (diaChi == null) "Thêm địa chỉ" else "Sửa địa chỉ", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = tenNguoiNhan,
            onValueChange = { tenNguoiNhan = it },
            label = { Text("Tên người nhận") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = soDienThoai,
            onValueChange = { soDienThoai = it },
            label = { Text("Số điện thoại") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = thongTinDiaChi,
            onValueChange = { thongTinDiaChi = it },
            label = { Text("Địa chỉ") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(
                onClick = {
                    val diaChiMoi = DiaChi(
                        MaDiaChi = diaChi?.MaDiaChi ?: 0,
                        MaKhachHang = diaChi?.MaKhachHang ?: 0, // Sẽ được override nếu thêm mới
                        TenNguoiNhan = tenNguoiNhan,
                        SoDienThoai = soDienThoai,
                        ThongTinDiaChi = thongTinDiaChi,
                        MacDinh = diaChi?.MacDinh ?: 0
                    )
                    onSubmit(diaChiMoi)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (diaChi == null) "Thêm" else "Lưu")
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("Hủy")
            }
        }
    }
}
