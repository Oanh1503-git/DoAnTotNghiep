import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.focus.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Diversity1
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app_e_commerce.model.BottomNavItem
import org.w3c.dom.Text
import com.example.laptopstore.models.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreens(navHostController: NavHostController){
    var isFocused by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = " Tài Khoản của tôi ",
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
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                }
            )
        }
    ){
            innerPadding->
        LazyColumn (modifier = Modifier.padding(innerPadding)){
            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navHostController.navigate(Screens.HOMEPAGE.route)
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = "title",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "Thông Tin Khách Hàng ", fontSize = 16.sp)
                }
            }
            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navHostController.navigate(Screens.HOMEPAGE.route)
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.AddLocation,
                        contentDescription = "title",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "Sổ Địa Chỉ ", fontSize = 16.sp)
                }
            }
            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navHostController.navigate(Screens.HOMEPAGE.route)
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Diversity1,
                        contentDescription = "title",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "Sản Phẩm Yêu Thích  ", fontSize = 16.sp)
                }

            }
            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navHostController.navigate(Screens.HOMEPAGE.route)
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = "title",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "THông Tin Đơn hàng  ", fontSize = 16.sp)
                }
            }
            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navHostController.navigate(Screens.HOMEPAGE.route)
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Details,
                        contentDescription = "title",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "Đơn Hàng Hủy ", fontSize = 16.sp)
                }
            }
        }
    }
}