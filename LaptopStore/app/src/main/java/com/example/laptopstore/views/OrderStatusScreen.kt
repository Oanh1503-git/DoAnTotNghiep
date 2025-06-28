package com.example.laptopstore.views

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.laptopstore.models.Screens
import com.example.lapstore.viewmodels.HoaDonViewModel
import com.example.laptopstore.viewmodels.DataStoreManager
import com.example.laptopstore.models.SanPham
import com.example.laptopstore.viewmodels.SanPhamViewModel
import com.example.laptopstore.viewmodels.ChiTietHoaDonViewmodel
import com.example.lapstore.models.ChiTietHoaDon
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderStatusScreen(
    navController: NavHostController,
    hoaDonBanViewModel: HoaDonViewModel,
    sanPhamViewModel: SanPhamViewModel,
    chiTietHoaDonViewmodel: ChiTietHoaDonViewmodel
) {
    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }
    val customerId by dataStoreManager.customerId.collectAsState(initial = null)
    val hoaDonList by hoaDonBanViewModel.danhSachHoaDonCuaKhachHang.collectAsState()
    val chiTietCache = remember { mutableStateMapOf<Int, List<ChiTietHoaDon>>() }
    val sanPhamCache = remember { mutableStateMapOf<Int, SanPham>() } // key: MaSanPham
    val loadingChiTiet = remember { mutableStateMapOf<Int, Boolean>() }
    val loadingSanPham = remember { mutableStateMapOf<Int, Boolean>() }
    Log.d("OrderStatusScreen", "Danh sách đơn hàng trả về: ${hoaDonList}")

    val tabs = listOf("Chờ xác nhận", "Đang giao", "Đã giao", "Đã hủy")
    val selectedTab = remember { mutableStateOf(0) }

    LaunchedEffect (customerId){
        Log.d("OrderStatusScreen", "Danh sách đơn hàng trả về: ${customerId}")
    }
    Log.d("listcm","$hoaDonList")
    if (hoaDonList.isEmpty()) {
        Text("Không có hóa đơn.")
    } else {
        LazyColumn {
            items(hoaDonList) { hoaDon ->
                Text("Mã hóa đơn: ${hoaDon.MaHoaDon}")
            }
        }
    }
    LaunchedEffect (Unit){
    }
    LaunchedEffect(Unit) {
        Log.d("OrderStatusScreen", "Gọi API lấy đơn hàng với customerId=$customerId, tab=${selectedTab.value}")
        hoaDonBanViewModel.getHoaDonTheoKhachHang(customerId?:"", selectedTab.value)
        Log.d("listcm","$hoaDonList")
    }
    LaunchedEffect(selectedTab.value, customerId) {
        Log.d("OrderStatusScreen", "Gọi API lấy đơn hàng với customerId=$customerId, tab=${selectedTab.value}")
        customerId?.let {
            hoaDonBanViewModel.getHoaDonTheoKhachHang(it, selectedTab.value)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Trạng thái đơn hàng") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screens.HOMEPAGE.route)
                    }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                })
        }
    ) { paddingValues ->

        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
        ) {
            TabRow(
                selectedTabIndex = selectedTab.value
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab.value == index,
                        onClick = { selectedTab.value = index },
                        text = { Text(title) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(hoaDonList.size) { index ->
                    val hoaDon = hoaDonList[index]
                    val chiTietList = chiTietCache[hoaDon.MaHoaDon] ?: emptyList()
                    val isLoading = loadingChiTiet[hoaDon.MaHoaDon] ?: false
                    LaunchedEffect(hoaDon.MaHoaDon) {
                        if (!chiTietCache.containsKey(hoaDon.MaHoaDon) && !isLoading) {
                            loadingChiTiet[hoaDon.MaHoaDon] = true
                            chiTietHoaDonViewmodel.getChiTietHoaDonTheoMaHoaDon(hoaDon.MaHoaDon)
                            chiTietCache[hoaDon.MaHoaDon] = chiTietHoaDonViewmodel.danhsachchitethoadon
                            loadingChiTiet[hoaDon.MaHoaDon] = false
                        }
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Ngày đặt: ${hoaDon.NgayDatHang}")
                            Text("Tổng tiền: ${hoaDon.TongTien / 1000}.000 VNĐ")
                            Text("Phương thức thanh toán: ${hoaDon.PhuongThucThanhToan}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Sản phẩm:", fontWeight = FontWeight.Bold)
                            if (isLoading) {
                                Text("Đang tải sản phẩm...")
                            } else if (chiTietList.isEmpty()) {
                                Text("Không có sản phẩm.")
                            } else {
                                chiTietList.forEach { ct ->
                                    val sp = sanPhamCache[ct.MaSanPham]
                                    val isLoadingSanPham = loadingSanPham[ct.MaSanPham] ?: false
                                    
                                    LaunchedEffect(ct.MaSanPham) {
                                        if (sp == null && !isLoadingSanPham) {
                                            loadingSanPham[ct.MaSanPham] = true
                                            try {
                                                sanPhamViewModel.getSanPhamById(ct.MaSanPham.toString())
                                                sanPhamViewModel.sanPham?.let { 
                                                    sanPhamCache[ct.MaSanPham] = it
                                                    Log.d("OrderStatusScreen", "Đã load sản phẩm: ${it.TenSanPham}, Hình ảnh: ${it.HinhAnh}")
                                                }
                                            } catch (e: Exception) {
                                                Log.e("OrderStatusScreen", "Lỗi load sản phẩm ${ct.MaSanPham}: ${e.message}")
                                                // Thử lại sau 2 giây nếu có lỗi
                                                kotlinx.coroutines.delay(2000)
                                                if (sanPhamCache[ct.MaSanPham] == null) {
                                                    try {
                                                        sanPhamViewModel.getSanPhamById(ct.MaSanPham.toString())
                                                        sanPhamViewModel.sanPham?.let { 
                                                            sanPhamCache[ct.MaSanPham] = it
                                                            Log.d("OrderStatusScreen", "Đã load lại sản phẩm: ${it.TenSanPham}")
                                                        }
                                                    } catch (retryException: Exception) {
                                                        Log.e("OrderStatusScreen", "Lỗi load lại sản phẩm ${ct.MaSanPham}: ${retryException.message}")
                                                    }
                                                }
                                            } finally {
                                                loadingSanPham[ct.MaSanPham] = false
                                            }
                                        }
                                    }
                                    
                                    val sanPham = sanPhamCache[ct.MaSanPham]
                                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                        if (isLoadingSanPham) {
                                            Text("Đang tải sản phẩm...")
                                        } else if (sanPham != null && sanPham.HinhAnh.isNotEmpty()) {
                                            AsyncImage(
                                                model = sanPham.HinhAnh,
                                                contentDescription = sanPham.TenSanPham,
                                                modifier = Modifier.size(60.dp),
                                                contentScale = ContentScale.Crop
                                            )
                                            Column(modifier = Modifier.padding(start = 8.dp)) {
                                                Text(sanPham.TenSanPham, fontWeight = FontWeight.Medium)
                                                Text("Giá: ${sanPham.Gia / 1000}.000 VNĐ")
                                                Text("Số lượng: ${ct.SoLuong}")
                                                Text("Thành tiền: ${ct.ThanhTien / 1000}.000 VNĐ")
                                            }
                                        } else if (sanPham != null) {
                                            // Sản phẩm có thông tin nhưng không có hình ảnh
                                            Column(modifier = Modifier.padding(start = 8.dp)) {
                                                Text(sanPham.TenSanPham, fontWeight = FontWeight.Medium)
                                                Text("Giá: ${sanPham.Gia / 1000}.000 VNĐ")
                                                Text("Số lượng: ${ct.SoLuong}")
                                                Text("Thành tiền: ${ct.ThanhTien / 1000}.000 VNĐ")
                                                Text("Không có hình ảnh", color = Color.Gray, fontSize = 12.sp)
                                            }
                                        } else {
                                            // Không thể tải thông tin sản phẩm
                                            Column(modifier = Modifier.padding(start = 8.dp)) {
                                                Text("Sản phẩm #${ct.MaSanPham}", fontWeight = FontWeight.Medium)
                                                Text("Số lượng: ${ct.SoLuong}")
                                                Text("Thành tiền: ${ct.ThanhTien / 1000}.000 VNĐ")
                                                Text("Không thể tải thông tin sản phẩm", color = Color.Red, fontSize = 12.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}


