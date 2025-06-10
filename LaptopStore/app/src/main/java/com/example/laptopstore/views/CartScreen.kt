package com.example.laptopstore.views

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.laptopstore.models.GioHang
import com.example.laptopstore.models.Screens
import com.example.laptopstore.models.SanPham
import com.example.laptopstore.viewmodels.GioHangViewModel
import com.example.laptopstore.viewmodels.SanPhamViewModel
import com.example.laptopstore.viewmodels.TaiKhoanViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavHostController,
    gioHangViewModel: GioHangViewModel,
    sanPhamViewModel: SanPhamViewModel,
    taiKhoanViewModel: TaiKhoanViewModel,
    savedStateHandle: SavedStateHandle
) {
    var isUpdating by remember { mutableStateOf(false) }
    var outOfStockMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val taikhoan by taiKhoanViewModel.taikhoan.collectAsState()
    val cartItems by gioHangViewModel.listGioHang.collectAsState(initial = emptyList())
    val allProducts by sanPhamViewModel.danhSachAllSanPham.collectAsState(initial = emptyList())
    val loginState by savedStateHandle.getStateFlow("login_state", false).collectAsState()
    
    // Loading và error states
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var key by remember { mutableStateOf(0) }
    val selectedItems = remember { mutableStateListOf<Int>() }
    val isDataReady = allProducts.isNotEmpty()
    val validCartItems = if(isDataReady){
        cartItems.filter { gioHang ->
            allProducts.any { it.MaSanPham == gioHang.MaSanPham }
        }
    }else emptyList()
    // Thêm log chi tiết
    LaunchedEffect(taikhoan) {
        Log.d("CartScreen", "TaiKhoan State: ${taikhoan != null}")
        Log.d("CartScreen", "MaKhachHang: ${taikhoan?.MaKhachHang}")
        Log.d("CartScreen", "TenTaiKhoan: ${taikhoan?.TenTaiKhoan}")
        Log.d("CartScreen", "Số lượng sản phẩm: ${allProducts.size}, Danh sách: $allProducts")
        Log.d("CartScreen", "Số lượng sản phẩm: ${cartItems.size}, Danh sách: $cartItems")
        taikhoan?.MaKhachHang?.let { gioHangViewModel.getGioHangByKhachHang(it) }
        if (taikhoan?.MaKhachHang.isNullOrEmpty()) {
            Log.w("CartScreen", "MaKhachHang is null or empty")
        }
    }
    LaunchedEffect(cartItems, allProducts) {
        cartItems.forEach { gioHang ->
            val sp = allProducts.find { it.MaSanPham == gioHang.MaSanPham }
            if (sp == null) {
                gioHangViewModel.deleteGioHang(gioHang.MaGioHang)
            }
        }
    }

    // Kiểm tra đăng nhập
    val isLoggedIn = remember(taikhoan) {
        !taikhoan?.MaKhachHang.isNullOrEmpty()
    }

    if (!isLoggedIn) {
        Dialog(
            onDismissRequest = {
                navController.popBackStack()
            }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Vui lòng đăng nhập để xem giỏ hàng",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { navController.popBackStack() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text("Quay lại")
                        }
                        Button(
                            onClick = { 
                                navController.navigate(Screens.Login_Screens.route) {
                                    popUpTo("cart_screen") { inclusive = true }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Đăng nhập")
                        }
                    }
                }
            }
        }
        return
    }

    // Load dữ liệu khi đã đăng nhập
    LaunchedEffect(key, loginState, taikhoan) {
        if (loginState && taikhoan != null) {
            try {
                isLoading = true
                val maKhachHang = taikhoan?.MaKhachHang
                if (maKhachHang.isNullOrEmpty()) {
                    Log.w("CartScreen", "Token may be expired, MaKhachHang is null")
                    navController.navigate(Screens.Login_Screens.route) {
                        popUpTo("cart_screen") { inclusive = true }
                    }
                    return@LaunchedEffect
                }
                
                // Load data
                sanPhamViewModel.getAllSanPham()
                gioHangViewModel.getGioHangByKhachHang(maKhachHang)
                
            } catch (e: Exception) {
                Log.e("CartScreen", "Error loading data", e)
                errorMessage = "Lỗi khi tải dữ liệu: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    // Xử lý chuyển đến trang thanh toán
    fun navigateToCheckout(cartItems: List<GioHang>, totalPrice: Int) {
        try {
            val simplifiedCartItems = cartItems.map { gioHang ->
                val sanPham = allProducts.find { it.MaSanPham == gioHang.MaSanPham }
                CartItem(
                    MaGioHang = gioHang.MaGioHang,
                    MaSanPham = gioHang.MaSanPham,
                    SoLuong = gioHang.SoLuong,
                    product = sanPham?.let { sp ->
                        Product(
                            MaSanPham = sp.MaSanPham,
                            TenSanPham = sp.TenSanPham,
                            HinhAnh = sp.HinhAnh,
                            Gia = sp.Gia,
                            GiamGia = sp.GiamGia,
                            SoLuong = sp.SoLuong
                        )
                    }
                )
            }
            
            val cartItemsJson = Json.encodeToString(simplifiedCartItems)
            val encodedCartItems = URLEncoder.encode(cartItemsJson, StandardCharsets.UTF_8.toString())
            
            if (encodedCartItems.length > 500000) {
                throw Exception("Dữ liệu giỏ hàng quá lớn")
            }
            
            navController.navigate(Screens.CHECKOUTSCREENS.route
                .replace("{totalPrice}", totalPrice.toString())
                .replace("{cartItems}", encodedCartItems))
            
        } catch (e: Exception) {
            Log.e("CartScreen", "Error navigating to checkout: ${e.message}")
            errorMessage = "Không thể chuyển đến trang thanh toán: ${e.message}"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Giỏ hàng",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            MenuBottomNavBar(navController)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.Red)
                    }
                }
                errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.Red
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = errorMessage ?: "Đã xảy ra lỗi",
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { 
                                errorMessage = null
                                key += 1
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Thử lại")
                        }
                    }
                }
                !isDataReady -> {
                    // Chờ dữ liệu load xong
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.Red)
                    }
                }
                validCartItems.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(100.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Giỏ hàng của bạn đang trống",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { navController.navigate(Screens.HOMEPAGE.route) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Tiếp tục mua sắm")
                        }
                    }
                }
                else -> {
                    Column {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(validCartItems) { gioHang ->
                                val sanPham = allProducts.find { it.MaSanPham == gioHang.MaSanPham }
                                if (sanPham != null) {
                                    CartItemCard(
                                        gioHang = gioHang,
                                        sanPham = sanPham,
                                        navController = navController,
                                        onQuantityChange = { newQuantity ->
                                            isUpdating=true
                                            if (newQuantity <= 0) {
                                                gioHangViewModel.deleteGioHang(gioHang.MaGioHang)
                                                selectedItems.remove(gioHang.MaGioHang)
                                            } else if (newQuantity <= sanPham.SoLuong) {
                                                val updatedGioHang = gioHang.copy(SoLuong = newQuantity)
                                                gioHangViewModel.updateGioHang(updatedGioHang)
                                            } else{
                                                outOfStockMessage = "Sản phẩm ${sanPham.TenSanPham} chỉ còn ${sanPham.SoLuong} trong kho"
                                                 outOfStockMessage= " Hết hàng : ${sanPham.TenSanPham}"
                                            }
                                            isUpdating=false
                                        },
                                        isSelected = selectedItems.contains(gioHang.MaGioHang),
                                        onCheckedChange = { checked ->
                                            if (checked) {
                                                selectedItems.add(gioHang.MaGioHang)
                                            } else {
                                                selectedItems.remove(gioHang.MaGioHang)
                                            }
                                        },
                                        isUpdating = isUpdating
                                    )

                                }
                            }
                        }

                        val totalPrice = validCartItems.filter { selectedItems.contains(it.MaGioHang) }.sumOf { gioHang ->
                            allProducts.find { it.MaSanPham == gioHang.MaSanPham }?.let { sp ->
                                sp.Gia * gioHang.SoLuong
                            } ?: 0
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Tổng tiền:",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${totalPrice / 1000}.000 VNĐ",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Red
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    if (selectedItems.isEmpty()) {
                                        outOfStockMessage = "Vui lòng chọn ít nhất 1 sản phẩm để đặt hàng"
                                        return@Button
                                    }
                                    val selectedCartItems = cartItems.filter { selectedItems.contains(it.MaGioHang) }
                                    navigateToCheckout(selectedCartItems, totalPrice)
                                },
                                enabled = selectedItems.isNotEmpty(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text("Đặt Hàng ")
                            }
                        }
                    }
                }
            }
        }
    }
    if (outOfStockMessage != null) {
        AlertDialog(
            onDismissRequest = { outOfStockMessage = null },
            title = { Text("Thông báo") },
            text = { Text(outOfStockMessage ?: "") },
            confirmButton = {
                Button(onClick = { outOfStockMessage = null }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun CartItemCard(
    gioHang: GioHang,
    sanPham: SanPham,
    navController: NavHostController,
    onQuantityChange: (Int) -> Unit,
    isSelected: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isUpdating: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp)
    ) {
        if (isUpdating) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.Red
                )
            }
        }

        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = onCheckedChange
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        navController.navigate("product_detail/${sanPham.MaSanPham}")
                    }
            ) {
                AsyncImage(
                    model = sanPham.HinhAnh,
                    contentDescription = sanPham.TenSanPham,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = sanPham.TenSanPham,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${sanPham.Gia / 1000}.000 VNĐ",
                    fontSize = 14.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        onQuantityChange(gioHang.SoLuong - 1) },
                        enabled =  !isUpdating) {
                        Icon(Icons.Default.Remove, contentDescription = null)
                    }
                    if (isUpdating){
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.Red
                        )
                    }else{
                        Text(text = gioHang.SoLuong.toString(), fontSize = 16.sp)
                    }
                    IconButton(
                        onClick = {

                                onQuantityChange(gioHang.SoLuong + 1)
                        },
                        enabled = gioHang.SoLuong < sanPham.SoLuong
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                }
            }
            IconButton(onClick = { onQuantityChange(0) }) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
            }
        }
    }
}
