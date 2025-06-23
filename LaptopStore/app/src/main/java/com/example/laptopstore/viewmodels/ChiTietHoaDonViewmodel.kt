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

    suspend fun addHoaDonChiTiet(chitiethoadon: ChiTietHoaDon): Boolean {
        return try {
            val response = LaptopStoreRetrofitClient.chiTietHoaDonAPIService.addChiTietHoaDon(chitiethoadon)
            chitiethoadonAddResult = if (response.success) {
                "Cập nhật thành công: ${response.message}"
            } else {
                "Cập nhật thất bại: ${response.message}"
            }
            response.success
        } catch (e: Exception) {
            Log.e("AddToCart", "Lỗi kết nối: ${e.message}")
            false
        }
    }


    fun getChiTietHoaDonTheoMaHoaDon(mahoadon: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.chiTietHoaDonAPIService.getChiTietHoaDoByMaHoaDon(mahoadon)
                }
                danhsachchitethoadon = response.chitiethoadon
            } catch (e: Exception) {
                // Xử lý lỗi nếu có
                Log.e("HinhAnhError", "Lỗi khi lấy hình ảnh: ${e.message}")
            }
        }
    }
}