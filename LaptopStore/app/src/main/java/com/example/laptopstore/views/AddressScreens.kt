package com.example.laptopstore.views

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.laptopstore.models.DiaChi
import com.example.lapstore.viewmodels.DiaChiViewmodel
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreens(
    navHostController: NavHostController,
    diaChiViewModel: DiaChiViewmodel,
    maKhachHang: Int
) {
    var showAddDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // State cho dialog thêm địa chỉ
    var tenNguoiNhan by remember { mutableStateOf("") }
    var soDienThoai by remember { mutableStateOf("") }
    var diaChi by remember { mutableStateOf("") }
    var isDefault by remember { mutableStateOf(false) }

    // Animation state
    var isLoaded by remember { mutableStateOf(false) }
    
    // Load danh sách địa chỉ khi màn hình được tạo
    LaunchedEffect(maKhachHang) {
        diaChiViewModel.getDiaChiByMaKhachHang(maKhachHang)
        isLoaded = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sổ địa chỉ") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = isLoaded,
                enter = scaleIn(animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
                exit = scaleOut(animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
            ) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = Color.Red
                ) {
                    Icon(Icons.Default.Add, "Thêm địa chỉ")
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            AnimatedVisibility(
                visible = isLoaded,
                enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = tween(500)
                ),
                exit = fadeOut(animationSpec = tween(500)) + slideOutVertically(
                    targetOffsetY = { it / 2 },
                    animationSpec = tween(500)
                )
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = diaChiViewModel.listDiacHi,
                        key = { it.MaDiaChi }
                    ) { diaChi ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            AddressCard(
                                diaChi = diaChi,
                                onSetDefault = { 
                                    scope.launch {
                                        diaChiViewModel.setDefaultAddress(diaChi.MaDiaChi)
                                        snackbarHostState.showSnackbar("Đã đặt làm địa chỉ mặc định")
                                    }
                                },
                                onDelete = {
                                    scope.launch {
                                        diaChiViewModel.deleteDiaChi(diaChi.MaDiaChi)
                                        snackbarHostState.showSnackbar("Đã xóa địa chỉ")
                                    }
                                }
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = isLoaded && diaChiViewModel.listDiacHi.isEmpty(),
                enter = fadeIn(animationSpec = tween(500)),
                exit = fadeOut(animationSpec = tween(500))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.LocationOff,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Chưa có địa chỉ nào",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }

    // Dialog thêm địa chỉ với animation
    AnimatedVisibility(
        visible = showAddDialog,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Thêm địa chỉ mới") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = tenNguoiNhan,
                        onValueChange = { tenNguoiNhan = it },
                        label = { Text("Tên người nhận") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    OutlinedTextField(
                        value = soDienThoai,
                        onValueChange = { soDienThoai = it },
                        label = { Text("Số điện thoại") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    OutlinedTextField(
                        value = diaChi,
                        onValueChange = { diaChi = it },
                        label = { Text("Địa chỉ") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isDefault,
                            onCheckedChange = { isDefault = it }
                        )
                        Text("Đặt làm địa chỉ mặc định")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            val newDiaChi = DiaChi(
                                MaDiaChi = 0, // Server sẽ tự sinh
                                MaKhachHang = maKhachHang,
                                TenNguoiNhan = tenNguoiNhan,
                                SoDienThoai = soDienThoai,
                                DiaChi = diaChi,
                                MacDinh = if (isDefault) 1 else 0
                            )
                            diaChiViewModel.themDiaChi(newDiaChi)
                            showAddDialog = false
                            // Reset form
                            tenNguoiNhan = ""
                            soDienThoai = ""
                            diaChi = ""
                            isDefault = false
                            snackbarHostState.showSnackbar("Đã thêm địa chỉ mới")
                        }
                    },
                    enabled = tenNguoiNhan.isNotBlank() && soDienThoai.isNotBlank() && diaChi.isNotBlank()
                ) {
                    Text("Thêm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressCard(
    diaChi: DiaChi,
    onSetDefault: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = diaChi.TenNguoiNhan,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                if (diaChi.MacDinh == 1) {
                    Surface(
                        color = Color.Red.copy(alpha = 0.1f),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            "Mặc định",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = diaChi.SoDienThoai,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = diaChi.DiaChi,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (diaChi.MacDinh != 1) {
                    TextButton(onClick = onSetDefault) {
                        Text("Đặt mặc định")
                    }
                }
                TextButton(onClick = onDelete) {
                    Text("Xóa")
                }
            }
        }
    }
}