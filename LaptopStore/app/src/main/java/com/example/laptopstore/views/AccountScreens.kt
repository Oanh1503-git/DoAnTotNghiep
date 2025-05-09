import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Diversity1
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.laptopstore.models.Screens
import kotlin.math.exp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreens(navHostController: NavHostController) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tài Khoản của tôi",
                        modifier = Modifier.padding(2.dp),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navHostController.navigate(Screens.HOMEPAGE.route) }) {
                        Icon(
                            Icons.Default.ArrowBackIosNew,
                            contentDescription = null,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                },
                actions = {
                    var expanded by remember{mutableStateOf(false)}
                    IconButton (onClick = {expanded=true}){
                        Icon(imageVector = Icons.Default.MoreVert,
                            contentDescription = "dot_menu",)
                        DropdownMenu(
                            expanded= expanded,
                            onDismissRequest = {
                                expanded=false
                            }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Dịch")},
                                onClick={
                                    expanded=false
                                    // code sử lý
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Đăng xuất") },
                                onClick = {
                                    expanded = false
                                    // TODO: Xử lý Đăng xuất
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            item {
                AccountMenuItem(
                    icon = Icons.Filled.Person,
                    title = "Thông Tin Khách Hàng",
                    onClick = { navHostController.navigate(Screens.HOMEPAGE.route) }
                )
            }
            item {
                AccountMenuItem(
                    icon = Icons.Filled.AddLocation,
                    title = "Sổ Địa Chỉ",
                    onClick = { navHostController.navigate(Screens.HOMEPAGE.route) }
                )
            }
            item {
                AccountMenuItem(
                    icon = Icons.Filled.Diversity1,
                    title = "Sản Phẩm Yêu Thích",
                    onClick = { navHostController.navigate(Screens.HOMEPAGE.route) }
                )
            }
            item {
                AccountMenuItem(
                    icon = Icons.Default.ShoppingCart,
                    title = "Thông Tin Đơn Hàng",
                    onClick = { navHostController.navigate(Screens.HOMEPAGE.route) }
                )
            }
            item {
                AccountMenuItem(
                    icon = Icons.Default.Details,
                    title = "Đơn Hàng Hủy",
                    onClick = { navHostController.navigate(Screens.HOMEPAGE.route) }
                )
            }
        }
    }
}

@Composable
fun AccountMenuItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontSize = 16.sp)
    }
}
