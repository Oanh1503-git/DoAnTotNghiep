package com.example.laptopstore.models

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import com.example.laptopstore.viewmodels.GioHangViewModel
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DiaChiItemOnCheckout(
    diaChi: DiaChi,
    onToggleMacDinh: ((Boolean) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (diaChi.MacDinh == 1) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = diaChi.TenNguoiNhan,
                    style = MaterialTheme.typography.titleMedium
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = diaChi.MacDinh == 1,
                        onCheckedChange = { isChecked ->
                            onToggleMacDinh?.invoke(isChecked)
                        }
                    )
                    Text("Mặc định", style = MaterialTheme.typography.bodySmall)
                }
            }
            Text("SĐT: ${diaChi.SoDienThoai}", style = MaterialTheme.typography.bodyMedium)
            Text("Địa chỉ: ${diaChi.ThongTinDiaChi}", style = MaterialTheme.typography.bodyMedium)
        }
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
@Composable
fun ProductCardInHome(product: SanPham, navController: NavHostController, images: List<HinhAnh>, modifier: Modifier = Modifier) {
    val defaultImage = images.find { it.MacDinh == 1 }?.DuongDan ?: product.HinhAnh

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
                    fontSize = 14.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
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