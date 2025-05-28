package com.example.laptopstore.views

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    gioHangViewModel: GioHangViewModel = viewModel(),
    sanPhamViewModel: SanPhamViewModel = viewModel(),
    taiKhoanViewModel: TaiKhoanViewModel = viewModel()
) {
    val isLoggedIn by taiKhoanViewModel.isLoggedIn.collectAsState()
    val tentaikhoan = taiKhoanViewModel.tentaikhoan
    val taikhoan = taiKhoanViewModel.taikhoan
    val cartItems by gioHangViewModel.listGioHang.collectAsState()
    val allProducts by sanPhamViewModel.danhSachAllSanPham.collectAsState()

    // Loading và error states
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    // Theo dõi trạng thái đã load giỏ hàng
    var hasLoadedCart by remember { mutableStateOf(false) }

    // LaunchedEffect để lấy dữ liệu
    LaunchedEffect(isLoggedIn, tentaikhoan, hasLoadedCart) {
        if (isLoggedIn && tentaikhoan != null && !hasLoadedCart) {
            isLoading = true
            errorMessage = null
            try {
                // Load sản phẩm trước
                sanPhamViewModel.getAllSanPham()
                
                // Lấy thông tin tài khoản nếu chưa có
                if (taikhoan == null && tentaikhoan != null) {
                    taiKhoanViewModel.getTaiKhoanByTentaikhoan(tentaikhoan)
                }
                
                // Sau đó load giỏ hàng với mã khách hàng
                val makh=taikhoan?.MaKhachHang
               if(makh != null){
                   gioHangViewModel.getGioHangByKhachHang(makh)
               }
                else{
                   errorMessage = "Không tìm thấy thông tin tài khoản"
               }
            } catch (e: Exception) {
                errorMessage = "Lỗi khi tải giỏ hàng: ${e.message}"
                Log.e("CartScreen", "Error loading cart", e)
            } finally {
                isLoading = false
            }
        }
    }

    // Effect để reset hasLoadedCart khi đăng xuất
    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            hasLoadedCart = false
        }
    }

    // Effect để theo dõi thay đổi của taikhoan
    LaunchedEffect(taikhoan) {
        val makh=taikhoan?.MaKhachHang
     if (isLoggedIn && !hasLoadedCart && makh !=null){
         gioHangViewModel.getGioHangByKhachHang(makh)
         hasLoadedCart =true
     }
    }

    // Tính tổng tiền từ giỏ hàng
    val totalPrice = cartItems.sumOf { gioHang ->
        val sanPham = allProducts.find { it.MaSanPham == gioHang.MaSanPham }
        val gia = sanPham?.Gia ?: 0
        gia * gioHang.SoLuong
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Giỏ hàng", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
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
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                !isLoggedIn -> {
                    // Hiển thị màn hình yêu cầu đăng nhập
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
                            text = "Vui lòng đăng nhập để xem giỏ hàng",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { 
                                navController.navigate(Screens.Login_Screens.route)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            modifier = Modifier.fillMaxWidth(0.7f)
                        ) {
                            Text("Đăng nhập")
                        }
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
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { 
                                errorMessage = null
                                hasLoadedCart = false // Reset để thử lại
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Thử lại")
                        }
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        if (cartItems.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Giỏ hàng của bạn đang trống",
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(cartItems) { gioHang ->
                                    val sanPham = allProducts.find { it.MaSanPham == gioHang.MaSanPham }
                                    if (sanPham != null) {
                                        CartItemCard(
                                            gioHang = gioHang,
                                            sanPham = sanPham,
                                            onQuantityChange = { newQuantity ->
                                                if (newQuantity <= 0) {
                                                    gioHangViewModel.deleteGioHang(gioHang.MaGioHang)
                                                } else if (newQuantity <= sanPham.SoLuong) {
                                                    val updatedGioHang = gioHang.copy(SoLuong = newQuantity)
                                                    gioHangViewModel.updateGioHang(updatedGioHang)
                                                }
                                            }
                                        )
                                    }
                                }
                            }

                            // Phần tổng tiền và nút thanh toán
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
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
                                        if (cartItems.isNotEmpty()) {
                                            val cartItemsJson = Json.encodeToString(cartItems)
                                            val encodedCartItems = URLEncoder.encode(cartItemsJson, StandardCharsets.UTF_8.toString())
                                            navController.navigate("checkout/${totalPrice}/${encodedCartItems}")
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                    shape = RoundedCornerShape(8.dp),
                                    enabled = cartItems.isNotEmpty()
                                ) {
                                    Text(
                                        text = "Thanh toán",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    gioHang: GioHang,
    sanPham: SanPham,
    onQuantityChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = sanPham.HinhAnh,
                    contentDescription = sanPham.TenSanPham,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
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
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onQuantityChange(gioHang.SoLuong - 1) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease quantity",
                            tint = Color.Black
                        )
                    }
                    Text(
                        text = gioHang.SoLuong.toString(),
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    IconButton(
                        onClick = { 
                            if (gioHang.SoLuong < sanPham.SoLuong) {
                                onQuantityChange(gioHang.SoLuong + 1)
                            }
                        },
                        modifier = Modifier.size(32.dp),
                        enabled = gioHang.SoLuong < sanPham.SoLuong
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase quantity",
                            tint = if (gioHang.SoLuong < sanPham.SoLuong) Color.Black else Color.Gray
                        )
                    }
                }
            }
            IconButton(
                onClick = { onQuantityChange(0) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove item",
                    tint = Color.Red
                )
            }
        }
    }
}
