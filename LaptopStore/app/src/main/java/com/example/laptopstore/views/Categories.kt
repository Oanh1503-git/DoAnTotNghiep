//package com.example.app_e_commerce.views
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBackIosNew
//import androidx.compose.material.icons.filled.NavigateNext
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.input.pointer.motionEventSpy
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.example.laptopstore.models.Screens
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun Categories(navController: NavHostController) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Categories", textAlign = TextAlign.Center) },
//                navigationIcon = {
//                    IconButton(onClick = { navController.navigate(Screens.HOMEPAGE.route) }) {
//                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = null)
//                    }
//                },
//                actions = {
//                    IconButton(onClick = {}) {
//                        Icon(Icons.Filled.Search, contentDescription = "Search")
//                    }
//                }
//            )
//        }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .padding(innerPadding)
//                .fillMaxSize()
//                .padding(8.dp)
//        ) {
//            val navHostController = rememberNavController()
//            AppContent(navHostController)
//        }
//    }
//}
//
//@Composable
//fun AppContent(navController: NavHostController) {
////    val categoryImages = listOf(
////
////    )
//
//    Row(modifier = Modifier.fillMaxSize()) {
//        // Menu bên trái
//        Column {
//            MenuSidebar(
//                onCategorySelected = { category ->
//                    navController.navigate(category) // Điều hướng tới màn hình tương ứng
//                },
//                modifier = Modifier.weight(1f),
//                categoryImages = categoryImages
//            )
//        }
//
//        // Màn hình nội dung sử dụng Navigation
//        Column(modifier = Modifier.weight(4f)) {
//            NavHost(navController, startDestination = "Dành cho bạn") {
//                composable("Dành cho bạn") { DanhChoBanScreens(navController) }
//                composable("Thiết bị điện tử") { ThietBiDienTuScreens(navController) }
//                composable("Thời trang và phụ kiện nữ") { ThoiTrangVaPhuKienNu(navController) }
//                // Thêm các màn hình khác vào đây
//            }
//        }
//    }
//}
//
//@Composable
//fun MenuSidebar(
//    onCategorySelected: (String) -> Unit,
//    modifier: Modifier,
//    categoryImages: List<Int>
//) {
//    Column(
//        modifier = modifier
//            .width(140.dp)
//            .background(Color.LightGray)
//            .padding(4.dp)
//    ) {
//        Text(
//            "Categories",
//            fontSize = 20.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(8.dp)
//        )
//
//        LazyColumn {
//            itemsIndexed(categories) { index, category ->
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { onCategorySelected(category) } // Gọi điều hướng khi nhấn
//                        .padding(8.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Image(
//                        painter = painterResource(id = categoryImages[index]),
//                        contentDescription = category,
//                        modifier = Modifier
//                            .size(50.dp)
//                            .clip(CircleShape)
//                    )
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(
//                        text = category,
//                        textAlign = TextAlign.Center,
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Normal,
//                        color = Color.Black
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun ContentScreen(category: String,modifier: Modifier) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Text("$category", fontSize = 22.sp,
//            fontWeight = FontWeight.Bold, color = Color.Red)
//        Spacer(modifier = Modifier.padding(5.dp))
//        LazyRow {
//            items(getProducts(category)) { product ->
//                ProductItem(product)
//            }
//        }
//    }
//}
//
//@Composable
//fun ProductItem(product: String) {
//    Card(
//        modifier = Modifier
//            .padding(8.dp)
//            .width(120.dp)
//            .height(100.dp),
//        colors = CardDefaults.cardColors(Color.White)
//    ) {
//        Box(contentAlignment = Alignment.Center) {
//            Text(product, fontSize = 14.sp)
//        }
//    }
//}
//
//val categories = listOf(
//    "Dành cho bạn", "Thiết bị điện tử", "Phụ kiện điện tử",
//    "Tv và Điện gia dụng", "Sức khỏe và làm đẹp", "Hàng củ mẹ và bé",
//    "Siêu thị tạp hóa", "gia dụng và đời sống", "thời trang và phụ kiện nữ",
//    "thời trang và phụ kiện nam", "thời trang và phụ kiện trẻ","Thể thao và du lịch",
//    "Ô tô , xe máy thiết bị đinh vị","Vocher và dịch vụ"
//)
//
//fun getProducts(category: String): List<String> {
//    return List(5) { "$category Sản phẩm ${it + 1}" }
//}
//@Composable
//fun DanhChoBanScreens(navHostController: NavHostController){
//    Row (modifier = Modifier.padding(2.dp)){
//        Box(){
//            Image(painter = painterResource(R.drawable.personicon),
//                contentDescription = "SanPhamNoiBat")
//            Spacer(modifier = Modifier.padding(4.dp))
//            Text(text = "san pham")
//        }
//
//    }
//}
//@Composable
//fun ThietBiDienTuScreens(navHostController: NavHostController) {
//    val categories = listOf("Điện Thoại Di động", "Máy Tính Bảng", "Laptop")
//    val products = listOf("laptop văn phòng", "laptop gaming")
//    val imageListElectronics = listOf(R.drawable.laptopcoban,R.drawable.laptopgaming)
//    Column(modifier = Modifier.padding(16.dp)) {
//        LazyColumn {
//            items(categories) { category ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { /* TODO: Chuyển đến danh mục con nếu cần */ }
//                        .padding(8.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = category,
//                        textAlign = TextAlign.Left,
//                        modifier = Modifier.weight(1f)
//                    )
//                    Icon(Icons.Default.NavigateNext, contentDescription = "NextToCommecre")
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        LazyColumn {
//            itemsIndexed(products) { index,product ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(4.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Image(
//                        painter = painterResource(id=imageListElectronics[index]),
//                        contentDescription = product,
//                        modifier = Modifier.size(100.dp)
//                    )
//                    Spacer(modifier = Modifier.width(2.dp))
//                    Text(text = product, fontSize = 20.sp)
//                }
//            }
//        }
//        Text(text = "Máy Tính để bàn")
//    }
//}
//
//@Composable
//fun ThoiTrangVaPhuKienNu(navHostController: NavHostController){
//    Column(modifier = Modifier.padding(16.dp)) {
//        Text("Thời trang và phụ kiện nữ", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Red)
//    }
//}
//@Composable
//fun ProductCard(){
//    Card(onClick = {}) {
//        Box(modifier = Modifier.padding(2.dp)){
//            Image(painter = painterResource(id = R.drawable.banner2),
//                contentDescription = "")
//            Spacer(Modifier.padding(4.dp))
//            Text(text = "tên sản phẩm")
//            Spacer(Modifier.padding(4.dp))
//            Text(text = "Price")
//            Spacer(Modifier.padding(4.dp) )
//            Text("review")
//        }
//    }
//}