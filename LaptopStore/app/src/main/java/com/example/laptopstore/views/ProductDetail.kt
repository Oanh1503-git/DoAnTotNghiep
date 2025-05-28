package com.example.laptopstore.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
fun ProductDetail(navController: NavHostController, productId: Int) {
    // Giả lập dữ liệu sản phẩm từ database
    val product = remember {
        listOf(
            Product(
                id = 1,
                name = "ASUS Vivobook Go 15 E1504FA",
                price = 12500000,
                imageUrl = "https://i.postimg.cc/KjV6SpNP/asus-vivobook-go15.jpg",
                discount = 0,
                specs = "AMD Ryzen 5 7520U, 16GB LPDDR5, AMD Radeon Graphics, 512GB PCIe NVMe SSD, 15.6 inch FHD (1920x1080)"
            ),
            Product(
                id = 2,
                name = "Dell Inspiron 15 3520",
                price = 15000000,
                imageUrl = "https://i.postimg.cc/Bn0Bm99x/dell-inspiron-15-3520.jpg",
                discount = 0,
                specs = "Intel Core i5-1235U, 8GB DDR4, Intel Iris Xe Graphics, 512GB PCIe NVMe SSD, 15.6 inch FHD (1920x1080) 120Hz"
            ),
            Product(
                id = 3,
                name = "HP 15-fc0085AU R5",
                price = 11500000,
                imageUrl = "https://i.postimg.cc/DyVrzkzt/hp-15-fc0085au-r5.jpg",
                discount = 0,
                specs = "AMD Ryzen 5 7520U, 8GB LPDDR5, AMD Radeon Graphics, 512GB PCIe NVMe SSD, 15.6 inch FHD (1920x1080)"
            ),
            Product(
                id = 4,
                name = "MacBook Air 13 inch M4 (Dự kiến)",
                price = 32000000,
                imageUrl = "https://i.postimg.cc/52b8wBLg/macbook-air-m4.jpg",
                discount = 0,
                specs = "Apple M4 chip, 8GB unified memory, Apple M4 GPU (dự kiến), 256GB SSD, 13.6 inch Liquid Retina"
            )
        ).find { it.id == productId } ?: Product(0, "", 0, "", 0, "")
    }

    // Giả lập danh sách ảnh từ bảng hinhanh
    val productImages = remember {
        listOf(
            ProductImage(1, "https://i.postimg.cc/KjV6SpNP/asus-vivobook-go15.jpg", true),
            ProductImage(2, "https://i.postimg.cc/jqBXJnM3/asus-vivobook-go15.jpg", false),
            ProductImage(3, "https://i.postimg.cc/rwNCbnM7/asus-vivobook-go15.jpg", false),
            ProductImage(4, "https://i.postimg.cc/X7FgKNhj/asus-vivobook-go15.jpg", false),
            ProductImage(5, "https://i.postimg.cc/Bn0Bm99x/dell-inspiron-15-3520.jpg", true),
            ProductImage(6, "https://i.postimg.cc/q7mcBNtL/dell-inspiron-15-3520.jpg", false),
            ProductImage(7, "https://i.postimg.cc/fLJcVMmh/dell-inspiron-15-3520.jpg", false),
            ProductImage(8, "https://i.postimg.cc/wBxcdXBM/dell-inspiron-15-3520.jpg", false),
            ProductImage(9, "https://i.postimg.cc/DyVrzkzt/hp-15-fc0085au-r5.jpg", true),
            ProductImage(10, "https://i.postimg.cc/DyVrzkzt/hp-15-fc0085au-r5.jpg", false),
            ProductImage(11, "https://i.postimg.cc/RhtwTGgs/hp-15-fc0085au-r5.jpg", false),
            ProductImage(12, "https://i.postimg.cc/dQZRvtJ2/hp-15-fc0085au-r5.jpg", false),
            ProductImage(13, "https://i.postimg.cc/52b8wBLg/macbook-air-m4.jpg", true),
            ProductImage(14, "https://i.postimg.cc/VNHMQ2TZ/macbook-air-m4.jpg", false),
            ProductImage(15, "https://i.postimg.cc/6pcnz8hX/macbook-air-m4.jpg", false),
            ProductImage(16, "https://i.postimg.cc/kXxx616D/macbook-air-m4.jpg", false)
        ).filter { it.id / 4 == (productId - 1) }
    }

    // Giả lập danh sách bình luận từ bảng binhluandanhgia
    val reviews = remember {
        listOf(
            Review(1, 1, 5, "Laptop rất mượt, hiệu năng tốt, đáng giá tiền!", "2025-05-10"),
            Review(2, 1, 4, "Máy đẹp, nhưng pin hơi yếu so với kỳ vọng.", "2025-05-09"),
            Review(3, 2, 5, "Hiệu năng tuyệt vời, màn hình sắc nét!", "2025-05-08"),
            Review(4, 2, 3, "Máy hơi nóng khi chạy lâu.", "2025-05-07"),
            Review(5, 3, 4, "Thiết kế đẹp, gọn nhẹ, phù hợp di chuyển.", "2025-05-06"),
            Review(6, 3, 5, "Rất hài lòng, giá cả hợp lý!", "2025-05-05"),
            Review(7, 4, 5, "MacBook đúng chuẩn Apple, tuyệt vời!", "2025-05-04"),
            Review(8, 4, 4, "Hiệu năng mạnh, nhưng giá hơi cao.", "2025-05-03")
        ).filter { it.productId == productId }
    }

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Mô tả", "Thông số", "Đánh giá")

    // Giả lập customerId (cần lấy từ phiên đăng nhập thực tế)
    val customerId = 1

    // Trạng thái cho ảnh được chọn
    var selectedImage by remember { mutableStateOf(productImages.find { it.isDefault }?.url ?: product.imageUrl) }
    val pagerState = rememberPagerState { productImages.size }

    // Coroutine scope để gọi scrollToPage
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = product.name, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
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
                    IconButton(onClick = { FavoriteManager.toggleFavorite(product.id, customerId) }) {
                        Icon(
                            imageVector = if (FavoriteManager.isFavorite(product.id)) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Yêu thích",
                            tint = if (FavoriteManager.isFavorite(product.id)) Color.Red else Color.Gray
                        )
                    }
                }
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
                // Phần hiển thị ảnh
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
                                model = productImages[page].url,
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
                                    .background(if (productImages[index].url == selectedImage) Color.Red else Color.White)
                                    .clickable {
                                        selectedImage = productImages[index].url
                                        coroutineScope.launch {
                                            pagerState.scrollToPage(index)
                                        }
                                    }
                            ) {
                                AsyncImage(
                                    model = productImages[index].url,
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
                    text = product.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    Text(
                        text = "${(product.price - ((product.price * product.discount) / 100)) / 1000}.000 VNĐ",
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            CartManager.addToCart(product)
                            navController.navigate(Screens.CARTSCREENS.route)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .padding(end = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        shape = RoundedCornerShape(8.dp),

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
                            val cartItems = listOf(CartItem(product, 1))
                            val cartItemsJson = Json.encodeToString(cartItems)
                            val encodedCartItems = URLEncoder.encode(cartItemsJson, StandardCharsets.UTF_8.toString())
                            navController.navigate("checkout/${product.price}/${encodedCartItems}")
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
            }
            item {
                when (selectedTabIndex) {
                    0 -> {
                        Text(
                            text = "Mô tả sản phẩm: ${product.name} là một chiếc laptop hiệu suất cao, được thiết kế dành cho ${if (product.discount > 0) "khuyến mãi đặc biệt" else "mọi nhu cầu"}. Với cấu hình ${product.specs}, sản phẩm này phù hợp cho công việc và giải trí.",
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
                            val specs = product.specs.split(", ")
                            Text(text = "CPU: ${specs.getOrNull(0) ?: "-"}", fontSize = 14.sp)
                            Text(text = "RAM: ${specs.getOrNull(1) ?: "-"}", fontSize = 14.sp)
                            Text(text = "Card màn hình: ${specs.getOrNull(2) ?: "-"}", fontSize = 14.sp)
                            Text(text = "Ổ cứng: ${specs.getOrNull(3) ?: "-"}", fontSize = 14.sp)
                            Text(text = "Màn hình: ${specs.getOrNull(4) ?: "-"}", fontSize = 14.sp)
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
                                            repeat(review.rating) {
                                                Icon(
                                                    imageVector = Icons.Default.Star,
                                                    contentDescription = "Sao",
                                                    tint = Color(0xFFFFD700),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                            repeat(5 - review.rating) {
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
                                            text = review.content,
                                            fontSize = 14.sp,
                                            color = Color.Black
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Ngày: ${review.date}",
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