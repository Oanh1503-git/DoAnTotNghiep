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
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    fun getHinhAnhTheoSanPham(MaSanPham: Int) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.hinhAnhAPIService.getHinhAnhBySanPham(MaSanPham)
                }
                danhSachHinhAnhTheoSanPham = response.hinhanh
            } catch (e: Exception) {
                errorMessage = "Lỗi khi lấy hình ảnh: ${e.message}"
                Log.e("HinhAnhError", "Lỗi khi lấy hình ảnh: ${e.message}")
            } finally {
                isLoading = false
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