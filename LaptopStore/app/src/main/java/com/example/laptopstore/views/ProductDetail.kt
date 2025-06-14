package com.example.laptopstore.views

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.laptopstore.models.BinhLuanDanhGia
import com.example.laptopstore.models.GioHang
import com.example.laptopstore.models.HinhAnh
import com.example.laptopstore.models.SanPham
import com.example.laptopstore.models.Screens
import com.example.laptopstore.viewmodels.BinhLuanViewModel
import com.example.laptopstore.viewmodels.DataStoreManager
import com.example.laptopstore.viewmodels.GioHangViewModel
import com.example.laptopstore.viewmodels.HinhAnhViewModel
import com.example.laptopstore.viewmodels.KhachHangViewModels
import com.example.laptopstore.viewmodels.SanPhamViewModel
import com.example.laptopstore.viewmodels.SanPhamYeuThichViewModel
import com.example.laptopstore.viewmodels.TaiKhoanViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat

import java.util.Date
import java.util.Locale
import java.util.*
data class ProductImage(
    val id: Int,
    val url: String,
    val isDefault: Boolean
)

data class Review(
    val id: Int,
    val productId: Int,
    val rating: Int,
    val content: String,
    val date: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetail(
    navController: NavHostController,
    productId: Int,
    sanPhamViewModel: SanPhamViewModel = viewModel(),
    hinhAnhViewModel: HinhAnhViewModel = viewModel(),
    binhLuanViewModel: BinhLuanViewModel = viewModel(),
    sanPhamYeuThichViewModel: SanPhamYeuThichViewModel = viewModel(),
    gioHangViewModel: GioHangViewModel = viewModel(),
    taiKhoanViewModel: TaiKhoanViewModel = viewModel(),
) {
    val product by sanPhamViewModel::sanPham
    val images by hinhAnhViewModel::danhSachHinhAnhTheoSanPham
    val reviews by binhLuanViewModel.reviewsByProductId.collectAsState(initial = emptyList())
    val isFavorite by sanPhamYeuThichViewModel.isFavorite.collectAsState(initial = false)
    val giohangAddResult by gioHangViewModel.giohangAddResult.collectAsState(initial = "")
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    // Collect StateFlow values
    val taikhoan by taiKhoanViewModel.taikhoan.collectAsState()
    val isLoggedIn by taiKhoanViewModel.isLoggedIn.collectAsState()
    val khachHang by taiKhoanViewModel.khachHang.collectAsState()

    // Get MaKhachHang safely

    var isAddingToCart by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showLoginDialog by remember { mutableStateOf(false) }
    // State cho form bình luận
    var commentText by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(5) }
    var isSubmitting by remember { mutableStateOf(false) }
    // Debug logging
    val dataStoreManager = remember { DataStoreManager(context) }
    val customerId by dataStoreManager.customerId.collectAsState(initial = null)
    val maKhachHang = customerId
    LaunchedEffect(taikhoan) {
        Log.d("ProductDetail", "TaiKhoan changed: $taikhoan")
        Log.d("ProductDetail", "MaKhachHang: $maKhachHang")
    }

    LaunchedEffect(customerId){
        Log.d("ProductDetail","$customerId")
        if(customerId.isNullOrEmpty()){
            Log.d("ProductDetail", "MaKhachHang is null or empty")
        }else{
            Log.d("ProductDetail", "co MaKhachHang ")
        }
    }
    // Load initial data
    LaunchedEffect(productId, maKhachHang) {
        isLoading = true
        errorMessage = null
        try {
            sanPhamViewModel.getSanPhamById(productId.toString())
            hinhAnhViewModel.getHinhAnhTheoSanPham(productId)
            binhLuanViewModel.getReviewsByProductId(productId)
            
            // Only attempt to access MaKhachHang when it's not null
            if (!maKhachHang.isNullOrEmpty()) {
                Log.d("ProductDetail", "Loading data for MaKhachHang: $maKhachHang")
                sanPhamYeuThichViewModel.checkFavorite(productId, maKhachHang)
                gioHangViewModel.getGioHangByKhachHang(maKhachHang)
            } else {
                Log.d("ProductDetail", "MaKhachHang is null or empty")
            }
        } catch (e: Exception) {
            errorMessage = "Lỗi khi tải dữ liệu: ${e.message}"
            Log.e("ProductDetail", "Error: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    val productOrDefault = product ?: SanPham(
        MaSanPham = 0,
        TenSanPham = "",
        MaLoaiSanPham = 0,
        mathuonghieu = 0,
        CPU = "",
        RAM = "",
        CardManHinh = "",
        SSD = "",
        ManHinh = "",
        MaMauSac = 0,
        Gia = 1,
        SoLuong = 0,
        MoTa = "",
        HinhAnh = "",
        TrangThai = 0
    )

    val productImages = images
    val selectedImage = productImages.find { it.MacDinh == 1 }?.DuongDan ?: productOrDefault.HinhAnh
    var currentImage by remember { mutableStateOf(selectedImage) }
    val pagerState = rememberPagerState { productImages.size }
    val coroutineScope = rememberCoroutineScope()

    // Dialog yêu cầu đăng nhập
    if (showLoginDialog) {
        AlertDialog(
            onDismissRequest = { showLoginDialog = false },
            title = { Text("Thông báo") },
            text = { 
                Column {
                    Text("Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng")
                    if (!maKhachHang.isNullOrEmpty()) {
                        Text(
                            text = "Đã đăng nhập với tài khoản: ${taikhoan?.TenTaiKhoan}",
                            color = Color.Green,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (maKhachHang.isNullOrEmpty()) {
                            showLoginDialog = false
                            navController.navigate(Screens.Login_Screens.route)
                        } else {
                            showLoginDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (maKhachHang.isNullOrEmpty()) Color.Red else Color.Green
                    )
                ) {
                    Text(if (maKhachHang.isNullOrEmpty()) "Đăng nhập" else "Đã đăng nhập")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showLoginDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Đóng")
                }
            }
        )
    }

    LaunchedEffect(giohangAddResult) {
        if (giohangAddResult.isNotEmpty()) {
            isAddingToCart = false
            Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = productOrDefault.TenSanPham,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (!maKhachHang.isNullOrEmpty()) {
                            sanPhamYeuThichViewModel.toggleFavorite(productId, maKhachHang!!)
                        } else {
                            showLoginDialog = true
                        }
                    }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isFavorite) "Bỏ yêu thích" else "Yêu thích",
                            tint = if (isFavorite) Color.Red else Color.Gray
                        )
                    }
                }
            )
        },
        bottomBar = {
            MenuBottomNavBar(navController) // Giả định đã định nghĩa
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = errorMessage ?: "Lỗi không xác định", fontSize = 16.sp, color = Color.Red)
            }
        } else if (productOrDefault.MaSanPham == 0) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Không tìm thấy sản phẩm", fontSize = 16.sp, color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color.White),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .shadow(4.dp, RoundedCornerShape(8.dp))
                        ) {
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxSize()
                            ) { page ->
                                AsyncImage(
                                    model = productImages.getOrNull(page)?.DuongDan ?: productOrDefault.HinhAnh,
                                    contentDescription = "Hình ảnh sản phẩm ${page + 1}",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(productImages.size) { index ->
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            if (productImages[index].DuongDan == currentImage) Color.Red else Color.White
                                        )
                                        .clickable {
                                            currentImage = productImages[index].DuongDan
                                            coroutineScope.launch {
                                                pagerState.scrollToPage(index)
                                            }
                                        }
                                ) {
                                    AsyncImage(

                                        model = productImages[index].DuongDan,
                                        contentDescription = "Hình thu nhỏ ${index + 1}",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = productOrDefault.TenSanPham,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${NumberFormat.getInstance(Locale("vi", "VN")).format(productOrDefault.Gia)} VNĐ",
                        fontSize = 22.sp,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                if (maKhachHang.isNullOrEmpty()) {
                                    showLoginDialog = true
                                    Log.d("ProductDetail", "Showing login dialog - MaKhachHang is null or empty")
                                } else {
                                    Log.d("", "Adding to cart with MaKhachHang: $maKhachHang")
                                    val gioHang = GioHang(
                                        MaGioHang = 0,
                                        MaSanPham = productOrDefault.MaSanPham,
                                        MaKhachHang = maKhachHang,
                                        SoLuong = 1,
                                        TrangThai = 1
                                    )
                                    gioHangViewModel.addToCart(gioHang)
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .padding(end = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Thêm vào giỏ hàng",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Button(
                            onClick = {
                                if (maKhachHang.isNullOrEmpty()) {
                                    showLoginDialog = true
                                } else {
                                    navController.navigate("checkoutScreen/${productOrDefault.Gia}")
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .padding(start = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Mua ngay",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    var selectedTabIndex by remember { mutableStateOf(0) }
                    val tabs = listOf("Mô tả", "Thông số", "Đánh giá")
                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = { Text(text = title, fontSize = 16.sp) }
                            )
                        }
                    }
                    when (selectedTabIndex) {
                        0 -> {
                            Text(
                                text = productOrDefault.MoTa,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                        1 -> {
                            Column(modifier = Modifier.padding(top = 16.dp)) {
                                Text(
                                    text = "Thông số kỹ thuật",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "CPU: ${productOrDefault.CPU}", fontSize = 14.sp)
                                Text(text = "RAM: ${productOrDefault.RAM}", fontSize = 14.sp)
                                Text(text = "Card màn hình: ${productOrDefault.CardManHinh}", fontSize = 14.sp)
                                Text(text = "Ổ cứng: ${productOrDefault.SSD}", fontSize = 14.sp)
                                Text(text = "Màn hình: ${productOrDefault.ManHinh}", fontSize = 14.sp)
                            }
                        }
                        2 -> {
                            Column(modifier = Modifier.padding(top = 16.dp)) {
                                Text(
                                    text = "Đánh giá sản phẩm",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                // Form thêm bình luận
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = "Thêm đánh giá của bạn",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        (1..5).forEach { star ->
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = "Sao $star",
                                                tint = if (star <= rating) Color(0xFFFFD700) else Color.Gray,
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .clickable { rating = star }
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = commentText,
                                        onValueChange = { commentText = it },
                                        label = { Text("Nhập bình luận") },
                                        modifier = Modifier.fillMaxWidth(),
                                        maxLines = 3
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = {
                                            if (commentText.isNotBlank() && !isSubmitting) {
                                                isSubmitting = true
                                                val review = BinhLuanDanhGia(
                                                    MaBinhLuan = 1,
                                                    MaKhachHang = maKhachHang ?: "",
                                                    MaSanPham = productId,
                                                    MaHoaDonBan = 19, // Giả định chưa có hóa đơn
                                                    SoSao = rating,
                                                    NoiDung = commentText,
                                                    NgayDanhGia = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                                                    TrangThai = 1
                                                )
                                                binhLuanViewModel.createReview(review)
                                                commentText = ""
                                                rating = 5
                                                Toast.makeText(context, "Đã gửi bình luận", Toast.LENGTH_SHORT).show()
                                                isSubmitting = false
                                            } else if (commentText.isBlank()) {
                                                Toast.makeText(context, "Vui lòng nhập bình luận", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                        shape = RoundedCornerShape(8.dp),
                                        enabled = !isSubmitting
                                    ) {
                                        Text(
                                            text = "Gửi đánh giá",
                                            color = Color.White,
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                // Danh sách bình luận
                                if (reviews.isEmpty()) {
                                    Text(
                                        text = "Chưa có đánh giá nào cho sản phẩm này.",
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                } else {
                                    reviews.forEach { review ->
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp)
                                                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                                .padding(12.dp)
                                        ) {
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                repeat(review.SoSao) {
                                                    Icon(
                                                        imageVector = Icons.Default.Star,
                                                        contentDescription = "Sao",
                                                        tint = Color(0xFFFFD700),
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                                repeat(5 - review.SoSao) {
                                                    Icon(
                                                        imageVector = Icons.Default.Star,
                                                        contentDescription = "Sao",
                                                        tint = Color.Gray,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = review.NoiDung ?: "Không có nội dung",
                                                fontSize = 14.sp,
                                                color = Color.Black
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Ngày: ${
                                                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                                                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(review.NgayDanhGia) ?: Date()
                                                    )
                                                }",
                                                fontSize = 12.sp,
                                                color = Color.Gray
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
    }
}
