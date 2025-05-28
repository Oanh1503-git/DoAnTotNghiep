import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.laptopstore.models.Screens
import com.example.laptopstore.viewmodels.KhachHangViewModels
import com.example.laptopstore.viewmodels.TaiKhoanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreens(
    navHostController: NavHostController,
    taiKhoanViewModel: TaiKhoanViewModel,
    khachHangViewModels: KhachHangViewModels
) {
    val context = LocalContext.current
    val isLoggedIn by taiKhoanViewModel.isLoggedIn.collectAsState()
    val tentaikhoan = taiKhoanViewModel.tentaikhoan

    // Dialog xác nhận đăng xuất
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tài Khoản của tôi",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navHostController.navigate(Screens.HOMEPAGE.route)
                    }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (!isLoggedIn) {
            // Hiển thị giao diện khi chưa đăng nhập
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Vui lòng đăng nhập để xem thông tin tài khoản",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        try {
                            navHostController.navigate("Login_Screens") {
                                launchSingleTop = true
                            }
                        } catch (e: Exception) {
                            // Log lỗi nếu có
                            e.printStackTrace()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Text("Đăng nhập")
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = {
                        try {
                            navHostController.navigate("REGISTERSCREEN") {
                                launchSingleTop = true
                            }
                        } catch (e: Exception) {
                            // Log lỗi nếu có
                            e.printStackTrace()
                        }
                    },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Text("Đăng ký")
                }
            }
        } else {
            // Hiển thị giao diện khi đã đăng nhập
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color.Red
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Xin chào, ${tentaikhoan ?: "Người dùng"}!",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Chúc bạn có một ngày tốt lành!",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedButton(
                                onClick = { showLogoutDialog = true },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color.Red
                                )
                            ) {
                                Icon(Icons.Default.Logout, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Đăng xuất")
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Text(
                        text = "Thông tin cá nhân",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        color = Color.Gray
                    )
                }

                item {
                    AccountItem(
                        icon = Icons.Default.Person,
                        text = "Thông Tin Khách Hàng",
                        onClick = {
                            val maKhachHang = tentaikhoan ?: return@AccountItem
                            navHostController.navigate(Screens.ACCOUNTDETAIL.createRoute(maKhachHang))
                        }
                    )
                }

                item {
                    AccountItem(
                        icon = Icons.Default.AddLocation,
                        text = "Sổ Địa Chỉ",
                        onClick = { 
                            val maKhachHang = taiKhoanViewModel.taikhoan?.MaKhachHang
                            if (maKhachHang != null) {
                                navHostController.navigate(Screens.ADDRESS.createRoute(maKhachHang))
                            }
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Text(
                        text = "Đơn hàng & Sản phẩm",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        color = Color.Gray
                    )
                }

                item {
                    AccountItem(
                        icon = Icons.Default.Favorite,
                        text = "Sản Phẩm Yêu Thích",
                        onClick = { navHostController.navigate(Screens.HOMEPAGE.route) }
                    )
                }

                item {
                    AccountItem(
                        icon = Icons.Default.ShoppingCart,
                        text = "Thông Tin Đơn Hàng",
                        onClick = { navHostController.navigate(Screens.HOMEPAGE.route) }
                    )
                }

                item {
                    AccountItem(
                        icon = Icons.Default.Cancel,
                        text = "Đơn Hàng Hủy",
                        onClick = { navHostController.navigate(Screens.HOMEPAGE.route) }
                    )
                }
            }
        }
    }

    // Dialog xác nhận đăng xuất
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text("Xác nhận đăng xuất")
            },
            text = {
                Text("Bạn có chắc chắn muốn đăng xuất?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        taiKhoanViewModel.logout()
                        showLogoutDialog = false
                        // Refresh lại màn hình hoặc điều hướng về trang chủ
                        navHostController.navigate(Screens.HOMEPAGE.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Đăng xuất")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}

@Composable
fun AccountItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Red
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}