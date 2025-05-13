package com.example.laptopstore.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.laptopstore.models.Product
import com.example.laptopstore.models.Screens

// Danh sách tạm thời để lưu giỏ hàng (sẽ thay bằng ViewModel hoặc database)
// thêm nút thêm sản phẩm yêu thích
// thêm hiện thông tin chi tiết sản phẩm ( hiện thông số )
//thêm nút mua ngay ( chuyển qua giao diện thanh toán ) có thể làm sau hoàn toàn bộ giao diện rồi tiếp tục
// hiện bình luận về sản phảm cuối trang ( phần này chưa qua trong có thể làm sau)
object CartManager {
    var cartItems by mutableStateOf(listOf<CartItem>())

    fun addToCart(product: Product) {
        val existingItem = cartItems.find { it.product.id == product.id }
        if (existingItem != null) {
            cartItems = cartItems.map {
                if (it.product.id == product.id) {
                    it.copy(quantity = it.quantity + 1)
                } else {
                    it
                }
            }
        } else {
            cartItems = cartItems + CartItem(product, 1)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetail(navController: NavHostController, productId: Int) {
    val product = remember {
        listOf(
            Product(
                id = 1,
                name = "Laptop gaming ASUS TUF Gaming F15",
                price = 29990000,
                imageUrl = "https://i.postimg.cc/KjV6SpNP/asus-vivobook-go15.jpg",
                discount = 10,
                specs = "Intel Core i5-11400H, RAM 16GB, SSD 512GB, NVIDIA GTX 1650 4GB"
            ),
            Product(
                id = 2,
                name = "Laptop MSI Modern 14",
                price = 8990000,
                imageUrl = "https://i.postimg.cc/RVZxD87n/msi-modern-15.jpg",
                discount = 0,
                specs = "Intel Core i3-1115G4, RAM 8GB, SSD 256GB, Intel UHD Graphics"
            ),
            Product(
                id = 3,
                name = "Laptop ASUS Vivobook 16X",
                price = 17290000,
                imageUrl = "https://i.postimg.cc/DyVrzkzt/hp-15-fc0085au-r5.jpg",
                discount = 5,
                specs = "AMD Ryzen 5 5600H, RAM 16GB, SSD 512GB, AMD Radeon Graphics"
            ),
            Product(
                id = 4,
                name = "Laptop ASUS Vivobook 14 OLED",
                price = 17390000,
                imageUrl = "https://i.postimg.cc/52b8wBLg/macbook-air-m4.jpg",
                discount = 0,
                specs = "Intel Core i7-1165G7, RAM 8GB, SSD 1TB, Intel Iris Xe"
            )
        ).find { it.id == productId } ?: Product(0, "", 0, "", 0, "")
    }

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Mô tả", "Đánh giá")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = product.name, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                actions = { }
            )
        },
        bottomBar = {
            MenuBottomNavBar(navController)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shadow(4.dp, RoundedCornerShape(8.dp))
                ) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = product.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    Text(
                        text = "${(product.price-((product.price * product.discount) / 100) )/ 1000}.000 VNĐ",
                        fontSize = 22.sp,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                    if (product.discount > 0) {
                        Text(
                            text = "${product.price / 1000}.000 VNĐ",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Thông số kỹ thuật: ${product.specs}",
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        CartManager.addToCart(product)
                        navController.navigate(Screens.CARTSCREENS.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
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
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
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
                            text = "Mô tả sản phẩm: ${product.name} là một chiếc laptop hiệu suất cao, được thiết kế dành cho ${if (product.discount > 0) "khuyến mãi đặc biệt" else "mọi nhu cầu"}. Với cấu hình ${product.specs}, sản phẩm này phù hợp cho công việc và giải trí.",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                    1 -> {
                        Text(
                            text = "Đánh giá: Sản phẩm nhận được 4.5/5 sao từ người dùng. Khách hàng đánh giá cao hiệu năng và thiết kế.",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

