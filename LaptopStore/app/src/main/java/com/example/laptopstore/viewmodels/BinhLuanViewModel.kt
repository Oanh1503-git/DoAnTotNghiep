package com.example.laptopstore.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laptopstore.RetrofitClient.LaptopStoreRetrofitClient
import com.example.laptopstore.models.BinhLuanDanhGia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class BinhLuanViewModel : ViewModel() {
    private val _reviewsByProductId = MutableStateFlow<List<BinhLuanDanhGia>>(emptyList())
    val reviewsByProductId: StateFlow<List<BinhLuanDanhGia>> = _reviewsByProductId.asStateFlow()

    fun getReviewsByProductId(productId: Int?) {
        if (productId == null || productId <= 0) {
            Log.e("BinhLuanViewModel", "Invalid productId: $productId")
            return
        }
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.binhLuanAPIService.getBinhLuanBySanPham(productId)
                }
                _reviewsByProductId.value = response.binhluan
            } catch (e: Exception) {
                Log.e("BinhLuanViewModel", "Error fetching reviews: ${e.message}")
            }
        }
    }

    fun createReview(review: BinhLuanDanhGia) {
        viewModelScope.launch {
            try {
                LaptopStoreRetrofitClient.binhLuanAPIService.createBinhLuan(review)
                // Cập nhật lại danh sách bình luận
                getReviewsByProductId(review.MaSanPham)
            } catch (e: Exception) {
                // Xử lý lỗi
            }
        }
    }
}