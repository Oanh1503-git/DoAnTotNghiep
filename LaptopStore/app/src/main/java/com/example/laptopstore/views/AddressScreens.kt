package com.example.laptopstore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.lapstore.viewmodels.DiaChiViewmodel
import com.example.laptopstore.models.DiaChi
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.laptopstore.models.Screens
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreens(
    navHostController: NavHostController,
    diaChiViewmodel: DiaChiViewmodel,
    maKhachHang: String
) {
    val listDiaChi = diaChiViewmodel.listDiacHi
    var showDialog by remember { mutableStateOf(false) }
    var diaChiDangSua by remember { mutableStateOf<DiaChi?>(null) }
    val scope = rememberCoroutineScope()

    // Load địa chỉ khi mở màn hình
    LaunchedEffect(maKhachHang) {
        diaChiViewmodel.getDiaChiKhachHang(maKhachHang)
    }
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tài Khoản của tôi",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navHostController.navigate(Screens.HOMEPAGE.route)
                    }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            diaChiViewmodel.getDiaChiKhachHang(maKhachHang)
                        }
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ){  innerPadding->
    Column(modifier = Modifier.padding(innerPadding)
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Danh sách địa chỉ", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))
    
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(listDiaChi.sortedByDescending { it.MacDinh }) { diaChi ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Người nhận: ${diaChi.TenNguoiNhan}")
                                Text("SĐT: ${diaChi.SoDienThoai}")
                                Text("Địa chỉ: ${diaChi.ThongTinDiaChi}")
                            }
                            Switch(
                                checked = diaChi.MacDinh == 1,
                                onCheckedChange = { isChecked ->
                                    scope.launch {
                                        diaChiViewmodel.updateDiaChi(
                                            diaChi.copy(MacDinh = if (isChecked) 1 else 0)
                                        )
                                        // Đợi một chút để đảm bảo cập nhật hoàn tất
                                        delay(500)
                                        diaChiViewmodel.getDiaChiKhachHang(maKhachHang)
                                    }
                                },
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }

                        Row(modifier = Modifier.padding(top = 8.dp)) {
                            if (diaChi.MacDinh == 1) {
                                Text(
                                    "Địa chỉ mặc định",
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(end = 8.dp, top = 8.dp)
                                )
                            }
                            Button(onClick = {
                                diaChiDangSua = diaChi
                                showDialog = true
                            }) {
                                Text("Sửa")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    scope.launch {
                                        diaChiViewmodel.deleteDiaChi(diaChi.MaDiaChi)
                                        // Đợi một chút để đảm bảo xóa hoàn tất
                                        delay(500)
                                        diaChiViewmodel.getDiaChiKhachHang(maKhachHang)
                                    }
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
                        scope.launch {
                            // Đợi một chút để đảm bảo thao tác thêm/sửa hoàn tất
                            delay(500)
                            diaChiViewmodel.getDiaChiKhachHang(maKhachHang)
                        }
                        showDialog = false
                    },
                    onCancel = { showDialog = false }
                )
            }
        }
    }
}}


@Composable
fun DiaChiForm(
    diaChi: DiaChi?,
    onSubmit: (DiaChi) -> Unit,
    onCancel: () -> Unit
) {
    var tenNguoiNhan by remember { mutableStateOf(diaChi?.TenNguoiNhan ?: "") }
    var soDienThoai by remember { mutableStateOf(diaChi?.SoDienThoai ?: "") }
    var thongTinDiaChi by remember { mutableStateOf(diaChi?.ThongTinDiaChi ?: "") }
    var maKhachHang by remember { mutableStateOf(diaChi?.MaKhachHang ?:"") }
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
                        MaKhachHang = maKhachHang , // Sẽ được override nếu thêm mới
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
