package com.example.laptopstore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laptopstore.RetrofitClient.LaptopStoreRetrofitClient
import com.example.laptopstore.models.SanPhamYeuThich
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SanPhamYeuThichViewModel : ViewModel() {
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _favorites = MutableStateFlow<List<SanPhamYeuThich>>(emptyList())
    val favorites: StateFlow<List<SanPhamYeuThich>> = _favorites.asStateFlow()

    fun checkFavorite(productId: Int, customerId: Int) {
        viewModelScope.launch {
            try {
                val response = LaptopStoreRetrofitClient.sanPhamYeuThichAPIService.checkFavorite(productId, customerId)
                _isFavorite.value = response.isFavorite
            } catch (e: Exception) {
                // Xử lý lỗi, ví dụ: ghi log
            }
        }
    }

    fun toggleFavorite(productId: Int, customerId: Int) {
        viewModelScope.launch {
            try {
                if (_isFavorite.value) {
                    LaptopStoreRetrofitClient.sanPhamYeuThichAPIService.removeFavorite(SanPhamYeuThich(0, productId, customerId))
                    _isFavorite.value = false
                } else {
                    LaptopStoreRetrofitClient.sanPhamYeuThichAPIService.addFavorite(SanPhamYeuThich(0, productId, customerId))
                    _isFavorite.value = true
                }
                // Cập nhật danh sách yêu thích
                getFavoritesByKhachHang(customerId)
            } catch (e: Exception) {
                // Xử lý lỗi
            }
        }
    }

    fun getFavoritesByKhachHang(customerId: Int) {
        viewModelScope.launch {
            try {
                val response = LaptopStoreRetrofitClient.sanPhamYeuThichAPIService.getFavoritesByKhachHang(customerId)
                _favorites.value = response.sanphamyeuthich
            } catch (e: Exception) {
                // Xử lý lỗi
            }
        }
    }
}