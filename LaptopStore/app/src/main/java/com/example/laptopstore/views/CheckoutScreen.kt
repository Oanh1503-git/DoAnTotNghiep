package com.example.laptopstore.views

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.lapstore.models.ChiTietHoaDon
import com.example.lapstore.viewmodels.DiaChiViewmodel
import com.example.lapstore.viewmodels.HoaDonViewModel
import com.example.laptopstore.models.CartItem
import com.example.laptopstore.models.DiaChi
import com.example.laptopstore.models.HoaDon
import com.example.laptopstore.models.MenuBottomNavBar
import com.example.laptopstore.models.Screens
import com.example.laptopstore.viewmodels.ChiTietHoaDonViewmodel
import com.example.laptopstore.viewmodels.DataStoreManager
import com.example.laptopstore.viewmodels.GioHangViewModel
import com.example.laptopstore.viewmodels.KhachHangViewModels
import com.example.laptopstore.viewmodels.SanPhamViewModel
import com.example.laptopstore.viewmodels.TaiKhoanViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.delay
import java.net.URLDecoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(navController: NavHostController,
                   totalPrice: Int,
                   cartItemsJson: String,
                   taiKhoanViewModel: TaiKhoanViewModel,
                   khachHangViewModels: KhachHangViewModels,
                   diaChiViewmodel: DiaChiViewmodel,
                   hoaDonBanViewModel: HoaDonViewModel,
                   chiTietHoaDonViewmodel: ChiTietHoaDonViewmodel,
                   sanPhamViewModel: SanPhamViewModel,
                   gioHangViewModel: GioHangViewModel
)
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
    val scope = rememberCoroutineScope()

    val soluongkho = sanPhamViewModel.soLuongTonKhoState.collectAsState()
    val sl=soluongkho.value
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
    fun getCurrentDateTimeFormatted(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        return current.format(formatter)
    }
    var ngaytaohoadon = getCurrentDateTimeFormatted()

    val cartItems = try {
        val decodedJson = URLDecoder.decode(cartItemsJson, "UTF-8")
        val type = object : TypeToken<List<CartItem>>() {}.type
        val items = Gson().fromJson<List<CartItem>>(decodedJson, type)
        items.forEach { Log.d("CHECKOUT", "CartItem: MaSP=${it.MaSanPham}, Gia=${it.Gia}, ProductGia=${it.product?.Gia}") }
        items
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
    val mahoadontheokhach = hoaDonBanViewModel.maHoaDonState.collectAsState()
    LaunchedEffect(mahoadontheokhach) {
        Log.d("checkout", "Mã hóa đơn mới nhất: ${mahoadontheokhach.value}")
    }

    val currencyFormatter = NumberFormat.getInstance(Locale("vi", "VN"))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Thanh toán", fontSize = 18.sp, fontWeight = FontWeight.Bold) },

                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screens.HOMEPAGE.route)
                    }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
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
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Địa chỉ giao hàng(chọn địa chỉ khác hoặc thêm địa chỉ mới nếu chưa có )", fontSize = 18.sp, fontWeight = FontWeight.Bold)

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
            items(cartItems) { cartItem ->
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
                                model = cartItem.getProductImage(),
                                contentDescription = cartItem.TenSanPham,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = cartItem.getProductName(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 2
                            )
                            Text(
                                text = "Số lượng: ${cartItem.SoLuong}",
                                fontSize = 14.sp
                            )
                            Text(
                                text = currencyFormatter.format((cartItem.getProductPrice() - ((cartItem.getProductPrice() * cartItem.getProductDiscount()) / 100))) + " VNĐ",
                                fontSize = 14.sp,
                                color = Color.Red,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            item {
                Text(
                    text = "Tổng tiền: ${currencyFormatter.format(totalPrice)} VNĐ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        scope.launch {
                            if (diaChiDuocChon.MaDiaChi == 0) {
                                errorMessage = "Vui lòng chọn địa chỉ giao hàng"
                                return@launch
                            }

                            if (cartItems.isEmpty()) {
                                errorMessage = "Giỏ hàng trống"
                                return@launch
                            }

                            if (maKhachHang.isNullOrBlank()) {
                                errorMessage = "Không xác định được khách hàng"
                                return@launch
                            }
                            if (sl !=null && sl <= 0){
                                errorMessage = "Sản phẩm đã hết hàng"
                                return@launch
                            }else{

                                val newHoaDonBan = HoaDon(
                                    MaHoaDon = 0, // để backend tự sinh
                                    MaKhachHang = maKhachHang,
                                    NgayDatHang = ngaytaohoadon,
                                    MaDiaChi = diaChiDuocChon.MaDiaChi,
                                    TongTien = totalPrice,
                                    PhuongThucThanhToan = selectedPaymentMethod,
                                    TrangThai = 0
                                )

                                val maHoaDonMoi = hoaDonBanViewModel.addHoaDon(newHoaDonBan)
                                Log.d("checkout", "Tạo hóa đơn thành công - Mã: $maHoaDonMoi")

                                if (maHoaDonMoi != null) {
                                    val allSuccess = cartItems.map { cartItem ->
                                        Log.d("ChiTietHoaDon", """
                                             Thêm sản phẩm:
                                                 - Mã SP: ${cartItem.MaSanPham}
                                                - Số lượng: ${cartItem.SoLuong}
                                                     - Giá: ${cartItem.Gia}
                                        - Giảm giá: ${cartItem.GiamGia}
                                    - Thành tiền: ${cartItem.SoLuong * (cartItem.Gia ?: 0).toDouble() - (cartItem.GiamGia ?: 0).toDouble()}
                                    """.trimIndent())

                                        Log.d("chitiethoadon", "Tạo hóa đơn thành công - Mã: $maHoaDonMoi")
                                        val success = chiTietHoaDonViewmodel.addHoaDonChiTiet(
                                            ChiTietHoaDon(
                                                MaChiTietHoaDon = 0,
                                                MaHoaDon = maHoaDonMoi,
                                                MaSanPham = cartItem.MaSanPham,
                                                SoLuong = cartItem.SoLuong,
                                                DonGia = (cartItem.Gia ?: cartItem.product?.Gia?.toDouble() ?: 0.0),
                                                GiamGia = (cartItem.GiamGia ?: cartItem.product?.GiamGia?.toDouble() ?: 0.0),
                                                ThanhTien = cartItem.SoLuong * (cartItem.Gia ?: cartItem.product?.Gia?.toDouble() ?: 0.0) - (cartItem.GiamGia ?: cartItem.product?.GiamGia?.toDouble() ?: 0.0)
                                            )
                                        )

                                        Log.d("ChiTietHoaDon", "SP: ${cartItem.MaSanPham} -> success: $success")
                                        success

                                    }.all { it }

                                    if (allSuccess) {
                                        // Hiển thị thông báo thành công
                                        Log.d("Checkout", "Đặt hàng thành công! Đang xóa sản phẩm khỏi giỏ hàng...")
                                        Log.d("ChiTietHoaDon", " maKhachHang-> : $maKhachHang")

                                        cartItems.forEach { cartItem ->
                                            sanPhamViewModel.truSoLuongTrongKho(cartItem.MaSanPham, cartItem.SoLuong)
                                            // Chỉ xóa khỏi giỏ hàng nếu sản phẩm thực sự có trong giỏ hàng
                                            Log.d("ChiTietHoaDon", " maKhachHang-> : $maKhachHang,${cartItem.MaSanPham}")
                                            if (cartItem.MaGioHang > 0) {
                                                Log.d("ChiTietHoaDon", " maKhachHang-> : $maKhachHang,${cartItem.MaSanPham}")
                                                gioHangViewModel.deleteOnCartByID(maKhachHang, cartItem.MaSanPham)

                                                Log.d("ChiTietHoaDon", " maKhachHang-> : $maKhachHang,${cartItem.MaSanPham}")
                                            }
                                        }
                                        // Đợi một chút để các thao tác xóa hoàn thành trước khi refresh
                                        delay(500)
                                        // Refresh lại danh sách giỏ hàng sau khi xóa
                                        gioHangViewModel.getGioHangByKhachHang(maKhachHang)
                                        navController.navigate(Screens.ORDERSUCCESSSCREEN.route)
                                    } else {
                                        errorMessage = "Lỗi khi thêm chi tiết hóa đơn"
                                    }

                                } else {
                                    errorMessage = "Tạo hóa đơn thất bại"
                                }
                            }

                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Xác nhận đặt hàng",
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
                title = { Text("Chọn địa chỉ giao hàng (nhấn chọn để thêm hoặc thay đổi địa chỉ)") },
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
                        Spacer(modifier = Modifier.height(16.dp))
                        // Nút thêm địa chỉ mới
                        Button(
                            onClick = {
                                showDiaChiDialog = false
                                navController.navigate(Screens.ADDRESS.createRoute(fromCheckout = true))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                        ) {
                            Text("Thêm địa chỉ mới", color = Color.White, fontWeight = FontWeight.Bold)
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

