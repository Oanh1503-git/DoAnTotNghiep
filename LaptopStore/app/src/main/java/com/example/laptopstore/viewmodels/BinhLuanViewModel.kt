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

    private val _danhGiaList = MutableStateFlow<List<BinhLuanDanhGia>>(emptyList())
    val danhGiaList: StateFlow<List<BinhLuanDanhGia>> = _danhGiaList
    fun getReviewsByProductId(productId: Int?) {
        if (productId == null || productId <= 0) {
            Log.e("BinhLuanViewModel", "Invalid productId: $productId")
            _reviewsByProductId.value = emptyList()
            return
        }

        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.binhLuanAPIService.getDanhGiaByMaSanPham(productId)
                }

                if (response.success && response.data != null) {
                    _reviewsByProductId.value = response.data
                    _danhGiaList.value = response.data
                    Log.d("BinhLuanViewModel", "Fetched ${response.data.size} đánh giá")
                } else {
                    _reviewsByProductId.value = emptyList()
                    _danhGiaList.value = emptyList()
                    Log.d("BinhLuanViewModel", "Không có đánh giá hoặc phản hồi thất bại")
                }

            } catch (e: Exception) {
                Log.e("BinhLuanViewModel", "Error fetching reviews: ${e.message}")
                _reviewsByProductId.value = emptyList()
                _danhGiaList.value = emptyList()
            }
        }
    }

    fun getDanhGiaByMaSanPham(maSanPham: Int) {
        viewModelScope.launch {
            try {
                val response = LaptopStoreRetrofitClient.binhLuanAPIService.getDanhGiaByMaSanPham(maSanPham)
                if (response.success && response.data != null) {
                    _danhGiaList.value = response.data
                } else {
                    _danhGiaList.value = emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _danhGiaList.value = emptyList()
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