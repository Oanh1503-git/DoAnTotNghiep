import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.laptopstore.models.Screens
import com.example.laptopstore.viewmodels.GioHangViewModel
import com.example.laptopstore.viewmodels.SanPhamViewModel
import com.example.laptopstore.viewmodels.TaiKhoanViewModel
import com.example.laptopstore.views.MenuBottomNavBar
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavHostController) {
    // Danh sách sản phẩm mẫu trong giỏ hàng
    val sampleCart = listOf(
        SampleCartItem("Laptop Acer Aspire", "https://via.placeholder.com/150", 15000000, 1),
        SampleCartItem("MacBook Air M1", "https://via.placeholder.com/150", 25000000, 2),
    )

    val totalPrice = sampleCart.sumOf { it.price * it.quantity }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Giỏ hàng", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
            )
        },
        bottomBar = {
            MenuBottomNavBar(navController) // Tuỳ bạn định nghĩa hoặc bỏ nếu không cần
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            if (sampleCart.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Giỏ hàng của bạn đang trống", fontSize = 16.sp, color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(sampleCart) { item ->
                        CartItemCardUIOnly(item)
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Tổng tiền:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "${totalPrice / 1000}.000 VNĐ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { /* Không thực hiện hành động gì */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Thanh toán", color = Color.White, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

data class SampleCartItem(
    val name: String,
    val imageUrl: String,
    val price: Int,
    val quantity: Int
)

@Composable
fun CartItemCardUIOnly(item: SampleCartItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(item.name, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${item.price / 1000}.000 VNĐ",
                    fontSize = 14.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Số lượng: ${item.quantity}", fontSize = 14.sp)
            }
            IconButton(onClick = { /* Xoá item - không xử lý */ }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
            }
        }
    }
}
