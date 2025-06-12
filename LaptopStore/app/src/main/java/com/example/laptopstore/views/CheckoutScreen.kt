package com.example.laptopstore.views

import android.app.AlertDialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.lapstore.viewmodels.DiaChiViewmodel
import com.example.laptopstore.models.CartItem
import com.example.laptopstore.models.DiaChi
import com.example.laptopstore.viewmodels.DataStoreManager
import com.example.laptopstore.viewmodels.KhachHangViewModels
import com.example.laptopstore.viewmodels.TaiKhoanViewModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(navController: NavHostController,
                   totalPrice: Int, cartItemsJson: String,
                   taiKhoanViewModel: TaiKhoanViewModel,
                   khachHangViewModels: KhachHangViewModels,
                   diaChiViewmodel: DiaChiViewmodel)
{
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var showDiaChiDialog by remember { mutableStateOf(false) }
    val danhSachDiaChi by diaChiViewmodel.listDiaChi.collectAsState()
    var diaChiDuocChon by remember {
        mutableStateOf( DiaChi.EMPTY) }

    var context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }
    val customerId by dataStoreManager.customerId.collectAsState(initial = null)
    val maKhachHang = customerId
    LaunchedEffect(customerId) {
        if (!customerId.isNullOrBlank()) {
            diaChiViewmodel.getDiaChiKhachHang(customerId)
            Log.d("Checkout", "Gọi API với mã KH: $customerId")
        } else {
            Log.d("Checkout", "customerId vẫn null hoặc rỗng")
        }
    }
    LaunchedEffect (danhSachDiaChi){
        if(danhSachDiaChi.isNotEmpty() &&  diaChiDuocChon== DiaChi.EMPTY){
            diaChiDuocChon =danhSachDiaChi.firstOrNull{it.MacDinh == 1} ?: DiaChi.EMPTY
        }
    }
    val cartItems = try {
        val decodedJson = URLDecoder.decode(cartItemsJson, StandardCharsets.UTF_8.toString())
        Json.decodeFromString<List<CartItem>>(decodedJson)
    } catch (e: Exception) {
        Log.e("CheckoutScreen", "Error decoding cart items: ${e.message}")
        errorMessage = "Lỗi khi xử lý dữ liệu giỏ hàng"
        emptyList()
    }
    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = {
                errorMessage = null
                navController.navigateUp()
            },
            title = { Text("Lỗi") },
            text = { Text(errorMessage!!) },
            confirmButton = {
                Button(onClick = {
                    errorMessage = null
                    navController.navigateUp()
                }) {
                    Text("OK")
                }
            }
        )
        return
    }

    var fullName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf("COD") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Thanh toán", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
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
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Địa chỉ giao hàng", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { showDiaChiDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE0F7FA)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (diaChiDuocChon.MaDiaChi == 0) {
                            Text("Chưa chọn địa chỉ")
                        } else {
                            Text("Người nhận: ${diaChiDuocChon.TenNguoiNhan}")
                            Text("SĐT: ${diaChiDuocChon.SoDienThoai}")
                            Text("Địa chỉ: ${diaChiDuocChon.ThongTinDiaChi}")
                        }
                    }
                }
            }
            item {
                Text(
                    text = "Phương thức thanh toán",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedPaymentMethod == "COD",
                        onClick = { selectedPaymentMethod = "COD" }
                    )
                    Text(
                        text = "Thanh toán khi nhận hàng (COD)",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedPaymentMethod == "Card",
                        onClick = { selectedPaymentMethod = "Card" }
                    )
                    Text(
                        text = "Thanh toán qua thẻ",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            item {
                Text(
                    text = "Danh sách sản phẩm",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                if (cartItems.isEmpty()) {
                    Text(
                        text = "Không có sản phẩm nào",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                } else {
                    cartItems.forEach { cartItem ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .shadow(4.dp, RoundedCornerShape(8.dp)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .background(Color.White)
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                ) {
                                    AsyncImage(
                                        model = cartItem.product?.HinhAnh,
                                        contentDescription = cartItem.product?.TenSanPham,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = cartItem.product?.TenSanPham ?: "Sản phẩm không xác định",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 2
                                    )
                                    Text(
                                        text = "Số lượng: ${cartItem.SoLuong}",
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "${(cartItem.product?.Gia ?: 0 - ((cartItem.product?.Gia ?: 0 * (cartItem.product?.GiamGia ?: 0)) / 100)) / 1000}.000 VNĐ",
                                        fontSize = 14.sp,
                                        color = Color.Red,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
            item {
                Text(
                    text = "Tổng tiền: ${totalPrice / 1000}.000 VNĐ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /* Xử lý thanh toán */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Xác nhận đặt hàng ",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        if (showDiaChiDialog) {
            AlertDialog(
                onDismissRequest = { showDiaChiDialog = false },
                title = { Text("Chọn địa chỉ giao hàng") },
                text = {
                    Column(modifier = Modifier.height(300.dp)) {
                        LazyColumn {
                            items(danhSachDiaChi.size) { index ->
                                val diaChi = danhSachDiaChi[index]
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable {
                                            diaChiDuocChon = diaChi
                                            showDiaChiDialog = false
                                        },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (diaChi.MacDinh == 1)
                                            Color(0xFFE0F7FA) else Color.White
                                    )
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text("Người nhận: ${diaChi.TenNguoiNhan}")
                                        Text("SĐT: ${diaChi.SoDienThoai}")
                                        Text("Địa chỉ: ${diaChi.ThongTinDiaChi}")
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showDiaChiDialog = false }) {
                        Text("Đóng")
                    }
                }
            )
        }

    }
}
