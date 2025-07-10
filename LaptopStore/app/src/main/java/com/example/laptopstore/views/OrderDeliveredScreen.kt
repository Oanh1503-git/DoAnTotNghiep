package com.example.laptopstore.views

import DonHangDayDuItem
import DonHangDayDuResponse
import SanPhamItem
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.lapstore.viewmodels.HoaDonViewModel
import com.example.laptopstore.models.BinhLuanDanhGia
import com.example.laptopstore.models.KhachHang
import com.example.laptopstore.viewmodels.BinhLuanViewModel
import com.example.laptopstore.viewmodels.ChiTietHoaDonViewmodel
import com.example.laptopstore.viewmodels.DataStoreManager
import com.example.laptopstore.viewmodels.SanPhamViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDeliveredScreen(
    viewModel: HoaDonViewModel,
    sanPhamViewModel: SanPhamViewModel,
    chitietdonhang: ChiTietHoaDonViewmodel,
    navController: NavHostController
) {

    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }
    val customerId by dataStoreManager.customerId.collectAsState(initial = null)

    val maKhachHang = customerId
    val binhLuanViewModel = remember { BinhLuanViewModel() }
    val currencyFormatter = NumberFormat.getInstance(Locale("vi", "VN")).apply {
        maximumFractionDigits = 0
    }
    LaunchedEffect(maKhachHang) {
        if (maKhachHang != null) {
            viewModel.getDonHangDayDuTheoKhachHang(maKhachHang)
            Log.d("OrderDeliveredScreen", "Fetching orders for customerId: $maKhachHang")
        }
    }
    val donHangList by viewModel.donHangList.collectAsState()
    Log.d("OrderDeliveredScreen", "Fetching orders for donhang: $donHangList")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Đơn hàng đã giao", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            val donHangDaGiao = donHangList.filter { it.TrangThai == "3" }

            Log.d("OrderDeliveredScreen", "Filtered orders: $donHangDaGiao")
            if (donHangDaGiao.isEmpty()){
                Text("Không có đơn hàng nào")
            }else{
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(donHangDaGiao) { donHang ->
                        DeliveredOrderCard(donHang, viewModel, navController, currencyFormatter, binhLuanViewModel,maKhachHang)
                }
            }}
        }
    }
}

@Composable
fun DeliveredOrderCard(
    donHang: DonHangDayDuItem,
    viewModel: HoaDonViewModel,
    navController: NavHostController,
    currencyFormatter: NumberFormat,
    binhLuanViewModel: BinhLuanViewModel,
    maKhachHang: String?
) {
    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }
    val tenKhachHang by dataStoreManager.username.collectAsState(initial = null)

    val sanPhamList = donHang.SanPham
    val distinctMaSanPhamCount = sanPhamList.map { it.MaSanPham }.distinct().count()

    // State chung cho comment & rating (nếu muốn tách riêng từng sản phẩm cần tạo StateHolder riêng)
    var isSubmitting by remember { mutableStateOf(false) }
    Text(
        text = "Ngay giao hang: ${donHang.NgayGiaoHang}",
        fontSize = 12.sp,
        color = Color.Gray
    )

    if (distinctMaSanPhamCount > 1) {
        sanPhamList.forEach { sp ->
            ReviewCard(
                sp = sp,
                donHang = donHang,
                maKhachHang = maKhachHang,
                tenKhachHang = tenKhachHang,
                currencyFormatter = currencyFormatter,
                binhLuanViewModel = binhLuanViewModel,
                isSubmitting = isSubmitting,
                onSubmittingChange = { isSubmitting = it }
            )
        }
    } else {
        sanPhamList.forEach { sp ->
            ReviewCard(
                sp = sp,
                donHang = donHang,
                maKhachHang = maKhachHang,
                tenKhachHang = tenKhachHang,
                currencyFormatter = currencyFormatter,
                binhLuanViewModel = binhLuanViewModel,
                isSubmitting = isSubmitting,
                onSubmittingChange = { isSubmitting = it }
            )
        }
    }
}

@Composable
fun ReviewCard(
    sp: SanPhamItem,
    donHang: DonHangDayDuItem,
    maKhachHang: String?,
    tenKhachHang: String?,
    currencyFormatter: NumberFormat,
    binhLuanViewModel: BinhLuanViewModel,
    isSubmitting: Boolean,
    onSubmittingChange: (Boolean) -> Unit
) {
    val context = LocalContext.current

    var rating by remember { mutableStateOf(5) }
    var commentText by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Product Info
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(sp.HinhAnh)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(sp.TenSanPham, fontWeight = FontWeight.SemiBold)
                    Text("Giá: ${currencyFormatter.format(sp.Gia)} VNĐ")
                    Text("Số lượng: ${sp.SoLuong}")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Đánh giá sản phẩm", fontWeight = FontWeight.Medium, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                (1..5).forEach { star ->
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star $star",
                        tint = if (star <= rating) Color(0xFFFFD700) else Color.Gray,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { rating = star }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = commentText,
                onValueChange = { commentText = it },
                label = { Text("Nhập bình luận") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    if (commentText.isNotBlank() && !isSubmitting) {
                        onSubmittingChange(true)

                        val review = BinhLuanDanhGia(
                            MaKhachHang = maKhachHang ?: "",
                            MaSanPham = sp.MaSanPham,
                            MaHoaDon = donHang.MaHoaDon,
                            SoSao = rating,
                            NoiDung = commentText,
                            NgayDanhGia = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                            TrangThai = 1,
                            MaBinhLuan = 0,
                            TenKhachHang = tenKhachHang ?: ""
                        )

                        binhLuanViewModel.createReview(review)

                        commentText = ""
                        rating = 5
                        Toast.makeText(context, "Đã gửi đánh giá", Toast.LENGTH_SHORT).show()

                        onSubmittingChange(false)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSubmitting,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Gửi đánh giá", color = Color.White)
            }
        }
    }
}
