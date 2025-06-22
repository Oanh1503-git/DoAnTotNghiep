package com.example.laptopstore.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapstore.models.ChiTietHoaDon
import com.example.laptopstore.RetrofitClient.LaptopStoreRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChiTietHoaDonViewmodel:ViewModel() {
    var chitiethoadonAddResult by mutableStateOf("")

    var danhsachchitethoadon by mutableStateOf<List<ChiTietHoaDon>>(emptyList())

    fun addHoaDonChiTiet(chitiethoadonban: ChiTietHoaDon) {
        viewModelScope.launch {
            try {
                // Gọi API để thêm sản phẩm vào giỏ hàng trên server
                val response = LaptopStoreRetrofitClient.chiTietHoaDonBanAPIService.addChiTietHoaDonBan(chitiethoadonban)
                chitiethoadonAddResult = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                Log.e("AddToCart", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun getChiTietHoaDonTheoMaHoaDon(mahoadon: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.chiTietHoaDonBanAPIService.getChiTietHoaDoByMaHoaDon(mahoadon)
                }
                danhsachchitethoadon = response.chitiethoadon
            } catch (e: Exception) {
                // Xử lý lỗi nếu có
                Log.e("HinhAnhError", "Lỗi khi lấy hình ảnh: ${e.message}")
            }
        }
    }
}