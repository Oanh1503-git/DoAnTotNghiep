import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.laptopstore.models.Screens
import com.example.laptopstore.viewmodels.TaiKhoanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreens(navHostController: NavHostController) {
    val taiKhoanViewModel: TaiKhoanViewModel = viewModel()
    var islogin by remember { mutableStateOf(false) }
    var isCheckingLogin by remember { mutableStateOf(true) }

    // Kiểm tra đăng nhập khi khởi tạo màn hình

    // Nếu vẫn đang kiểm tra đăng nhập, chưa render UI


    // Nếu chưa đăng nhập thì chuyển hướng sang màn hình Login

    // Giao diện khi đã đăng nhập
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
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            item{

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Button(onClick ={
                        navHostController.navigate(Screens.Login_Screens.route)
                            }

                    ){
                        Text("đăng nhập")
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text("xin chào bạn ")
                }
            }
            item {
                AccountItem(
                    icon = Icons.Default.Person,
                    text = "Thông Tin Khách Hàng",
                    onClick = { navHostController.navigate(Screens.HOMEPAGE.route) }
                )
            }
            item {
                AccountItem(
                    icon = Icons.Default.AddLocation,
                    text = "Sổ Địa Chỉ",
                    onClick = { navHostController.navigate(Screens.HOMEPAGE.route) }
                )
            }
            item {
                AccountItem(
                    icon = Icons.Default.Diversity1,
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
                    icon = Icons.Default.Details,
                    text = "Đơn Hàng Hủy",
                    onClick = { navHostController.navigate(Screens.HOMEPAGE.route) }
                )
            }
        }
    }
}

@Composable
fun AccountItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, fontSize = 16.sp)
    }
}
