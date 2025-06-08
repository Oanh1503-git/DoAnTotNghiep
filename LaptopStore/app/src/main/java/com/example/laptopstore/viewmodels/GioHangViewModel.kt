package com.example.laptopstore.viewmodels

import DeleteRequest
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laptopstore.RetrofitClient.LaptopStoreRetrofitClient
import com.example.laptopstore.models.GioHang
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GioHangViewModel : ViewModel() {
    private val _listGioHang = MutableStateFlow<List<GioHang>>(emptyList())
    val listGioHang: StateFlow<List<GioHang>> = _listGioHang.asStateFlow()

    private val _giohangUpdateResult = MutableStateFlow("")
    val giohangUpdateResult: StateFlow<String> = _giohangUpdateResult.asStateFlow()

    private val _giohangAddResult = MutableStateFlow("")
    val giohangAddResult: StateFlow<String> = _giohangAddResult.asStateFlow()

    fun getGioHangByKhachHang(MaKhachHang: String) {
        if (MaKhachHang.isNullOrBlank()) {
            _giohangUpdateResult.value = "MaKhachHang không hợp lệ"
            return
        }
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.giohangAPIService.getGioHangByKhachHang(MaKhachHang)
                }
                Log.d("GioHangViewModel", "Danh sách giỏ hàng: $response")
                _listGioHang.value = response.giohang
            } catch (e: Exception) {
                _giohangUpdateResult.value = "Lỗi khi lấy giỏ hàng: ${e.message}"
                Log.e("GioHangError", "Lỗi khi lấy giỏ hàng: ${e.message}")
            }
        }
    }

    fun updateGioHang(gioHang: GioHang) {
        if (gioHang.MaGioHang <= 0 || gioHang.SoLuong <= 0) {
            _giohangUpdateResult.value = "Dữ liệu giỏ hàng không hợp lệ"
            return
        }
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.giohangAPIService.updateGioHang(gioHang)
                }
                _giohangUpdateResult.value = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                _giohangUpdateResult.value = "Lỗi khi cập nhật giỏ hàng: ${e.message}"
                Log.e("GioHangError", "Lỗi khi cập nhật giỏ hàng: ${e.message}")
            }
        }
    }

    fun updateAllGioHang() {
        if (_listGioHang.value.isEmpty()) {
            _giohangUpdateResult.value = "Giỏ hàng rỗng"
            return
        }
        viewModelScope.launch {
            try {
                var allSuccess = true
                _listGioHang.value.forEach { giohang ->
                    val response = withContext(Dispatchers.IO) {
                        LaptopStoreRetrofitClient.giohangAPIService.updateGioHang(giohang)
                    }
                    if (!response.success) {
                        allSuccess = false
                        Log.e("GioHangError", "Cập nhật thất bại cho sản phẩm: ${giohang.MaSanPham}")
                    }
                }
                _giohangUpdateResult.value = if (allSuccess) {
                    "Cập nhật toàn bộ giỏ hàng thành công"
                } else {
                    "Một số sản phẩm cập nhật thất bại"
                }
            } catch (e: Exception) {
                _giohangUpdateResult.value = "Lỗi khi cập nhật toàn bộ giỏ hàng: ${e.message}"
                Log.e("GioHangError", "Lỗi khi cập nhật toàn bộ giỏ hàng: ${e.message}")
            }
        }
    }

    fun deleteGioHang(maGioHang: Int) {
        if (maGioHang <= 0) {
            _giohangUpdateResult.value = "MaGioHang không hợp lệ"
            return
        }
        viewModelScope.launch {
            try {
                val deleteRequest = DeleteRequest(maGioHang)
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.giohangAPIService.deleteGioHang(deleteRequest)
                }
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.message == "Gio Hang Deleted") {
                        _listGioHang.value = _listGioHang.value.filter { it.MaGioHang != maGioHang }
                        _giohangUpdateResult.value = "Xóa giỏ hàng thành công"
                        Log.d("GioHangViewModel", "Giỏ hàng đã được xóa")
                    } else {
                        _giohangUpdateResult.value = "Lỗi: ${apiResponse?.message}"
                        Log.e("GioHangViewModel", "Lỗi: ${apiResponse?.message}")
                    }
                } else {
                    _giohangUpdateResult.value = "Lỗi: ${response.message()}"
                    Log.e("GioHangViewModel", "Lỗi: ${response.message()}")
                }
            } catch (e: Exception) {
                _giohangUpdateResult.value = "Lỗi khi xóa giỏ hàng: ${e.message}"
                Log.e("GioHangViewModel", "Lỗi: ${e.message}")
            }
        }
    }

    fun addToCart(gioHang: GioHang) {
        if (gioHang.MaSanPham <= 0 || gioHang.MaKhachHang.isNullOrBlank() || gioHang.SoLuong <= 0 || gioHang.TrangThai < 0) {
            _giohangAddResult.value = "Dữ liệu giỏ hàng không hợp lệ"
            return
        }
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.giohangAPIService.addToCart(gioHang)
                }
                _giohangAddResult.value = if (response.success) {
                    getGioHangByKhachHang(gioHang.MaKhachHang)
                    "Thêm vào giỏ hàng thành công: ${response.message}"
                } else {
                    "Thêm vào giỏ hàng thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                _giohangAddResult.value = "Lỗi khi thêm vào giỏ hàng: ${e.message}"
                Log.e("GioHangError", "Lỗi khi thêm vào giỏ hàng: ${e.message}")
            }

        }
    }
}