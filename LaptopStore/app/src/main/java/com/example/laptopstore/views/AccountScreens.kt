import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AccountScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tài khoản của tôi") }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(
                    text = "Xin chào, le nguyen hoang oanh",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(16.dp),
                    color = Color.DarkGray
                )
            }

            val options = listOf(
                "Đơn hàng của tôi",
                "Đơn hàng đổi trả",
                "Đơn hàng huỷ",
                "Danh sách yêu thích & Cửa hàng đang theo dõi",
                "Đánh giá của tôi",
                "Số địa chỉ",
                "Tùy chọn thanh toán",
                "Thông tin tài khoản",
                "Cài đặt",
                "Chính sách",
                "Nhận xét",
                "Trung tâm hỗ trợ"
            )

            options.forEach { title ->
                ListItem(
                    headlineText = { Text(text = title) },
                    modifier = Modifier
                        .clickable {
                            // TODO: điều hướng tới từng màn hình cụ thể
                            navController.navigate("screen_${title.hashCode()}")
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Divider()
            }
        }
    }
}
