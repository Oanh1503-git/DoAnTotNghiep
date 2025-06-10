package com.example.laptopstore

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.lapstore.viewmodels.DiaChiViewmodel
import com.example.laptopstore.models.DiaChi
import com.example.laptopstore.models.Screens
import com.example.laptopstore.viewmodels.DataStoreManager
import com.example.laptopstore.viewmodels.TaiKhoanViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
    navController: NavHostController,
    diaChiViewModel: DiaChiViewmodel,
    taiKhoanViewModel: TaiKhoanViewModel
) {
    val khachHang by taiKhoanViewModel.khachHang.collectAsState()

    val listDiaChi by diaChiViewModel.listDiaChi.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var editingDiaChi by remember { mutableStateOf<DiaChi?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val dataStoreManager = remember { DataStoreManager(context) }
    val customerId by dataStoreManager.customerId.collectAsState(initial = null)

    // Lấy địa chỉ mặc định (nếu có)
    val maKhachHang = customerId ?:""
    val diaChiMacDinh = listDiaChi.firstOrNull { it.MacDinh == 1 }
    LaunchedEffect(maKhachHang){
        Log.d("address","$maKhachHang")

    }
    LaunchedEffect(maKhachHang) {
        if (maKhachHang.isNullOrEmpty()) {
            errorMessage = "Mã khách hàng không hợp lệ"
            return@LaunchedEffect
        }
        try {
            isLoading = true
            errorMessage = null
            diaChiViewModel.getDiaChiKhachHang(maKhachHang)
        } catch (e: Exception) {
            errorMessage = "Lỗi khi tải địa chỉ: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Sổ địa chỉ", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screens.ACCOUNTSCREENS.route)
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay về")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (maKhachHang.isNotBlank()) {
                            scope.launch {
                                try {
                                    isLoading = true
                                    errorMessage = null
                                    diaChiViewModel.getDiaChiKhachHang(maKhachHang)
                                } catch (e: Exception) {
                                    errorMessage = "Không thể làm mới: ${e.message}"
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Làm mới")
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
            // Dòng đầu tiên: Thêm địa chỉ (luôn hiện)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        editingDiaChi = null
                        showDialog = true
                    }
                ) {
                    Text("Thêm địa chỉ")
                }
            }

            // Dưới dòng này là thông tin khách hàng (nếu có địa chỉ mặc định)
            if (diaChiMacDinh != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        // Dòng 1: Tên khách hàng (trái), nút chỉnh sửa (phải)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = diaChiMacDinh.TenNguoiNhan,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(
                                onClick = {
                                    editingDiaChi = diaChiMacDinh
                                    showDialog = true
                                }
                            ) {
                                Text("Chỉnh sửa")
                            }
                        }
                        // Dòng 2: Số điện thoại
                        Text(
                            text = "SĐT: ${diaChiMacDinh.SoDienThoai}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        // Dòng 3: Địa chỉ
                        Text(
                            text = "Địa chỉ: ${diaChiMacDinh.ThongTinDiaChi}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        // Dòng 4: Mặc định
                        Text(
                            text = "Mặc định",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(top = 4.dp) 
                        )
                    }
                }
            }

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            // Danh sách các địa chỉ khác (trừ mặc định)
            val otherAddresses = listDiaChi.filter { it.MacDinh != 1 }
            if (!isLoading && errorMessage == null) {
                Text("Danh sách địa chỉ khác", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))

                if (otherAddresses.isEmpty()) {
                    Text("Không có địa chỉ nào khác", color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(otherAddresses) { diaChi ->
                            DiaChiItem(
                                diaChi = diaChi,
                                onEdit = {
                                    editingDiaChi = diaChi
                                    showDialog = true
                                },
                                onDelete = {
                                    diaChiViewModel.deleteDiaChi(diaChi.MaDiaChi, maKhachHang)
                                    scope.launch {
                                        delay(500)
                                        diaChiViewModel.getDiaChiKhachHang(maKhachHang)
                                    }
                                },
                                onToggleMacDinh = { isChecked ->
                                    if (isChecked) {
                                        diaChiViewModel.setDiaChiMacDinh(maKhachHang,diaChi.MaDiaChi)
                                        scope.launch {
                                            delay(500)
                                            diaChiViewModel.getDiaChiKhachHang(maKhachHang)
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Dialog thêm/sửa địa chỉ
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(modifier = Modifier.padding(16.dp)) {
                DiaChiForm(
                    diaChi = editingDiaChi,
                    maKhachHang = maKhachHang,
                    onSubmit = { diaChi ->
                        if (editingDiaChi == null) {
                            diaChiViewModel.addDiaChi(diaChi.copy(MaKhachHang = maKhachHang), maKhachHang)
                        } else {
                            diaChiViewModel.updateDiaChi(diaChi, maKhachHang)
                        }
                        scope.launch {
                            delay(500)
                            diaChiViewModel.getDiaChiKhachHang(maKhachHang)
                        }
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
    maKhachHang: String,
    onSubmit: (DiaChi) -> Unit,
    onCancel: () -> Unit
) {
    var tenNguoiNhan by remember { mutableStateOf(diaChi?.TenNguoiNhan ?: "") }
    var soDienThoai by remember { mutableStateOf(diaChi?.SoDienThoai ?: "") }
    var thongTinDiaChi by remember { mutableStateOf(diaChi?.ThongTinDiaChi ?: "") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = if (diaChi == null) "Thêm địa chỉ" else "Sửa địa chỉ",
            style = MaterialTheme.typography.titleMedium
        )

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
                        MaKhachHang = maKhachHang,
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

@Composable
fun DiaChiItem(
    diaChi: DiaChi,
    onEdit: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    onToggleMacDinh: ((Boolean) -> Unit)? = null, // <--- Thêm callback này
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (diaChi.MacDinh == 1) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = diaChi.TenNguoiNhan,
                    style = MaterialTheme.typography.titleMedium
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = diaChi.MacDinh == 1,
                        onCheckedChange = { isChecked ->
                            onToggleMacDinh?.invoke(isChecked)
                        }
                    )
                    Text("Mặc định", style = MaterialTheme.typography.bodySmall)
                }
            }
            Text("SĐT: ${diaChi.SoDienThoai}", style = MaterialTheme.typography.bodyMedium)
            Text("Địa chỉ: ${diaChi.ThongTinDiaChi}", style = MaterialTheme.typography.bodyMedium)
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                onEdit?.let {
                    TextButton(onClick = it) { Text("Sửa") }
                }
                onDelete?.let {
                    TextButton(onClick = it) {
                        Text("Xóa", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}
