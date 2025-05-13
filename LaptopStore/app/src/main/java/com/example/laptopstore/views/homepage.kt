package com.example.laptopstore.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app_e_commerce.model.BottomNavItem
import com.example.laptopstore.R
import com.example.laptopstore.models.Screens
import kotlinx.coroutines.delay
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import coil.compose.AsyncImage
import com.example.laptopstore.models.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HOMEPAGE(navController: NavHostController) {
    var sortOption by remember { mutableStateOf("Phổ biến") }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                actions = { SearchField() }
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
                BannerSection()
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sản phẩm đề xuất",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Button(
                        onClick = {
                            sortOption = when (sortOption) {
                                "Phổ biến" -> "Giá thấp đến cao"
                                "Giá thấp đến cao" -> "Giá cao đến thấp"
                                else -> "Phổ biến"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                            .height(36.dp)
                    ) {
                        Text(text = sortOption, fontSize = 14.sp)
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = "Sort",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            items(sortedProducts(sortOption).chunked(2)) { productRow ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp), // Thêm padding dọc cho mỗi hàng
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // Khoảng cách ngang giữa các card
                ) {
                    productRow.forEach { product ->
                        ProductCard(
                            product = product,
                            navController = navController,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp) // Padding ngang trong mỗi card
                        )
                    }
                    // Nếu chỉ có 1 sản phẩm trong hàng, thêm Spacer để cân đối
                    if (productRow.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun BannerSection() {
    val images = listOf(
        R.drawable.anh1,
        R.drawable.anh2,
        R.drawable.anh3
    )

    val pagerState = rememberPagerState { images.size }

    Column(
        modifier = Modifier.fillMaxWidth().background(Color.Transparent)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth().height(180.dp)
        ) { page ->
            Image(
                painter = painterResource(id = images[page]),
                contentDescription = "Banner $page",
                modifier = Modifier.fillMaxSize().padding(8.dp),
                contentScale = ContentScale.Crop
            )
        }
    }

    LaunchedEffect(pagerState) {
        while (true) {
            delay(3000)
            pagerState.animateScrollToPage((pagerState.currentPage + 1) % images.size)
        }
    }
}


@Composable
fun CategoryCard(category: Category, navController: NavHostController) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .clickable {
                // Điều hướng đến danh mục cụ thể
                navController.navigate(Screens.CATAGORIES.route)
            },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = category.imageUrl,
                contentDescription = category.name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Text(
                text = category.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun ProductCard(product: Product, navController: NavHostController, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .clickable {
                navController.navigate("product_detail/${product.id}")
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
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if (product.discount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(4.dp)
                            .background(Color.Red, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "-${product.discount}%",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Text(
                text = product.name,
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
                    text = "${(product.price-((product.price * product.discount) / 100) )/ 1000}.000 VNĐ",
                    fontSize = 16.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
                if (product.discount > 0) {
                    Text(
                        text = "${product.price / 1000}.000 VNĐ",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textDecoration = TextDecoration.LineThrough
                    )
                }
            }
        }
    }
}



data class Category(
    val name: String,
    val imageUrl: String
)

private val products = listOf(
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
)

private fun sortedProducts(sortOption: String): List<Product> {
    return when (sortOption) {
        "Giá thấp đến cao" -> products.sortedBy { (it.price-((it.price * it.discount) / 100) ) }
        "Giá cao đến thấp" -> products.sortedByDescending { (it.price-((it.price * it.discount) / 100) ) }
        else -> products
    }
}

@Composable
fun SearchField() {
    var searchText by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .shadow(2.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                BasicTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                    decorationBox = { innerTextField ->
                        if (searchText.isEmpty()) {
                            Text(
                                text = "Tìm kiếm sản phẩm",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                )
            }
        }
        Button(
            onClick = { /* Xử lý tìm kiếm */ },
            modifier = Modifier
                .padding(start = 8.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                text = "Tìm kiếm",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun MenuBottomNavBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, Screens.HOMEPAGE.route),
        BottomNavItem("Categories", Icons.AutoMirrored.Filled.List, Screens.CATAGORIES.route),
        BottomNavItem("Cart", Icons.Default.ShoppingCart, Screens.CARTSCREENS.route),
        BottomNavItem("Account", Icons.Default.Person, Screens.ACCOUNTSCREENS.route)
    )

    var selectedItem by remember { mutableStateOf(0) }

    NavigationBar(containerColor = Color.White) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp),
                        tint = if (selectedItem == index) Color.Black else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 12.sp,
                        color = if (selectedItem == index) Color.Black else Color.Gray
                    )
                },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item.route) {
                        popUpTo(Screens.HOMEPAGE.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}