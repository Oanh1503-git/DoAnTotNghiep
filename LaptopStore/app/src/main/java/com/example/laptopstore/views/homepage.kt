package com.example.laptopstore.views

import android.util.Log
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.app_e_commerce.model.BottomNavItem
import com.example.laptopstore.R
import com.example.laptopstore.models.SanPham
import com.example.laptopstore.models.Screens
import com.example.laptopstore.models.HinhAnh
import com.example.laptopstore.viewmodels.HinhAnhViewModel
import com.example.laptopstore.viewmodels.SanPhamViewModel
import com.example.laptopstore.viewmodels.GioHangViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HOMEPAGE(navController: NavHostController,
             sanPhamViewModel: SanPhamViewModel = viewModel(),
             hinhAnhViewModel: HinhAnhViewModel = viewModel(),
             gioHangViewModel: GioHangViewModel= viewModel()) {
    var sortOption by remember { mutableStateOf("Phổ biến") }
    val products by sanPhamViewModel.danhSachAllSanPham.collectAsState(initial = emptyList())
    val searchResults by sanPhamViewModel.danhSach.collectAsState(initial = emptyList())
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    // Lấy danh sách hình ảnh từ ViewModel
    var productImages by remember { mutableStateOf<List<HinhAnh>>(emptyList()) }

    // Lấy tham số từ navigation
    LaunchedEffect(navController.currentBackStackEntry) {
        // Đợi dữ liệu từ getAllSanPham hoàn tất
        while (sanPhamViewModel.danhSachAllSanPham.value.isEmpty() && !sanPhamViewModel.isLoading) {
            delay(100) // Chờ ngắn để tránh vòng lặp vô hạn
        }

        val search = navController.currentBackStackEntry?.arguments?.getString("searchQuery")
        val brand = navController.currentBackStackEntry?.arguments?.getString("brand")
        val price = navController.currentBackStackEntry?.arguments?.getString("price")
        val usage = navController.currentBackStackEntry?.arguments?.getString("usage")
        val chip = navController.currentBackStackEntry?.arguments?.getString("chip")
        val screen = navController.currentBackStackEntry?.arguments?.getString("screen")

        if (!search.isNullOrEmpty()) {
            isSearching = true
            searchQuery = search ?: ""
            performSearch(search, sanPhamViewModel)
        } else if (brand != null || price != null || usage != null || chip != null || screen != null) {
            isSearching = true
            performFilter(sanPhamViewModel, brand, price, usage, chip, screen)
        } else {
            isSearching = false
        }

        val response = hinhAnhViewModel.getAllHinhAnh()
        productImages = response.hinhanh // Cần sửa lỗi casting trước
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                actions = {
                    SearchField(
                        searchQuery = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearch = {
                            if (searchQuery.isNotEmpty()) {
                                isSearching = true
                                performSearch(searchQuery, sanPhamViewModel)
                                navController.navigate("homepage?searchQuery=$searchQuery") {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            } else {
                                isSearching = false
                                sanPhamViewModel.clearSanPhamSearch()
                            }
                        }
                    )
                }
            )
        },
        bottomBar = {
            MenuBottomNavBar(navController, gioHangViewModel)
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
                if (sanPhamViewModel.isLoading) {
                    Text("Đang tải...", modifier = Modifier.padding(16.dp))
                }
                sanPhamViewModel.errorMessage?.let { error ->
                    Text("Lỗi: $error", color = Color.Red, modifier = Modifier.padding(16.dp))
                }
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
                        text = if (isSearching) "Kết quả tìm kiếm" else "Sản phẩm đề xuất",
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
            val displayProducts = if (isSearching) searchResults else products
            if (displayProducts.isEmpty() && !sanPhamViewModel.isLoading) {
                item {
                    Text(
                        text = if (isSearching) "Không tìm thấy sản phẩm nào" else "Không có sản phẩm nào để hiển thị",
                        modifier = Modifier.padding(16.dp),
                        color = Color.Gray
                    )
                    Log.d("HOMEPAGE", "Displaying products: $displayProducts")
                }
            } else {
                items(sortedProducts(displayProducts, sortOption).chunked(2)) { productRow ->
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

fun performSearch(query: String, sanPhamViewModel: SanPhamViewModel) {
    if (query.isNotEmpty()) {
        sanPhamViewModel.getSanPhamSearch(query)
    }
}

fun performFilter(sanPhamViewModel: SanPhamViewModel, brand: String?, price: String?, usage: String?, chip: String?, screen: String?) {
    val allProducts = sanPhamViewModel.danhSachAllSanPham.value
    Log.d("HOMEPAGE", "All products: $allProducts")
    Log.d("HOMEPAGE", "Filter params - Brand: $brand, Price: $price, Usage: $usage, Chip: $chip, Screen: $screen")
    val filtered = allProducts.filter { product ->
        val brandMatch = brand == null || product.TenSanPham.contains(brand, ignoreCase = true) // Bỏ split, kiểm tra trực tiếp
        val priceMatch = price == null || filterByPrice(product.Gia, price)
        val usageMatch = usage == null || product.MoTa.contains(usage, ignoreCase = true)
        val chipMatch = chip == null || product.CPU.contains(chip, ignoreCase = true) // Bỏ split, kiểm tra trực tiếp
        val screenMatch = screen == null || product.ManHinh.contains(screen.replace(" inch", ""), ignoreCase = true)
        Log.d("HOMEPAGE", "Checking product: ${product.TenSanPham}, Matches: $brandMatch, $priceMatch, $usageMatch, $chipMatch, $screenMatch")
        brandMatch && priceMatch && usageMatch && chipMatch && screenMatch
    }
    sanPhamViewModel.updateFilteredProducts(filtered)
    Log.d("HOMEPAGE", "Filtered results: $filtered")
}

fun filterByPrice(gia: Int, priceRange: String): Boolean {
    return when (priceRange) {
        "Dưới 10 triệu" -> gia < 10_000_000
        "Từ 10 - 15 triệu" -> gia in 10_000_000..15_000_000
        "Từ 15 - 20 triệu" -> gia in 15_000_000..20_000_000
        "Từ 25 - 30 triệu" -> gia in 25_000_000..30_000_000
        else -> true
    }
}

fun filterByUsage(moTa: String, usage: String): Boolean {
    return when (usage) {
        "Văn phòng" -> moTa.contains("văn phòng", ignoreCase = true)
        "Gaming" -> moTa.contains("gaming", ignoreCase = true)
        "Mỏng nhẹ" -> moTa.contains("mỏng nhẹ", ignoreCase = true)
        "Cảm ứng" -> moTa.contains("cảm ứng", ignoreCase = true)
        "Laptop AI" -> moTa.contains("AI", ignoreCase = true)
        "Mac CTO - Nâng cấp theo cách của bạn" -> moTa.contains("Mac", ignoreCase = true)
        else -> true
    }
}

@Composable
fun SearchField(searchQuery: String, onQueryChange: (String) -> Unit, onSearch: () -> Unit) {


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
                    value = searchQuery,
                    onValueChange = onQueryChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                    decorationBox = { innerTextField ->
                        if (searchQuery.isEmpty()) {
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
            onClick = {
                onSearch()
            },
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
fun MenuBottomNavBar(navController: NavHostController, gioHangViewModel: GioHangViewModel = viewModel()) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, Screens.HOMEPAGE.route),
        BottomNavItem("Categories", Icons.AutoMirrored.Filled.List, Screens.CATAGORIES.route),
        BottomNavItem("Cart", Icons.Default.ShoppingCart, Screens.CARTSCREENS.route),
        BottomNavItem("Account", Icons.Default.Person, Screens.ACCOUNTSCREENS.route)
    )

    // Theo dõi route hiện tại
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    // Lấy danh sách giỏ hàng
    val cartItems by gioHangViewModel.listGioHang.collectAsState(initial = emptyList())
    // Đếm số lượng mã sản phẩm khác nhau
    val uniqueProductCount = cartItems.map { it.MaSanPham }.distinct().size
    
    NavigationBar(containerColor = Color.White) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    if (item.title == "Cart" && uniqueProductCount > 0) {
                        Box {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                modifier = Modifier.size(24.dp),
                                tint = if (currentRoute == item.route) Color.Black else Color.Gray
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 10.dp, y = (-4).dp)
                                    .size(16.dp)
                                    .background(Color.Red, shape = RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = uniqueProductCount.toString(),
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Justify   ,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    } else {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            modifier = Modifier.size(30.dp),
                            tint = if (currentRoute == item.route) Color.Black else Color.Gray
                        )
                    }
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 12.sp,
                        color = if (currentRoute == item.route) Color.Black else Color.Gray
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            // Tránh tạo nhiều bản sao của cùng một màn hình
                            launchSingleTop = true
                            // Khôi phục state khi quay lại
                            restoreState = true
                            // Chỉ pop đến root khi đang ở root
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                        }
                    }
                }
            )
        }
    }
}