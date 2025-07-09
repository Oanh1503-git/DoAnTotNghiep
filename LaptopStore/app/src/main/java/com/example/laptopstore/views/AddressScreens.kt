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
import com.example.laptopstore.api.District
import com.example.laptopstore.api.Province
import com.example.laptopstore.api.Ward
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
    taiKhoanViewModel: TaiKhoanViewModel,
    fromCheckout: Boolean = false
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
                        if (fromCheckout) {
                            navController.popBackStack() // hoặc navController.navigateUp()
                        } else {
                            navController.navigate(Screens.ACCOUNTSCREENS.route)
                        }

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
                    diaChiViewModel = diaChiViewModel,
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaChiForm(
    diaChi: DiaChi?,
    maKhachHang: String,
    diaChiViewModel: DiaChiViewmodel, // Thêm ViewModel này
    onSubmit: (DiaChi) -> Unit,
    onCancel: () -> Unit
) {

    var tenNguoiNhan by remember { mutableStateOf(diaChi?.TenNguoiNhan ?: "") }
    var soDienThoai by remember { mutableStateOf(diaChi?.SoDienThoai ?: "") }
    var expandedProvince by remember { mutableStateOf(false) }
    var expandedDistrict by remember { mutableStateOf(false) }
    var expandedWard by remember { mutableStateOf(false) }
    // Province selection
    val provinces by diaChiViewModel.provinces.collectAsState()
    val districts by diaChiViewModel.districts.collectAsState()
    val wards by diaChiViewModel.wards.collectAsState()
    var selectedProvince by remember { mutableStateOf<Province?>(null) }
    var selectedDistrict by remember { mutableStateOf<District?>(null) }
    var selectedWard by remember { mutableStateOf<Ward?>(null) }
    var thongtinbosung by remember { mutableStateOf(diaChi?.ThongTinDiaChi ?: "") }

    LaunchedEffect(Unit) {
        diaChiViewModel.fetchProvinces()
    }
    LaunchedEffect(diaChi) {
        diaChi?.let {
            // 1. Fetch tất cả provinces trước
            diaChiViewModel.fetchProvinces()

            // 2. Sau khi fetch xong, chọn province khớp với diaChi
            val province = diaChiViewModel.provinces.value.find { province ->
                province.code == diaChi.provinceId // provinceId là tên field bạn lưu
            }
            selectedProvince = province

            // 3. Nếu có province, fetch districts
            province?.let {
                diaChiViewModel.fetchDistricts(province.code)

                // 4. Sau khi fetch xong, chọn district
                val district = diaChiViewModel.districts.value.find { district ->
                    district.code == diaChi.districtId
                }
                selectedDistrict = district

                // 5. Nếu có district, fetch wards
                district?.let {
                    diaChiViewModel.fetchWards(district.code)

                    // 6. Sau khi fetch xong, chọn ward
                    val ward = diaChiViewModel.wards.value.find { ward ->
                        ward.code == diaChi.wardId
                    }
                    selectedWard = ward
                }
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // ... Tên người nhận và SĐT như cũ

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

        // Dropdown Province
        ExposedDropdownMenuBox(
            expanded = expandedProvince,
            onExpandedChange = { expandedProvince = !expandedProvince }
        ) {
            OutlinedTextField(
                value = selectedProvince?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Tỉnh/Thành Phố") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor() // ⚠️ Cần thêm menuAnchor() trong Compose Material3
            )

            ExposedDropdownMenu(
                expanded = expandedProvince,
                onDismissRequest = { expandedProvince = false }
            ) {
                provinces.forEach { province ->
                    DropdownMenuItem(
                        text = { Text(province.name) },
                        onClick = {
                            selectedProvince = province

                            expandedProvince = false // Đóng menu sau khi chọn
                            diaChiViewModel.fetchDistricts(province.code)
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expandedDistrict,
            onExpandedChange = { expandedDistrict = !expandedDistrict }
        ) {
            OutlinedTextField(
                value = selectedDistrict?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Quận/Huyện") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expandedDistrict,
                onDismissRequest = { expandedDistrict = false }
            ) {
                districts.forEach { district ->
                    DropdownMenuItem(
                        text = { Text(district.name) },
                        onClick = {
                            selectedDistrict = district
                            expandedDistrict = false
                            diaChiViewModel.fetchWards(district.code)
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expandedWard,
            onExpandedChange = { expandedWard = !expandedWard }
        ) {
            OutlinedTextField(
                value = selectedWard?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Phường/Xã") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expandedWard,
                onDismissRequest = { expandedWard = false }
            ) {
                wards.forEach { ward ->
                    DropdownMenuItem(
                        text = { Text(ward.name) },
                        onClick = {
                            selectedWard = ward
                            expandedWard = false
                        }
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = thongtinbosung,
            onValueChange = { thongtinbosung = it },
            label = { Text("thông tin bổ sung ") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(
                onClick = {
                    val fullAddress = "${selectedWard?.name ?: ""}, ${selectedDistrict?.name ?: ""}, ${selectedProvince?.name ?: ""},${thongtinbosung}"
                    val diaChiMoi = DiaChi(
                        MaDiaChi = diaChi?.MaDiaChi ?: 0,
                        MaKhachHang = maKhachHang,
                        TenNguoiNhan = tenNguoiNhan,
                        SoDienThoai = soDienThoai,
                        ThongTinDiaChi = fullAddress,
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
