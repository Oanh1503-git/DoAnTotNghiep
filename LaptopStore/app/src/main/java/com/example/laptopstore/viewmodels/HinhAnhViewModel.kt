package com.example.laptopstore.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laptopstore.models.HinhAnh
import com.example.laptopstore.RetrofitClient.LaptopStoreRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HinhAnhViewModel : ViewModel() {
    var danhSachHinhAnhTheoSanPham by mutableStateOf<List<HinhAnh>>(emptyList())

    fun getHinhAnhTheoSanPham(MaSanPham: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.hinhAnhAPIService.getHinhAnhBySanPham(MaSanPham)
                }
                danhSachHinhAnhTheoSanPham = response.hinhanh
            } catch (e: Exception) {
                Log.e("HinhAnhError", "Lỗi khi lấy hình ảnh: ${e.message}")
            }
        }
    }

    suspend fun getAllHinhAnh(): HinhAnhResponse {
        return withContext(Dispatchers.IO) {
            try {
                LaptopStoreRetrofitClient.hinhAnhAPIService.getAllHinhAnh() as HinhAnhResponse
            } catch (e: Exception) {
                Log.e("HinhAnhError", "Lỗi khi lấy tất cả hình ảnh: ${e.message}")
                HinhAnhResponse(emptyList()) // Đảm bảo constructor khớp
            }
        }
    }
}

data class HinhAnhResponse(
    val hinhanh: List<HinhAnh>
)