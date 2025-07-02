package com.example.laptopstore.views

import android.content.ClipData.Item
import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.laptopstore.api.SeachSanphamResponse
import com.example.laptopstore.models.Screens
import com.example.laptopstore.viewmodels.GioHangViewModel
import com.example.laptopstore.viewmodels.HinhAnhViewModel
import com.example.laptopstore.viewmodels.SanPhamViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import com.example.laptopstore.models.SanPham
import com.example.laptopstore.models.HinhAnh
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.layout.ContentScale
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeachSanphamScreen(navController: NavHostController,
                       sanPhamViewModel: SanPhamViewModel = viewModel(),
                       hinhAnhViewModel: HinhAnhViewModel = viewModel(),
                       gioHangViewModel: GioHangViewModel = viewModel()
){

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "Kết quả tìm kiếm", fontWeight = FontWeight.Bold, fontSize = 18.sp)},
                navigationIcon ={
                    IconButton(onClick = {
                        navController.navigate(Screens.CATAGORIES.route)
                    }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }

                }
            )
        }

    ){
        innerPadding->
        val searchResultsState = sanPhamViewModel.danhSach.collectAsState(initial = null)
        val searchResults=searchResultsState.value
        var productImages = remember { mutableStateListOf<HinhAnh>() }
        val isLoading = sanPhamViewModel.isLoading
        val errorMessage = sanPhamViewModel.errorMessage
        val currencyFormatter = NumberFormat.getInstance(Locale("vi", "VN"))
        LaunchedEffect(Unit) {
            val response = hinhAnhViewModel.getAllHinhAnh()
            productImages.clear()
            productImages.addAll(response.hinhanh ?: emptyList())

        }
        LazyColumn (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
        ){
            if (isLoading) {
                item {
                    Text("Đang tải...", modifier = Modifier.padding(16.dp))
                }
            }
            errorMessage?.let { error ->
                item {
                    Text("Lỗi: $error", color = Color.Red, modifier = Modifier.padding(16.dp))
                }
            }
            if ((searchResults == null || searchResults.isEmpty()) && !isLoading) {
                item {
                    Text(
                        text = "Không tìm thấy sản phẩm nào",
                        modifier = Modifier.padding(16.dp),
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                items((searchResults?: emptyList()).chunked(2)) { productRow ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        productRow.forEach { product ->
                            ProductCard(
                                product = product,
                                navController = navController,
                                images = productImages.filter { it.MaSanPham == product.MaSanPham },
                                modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                            )

                        }
                        if (productRow.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: SanPham,
    navController: NavHostController,
    images: List<HinhAnh>,
    modifier: Modifier = Modifier
) {
    val defaultImage = images.find { it.MacDinh == 1 }?.DuongDan ?: (product.HinhAnh ?: "")

    val currencyFormatter = NumberFormat.getInstance(Locale("vi", "VN")).apply {
        maximumFractionDigits = 0 // Không hiển thị phần thập phân
    }
    Card(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .clickable {
                navController.navigate("product_detail/${product.MaSanPham}")
            },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = defaultImage,
                    contentDescription = product.TenSanPham,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                text = product.TenSanPham,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .height(40.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = "${currencyFormatter.format(product.Gia)} VNĐ",
                    fontSize = 16.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
