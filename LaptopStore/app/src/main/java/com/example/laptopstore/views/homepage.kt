package com.example.laptopstore.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.app_e_commerce.model.BottomNavItem
import com.example.laptopstore.R
import com.example.laptopstore.models.SanPham
import com.example.laptopstore.models.Screens
import com.example.laptopstore.models.HinhAnh
import com.example.laptopstore.viewmodels.HinhAnhViewModel
import com.example.laptopstore.viewmodels.SanPhamViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HOMEPAGE(navController: NavHostController, sanPhamViewModel: SanPhamViewModel = viewModel(), hinhAnhViewModel: HinhAnhViewModel = viewModel()) {
    var sortOption by remember { mutableStateOf("Phổ biến") }
    val products by sanPhamViewModel.danhSachAllSanPham.collectAsState(initial = emptyList())

    // Lấy danh sách hình ảnh từ ViewModel
    var productImages by remember { mutableStateOf<List<HinhAnh>>(emptyList()) }
    if (sanPhamViewModel.isLoading) {
        Text("Đang tải...", modifier = Modifier.padding(16.dp))
    }
    sanPhamViewModel.errorMessage?.let { error ->
        Text("Lỗi: $error", color = Color.Red, modifier = Modifier.padding(16.dp))
    }
    LaunchedEffect(Unit) {

        sanPhamViewModel.getAllSanPham() // Lấy danh sách sản phẩm từ API
        val response = hinhAnhViewModel.getAllHinhAnh() // Lấy tất cả hình ảnh
        productImages = response.hinhanh
    }

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
            items(sortedProducts(products, sortOption).chunked(2)) { productRow ->
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
fun ProductCard(product: SanPham, navController: NavHostController, images: List<HinhAnh>, modifier: Modifier = Modifier) {
    val defaultImage = images.find { it.MacDinh == 1 }?.DuongDan ?: product.HinhAnh
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
                    text = "${product.Gia / 1000}.000 VNĐ",
                    fontSize = 16.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun sortedProducts(products: List<SanPham>, sortOption: String): List<SanPham> {
    return when (sortOption) {
        "Giá thấp đến cao" -> products.sortedBy { it.Gia }
        "Giá cao đến thấp" -> products.sortedByDescending { it.Gia }
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

    var selectedItem by remember { mutableIntStateOf(0) }

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