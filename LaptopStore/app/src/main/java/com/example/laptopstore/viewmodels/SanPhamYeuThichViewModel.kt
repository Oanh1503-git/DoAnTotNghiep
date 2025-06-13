package com.example.laptopstore.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laptopstore.RetrofitClient.LaptopStoreRetrofitClient
import com.example.laptopstore.models.HienSanPhamYeuThich
import com.example.laptopstore.models.SanPhamYeuThich
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SanPhamYeuThichViewModel : ViewModel() {
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _showfavorites = MutableStateFlow<List<HienSanPhamYeuThich>>(emptyList())
    val showfavorites: StateFlow<List<HienSanPhamYeuThich>> = _showfavorites.asStateFlow()
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun checkFavorite(productId: Int, customerId: String) {
        viewModelScope.launch {
            try {
                val response = LaptopStoreRetrofitClient.sanPhamYeuThichAPIService.checkFavorite(productId, customerId)
                _isFavorite.value = response.isFavorite
                Log.d("SanPhamYeuThichViewModel", "Checked favorite: $productId, isFavorite: ${response.isFavorite}")
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kiểm tra yêu thích: ${e.message}"
                Log.e("SanPhamYeuThichViewModel", "Error checking favorite: ${e.message}")
            }
        }
    }

    fun toggleFavorite(productId: Int, customerId: String) {
        viewModelScope.launch {
            try {
                if (_isFavorite.value) {
                    Log.d("SanPhamYeuThichViewModel", "Removing favorite: $productId")
                    val response = LaptopStoreRetrofitClient.sanPhamYeuThichAPIService.removeFavorite(
                        SanPhamYeuThich( productId, customerId)
                    )
                    if (response.message.contains("success", ignoreCase = true)) {
                        _isFavorite.value = false
                        Log.d("SanPhamYeuThichViewModel", "Favorite removed: $productId")
                    } else {
                        _errorMessage.value = "Không thể xóa yêu thích: ${response.message}"
                    }
                } else {
                    Log.d("SanPhamYeuThichViewModel", "Adding favorite: $productId")
                    val response = LaptopStoreRetrofitClient.sanPhamYeuThichAPIService.addFavorite(
                        SanPhamYeuThich( productId, customerId)
                    )
                    if (response.message.contains("success", ignoreCase = true)) {
                        _isFavorite.value = true
                        Log.d("SanPhamYeuThichViewModel", "Favorite added: $productId")
                    } else {
                        _errorMessage.value = "Không thể thêm yêu thích: ${response.message}"
                    }
                }
                getFavoritesByKhachHang(customerId)
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi khi thay đổi yêu thích: ${e.message}"
                Log.e("SanPhamYeuThichViewModel", "Error toggling favorite: ${e.message}")
            }
        }
    }

    fun getFavoritesByKhachHang(customerId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = LaptopStoreRetrofitClient.sanPhamYeuThichAPIService.getFavoritesByKhachHang(customerId)
                _showfavorites.value = response.hiensanphamyeuthich
                Log.d("SanPhamYeuThichViewModel", "Favorites loaded: ${response.hiensanphamyeuthich.size}")
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi tải danh sách yêu thích: ${e.message}"
                Log.e("SanPhamYeuThichViewModel", "Error loading favorites: ${e.message}")
            }
        }
    }
}
