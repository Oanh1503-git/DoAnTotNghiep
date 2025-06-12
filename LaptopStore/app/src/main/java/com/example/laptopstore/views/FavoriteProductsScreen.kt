package com.example.laptopstore.views


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.laptopstore.models.HienSanPhamYeuThich
import com.example.laptopstore.models.SanPhamYeuThich
import com.example.laptopstore.viewmodels.SanPhamYeuThichViewModel
import com.example.laptopstore.viewmodels.TaiKhoanViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteProductsScreen(
    navController: NavHostController,
    sanPhamYeuThichViewModel: SanPhamYeuThichViewModel,
    taiKhoanViewModel: TaiKhoanViewModel
) {
    val showfavorites by sanPhamYeuThichViewModel.showfavorites.collectAsState()
    val khachHang by taiKhoanViewModel.khachHang.collectAsState()
    val context = LocalContext.current
    val isLoading by sanPhamYeuThichViewModel.isFavorite.collectAsState()
    val errorMessage by sanPhamYeuThichViewModel.errorMessage.collectAsState()

    // Load favorites when MaKhachHang is available
    LaunchedEffect(khachHang) {
        khachHang?.MaKhachHang?.let { maKhachHang ->
            sanPhamYeuThichViewModel.getFavoritesByKhachHang(maKhachHang)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sản Phẩm Yêu Thích", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage ?: "Lỗi không xác định",
                        fontSize = 16.sp,
                        color = Color.Red
                    )
                }
            }
            showfavorites.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Không có sản phẩm yêu thích nào.",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(showfavorites) { showfavorite ->
                        FavoriteProductItem(
                            showfavorite = showfavorite,
                            onClick = {
                                navController.navigate("product_detail/${showfavorite.MaSanPham}")
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteProductItem(showfavorite: HienSanPhamYeuThich, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = showfavorite.HinhAnh,
                contentDescription = showfavorite.TenSanPham,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = showfavorite.TenSanPham ?: "Không có tên",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = showfavorite.Gia?.let {
                        "${NumberFormat.getInstance(Locale("vi", "VN")).format(it)} VNĐ"
                    } ?: "Không có giá",
                    fontSize = 14.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}