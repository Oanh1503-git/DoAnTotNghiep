package com.example.laptopstore.viewmodels

import com.example.laptopstore.RetrofitClient.LaptopStoreRetrofitClient


import KiemTraTaiKhoanResponse
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapstore.models.SanPham
import com.example.lapstore.models.TaiKhoan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.lapstore.models.GioHang
import com.example.lapstore.models.HoaDonBan
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TaiKhoanViewModel:ViewModel() {
    // Thông tin tài khoản người dùng sau khi đăng nhập
    var taikhoan: TaiKhoan? by mutableStateOf(null)
        private set

    // Kết quả kiểm tra đăng nhập
    private val _loginResult = mutableStateOf<KiemTraTaiKhoanResponse?>(null)
    val loginResult: State<KiemTraTaiKhoanResponse?> = _loginResult

    // Trạng thái đăng nhập (null: chưa kiểm tra, true/false: kết quả)
    private val _isLoggedIn = mutableStateOf<Boolean?>(null)
    val isLoggedIn: State<Boolean?> = _isLoggedIn

    // Kết quả kiểm tra tên tài khoản khi đăng ký
    private val _checkUsernameResult = mutableStateOf<KiemTraTaiKhoanResponse?>(null)
    val checkUsernameResult: State<KiemTraTaiKhoanResponse?> = _checkUsernameResult

    // Biến lưu trạng thái tạo tài khoản mới
    var TaoTaiKhoanResult by mutableStateOf("")

    // Biến lưu trạng thái cập nhật tài khoản
    var taikhoanUpdateResult by mutableStateOf("")

    // Biến lưu tên tài khoản hiện tại (tạm thời)
    var tentaikhoan: String? = null

    // Hàm kiểm tra đăng nhập
    fun kiemTraDangNhap(tenTaiKhoan: String, matKhau: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.taiKhoanAPIService.kiemTraDangNhap(tenTaiKhoan, matKhau)
                }
                _loginResult.value = response
                _isLoggedIn.value = response.result
            } catch (e: Exception) {
                Log.e("TaiKhoanViewModel", "Đã xảy ra lỗi: ${e.message}")
                _loginResult.value = KiemTraTaiKhoanResponse(result = false, message = e.message)
                _isLoggedIn.value = false
            }
        }
    }

    fun kiemTraDangNhap1(tenTaiKhoan: String, matKhau: String) {
        viewModelScope.launch {
            try {
                // Thực hiện yêu cầu API
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.taiKhoanAPIService.kiemTraDangNhap(tenTaiKhoan, matKhau)
                }
                // Cập nhật kết quả API vào state
                _loginResult.value = response
                _isLoggedIn.value = response.result // true nếu đăng nhập thành công

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
