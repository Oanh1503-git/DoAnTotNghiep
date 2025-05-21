package com.example.laptopstore.viewmodels

import com.example.laptopstore.RetrofitClient.LaptopStoreRetrofitClient


import KiemTraTaiKhoanResponse
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapstore.models.TaiKhoan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TaiKhoanViewModel:ViewModel() {
    var taikhoan: TaiKhoan? by mutableStateOf(null)
        private set

    private val _loginResult = mutableStateOf<KiemTraTaiKhoanResponse?>(null)
    val loginResult: State<KiemTraTaiKhoanResponse?> = _loginResult

    private val _checkUsernameResult = mutableStateOf<KiemTraTaiKhoanResponse?>(null)
    val checkUsernameResult: State<KiemTraTaiKhoanResponse?> = _checkUsernameResult

    var taikhoanUpdateResult by mutableStateOf("")

    var tentaikhoan: String? = null

    var TaoTaiKhoanResult by mutableStateOf("")

    fun kiemTraDangNhap(tenTaiKhoan: String, matKhau: String) {
        viewModelScope.launch {
            try {
                // Thực hiện yêu cầu API
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.taiKhoanAPIService.kiemTraDangNhap(tenTaiKhoan, matKhau)
                }
                // Cập nhật kết quả API vào state
                _loginResult.value = response
            } catch (e: Exception) {
                // Xử lý lỗi nếu có
                Log.e("TaiKhoanViewModel", "Đã xảy ra lỗi: ${e.message}")
                _loginResult.value = KiemTraTaiKhoanResponse(result = false, message = e.message)
            }
        }
    }

    fun kiemTraTrungUsername(tenTaiKhoan: String) {
        viewModelScope.launch {
            try {
                // Thực hiện yêu cầu API
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.taiKhoanAPIService.kiemTraTrunUsername(tenTaiKhoan)
                }
                // Cập nhật kết quả API vào state
                _checkUsernameResult.value = response
            } catch (e: Exception) {
                // Xử lý lỗi nếu có
                Log.e("TaiKhoanViewModel", "Đã xảy ra lỗi: ${e.message}")
                _checkUsernameResult.value = KiemTraTaiKhoanResponse(result = false, message = e.message)
            }
        }
    }

    fun getTaiKhoanByTentaikhoan(tentaikhoan: String) {
        this.tentaikhoan = tentaikhoan
        viewModelScope.launch(Dispatchers.IO) {
            try {
                taikhoan = LaptopStoreRetrofitClient.taiKhoanAPIService.getTaiKhoanByTentaikhoan(tentaikhoan)
            } catch (e: Exception) {
                Log.e("SanPhamViewModel", "Error getting SanPham", e)
            }
        }
    }

    fun updateTaiKhoan(taiKhoan: TaiKhoan) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.taiKhoanAPIService.updateTaiKhoan(taiKhoan)
                }
                taikhoanUpdateResult = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                taikhoanUpdateResult = "Lỗi khi cập nhật giỏ hàng: ${e.message}"
                Log.e("GioHang Error", "Lỗi khi cập nhật giỏ hàng: ${e.message}")
            }
        }
    }

    fun TaoTaiKhoan(taikhoan: TaiKhoan) {
        viewModelScope.launch {
            try {
                val response = LaptopStoreRetrofitClient.taiKhoanAPIService.TaoTaiKhoan(taikhoan)
                TaoTaiKhoanResult = if (response.success) {
                    "Đăng ký thành công: ${response.message}"
                } else {
                    "Đăng ký thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                Log.e("Thêm tài khoản", "Lỗi kết nối: ${e.message}")
            }
        }
    }
}
