package com.example.laptopstore.viewmodels

import DataStoreManager
import KiemTraTaiKhoanResponse
import LoginRequest
import LoginResult
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laptopstore.RetrofitClient.LaptopStoreRetrofitClient
import com.example.laptopstore.models.KhachHang
import com.example.laptopstore.models.TaiKhoan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaiKhoanViewModel : ViewModel {

    private lateinit var context: Context
    private lateinit var prefs: SharedPreferences
    private lateinit var dataStore: DataStoreManager

    // State management using StateFlow
    private val _loginResult = MutableStateFlow<LoginResult?>(null)
    val loginResult: StateFlow<LoginResult?> = _loginResult

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _taikhoan = MutableStateFlow<TaiKhoan?>(null)
    val taikhoan: StateFlow<TaiKhoan?> = _taikhoan

    private val _khachHang = MutableStateFlow<KhachHang?>(null)
    val khachHang: StateFlow<KhachHang?> = _khachHang

    private val _isThongTinDayDu = MutableStateFlow<Boolean?>(null)
    val isThongTinDayDu: StateFlow<Boolean?> = _isThongTinDayDu

    private val _tempAccountLogin = MutableStateFlow(false)
    val tempAccountLogin: StateFlow<Boolean> = _tempAccountLogin

    private val _tentaikhoan = MutableStateFlow<String?>(null)
    val tentaikhoan: StateFlow<String?> = _tentaikhoan

    private val _checkUsernameResult = MutableStateFlow<KiemTraTaiKhoanResponse?>(null)
    val checkUsernameResult: StateFlow<KiemTraTaiKhoanResponse?> = _checkUsernameResult

    var TaoTaiKhoanResult by mutableStateOf("")
    var taikhoanUpdateResult by mutableStateOf("")

    // Constructor không tham số (bắt buộc cho Jetpack Compose)
    constructor() : super()

    // Constructor có Context (sử dụng từ Factory)
    constructor(context: Context) : super() {
        initWithContext(context)
    }

    private fun initWithContext(context: Context) {
        this.context = context
        prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        dataStore = DataStoreManager(context)
        
        Log.d("TaiKhoanViewModel", "Initializing with context")
        viewModelScope.launch {
            try {
                _isLoggedIn.value = dataStore.isLoggedIn.first()
                val savedUsername = dataStore.username.first()
                Log.d("TaiKhoanViewModel", "Initial login state: ${_isLoggedIn.value}, username: $savedUsername")
                if (_isLoggedIn.value && savedUsername != null) {
                    _tentaikhoan.value = savedUsername
                    getTaiKhoanByTentaikhoan(savedUsername)
                }
            } catch (e: Exception) {
                Log.e("TaiKhoanViewModel", "Error initializing login state", e)
            }
        }
    }

    fun setTempAccountLogin(value: Boolean) {
        _tempAccountLogin.value = value
    }

    fun getTaiKhoanByTentaikhoan(tentaikhoan: String) {
        viewModelScope.launch {
            try {
                val taiKhoanInfo = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.taiKhoanAPIService.getTaiKhoanByTentaikhoan(tentaikhoan)
                }
                _taikhoan.value = taiKhoanInfo
                Log.d("TaiKhoanViewModel", "Lấy thông tin tài khoản thành công: $taiKhoanInfo")
                Log.d("TaiKhoanViewModel", "MaKhachHang: ${taiKhoanInfo?.MaKhachHang}")
                
                // Nếu có MaKhachHang, lấy thông tin khách hàng
                taiKhoanInfo?.MaKhachHang?.let { maKH ->
                    kiemTraThongTinKhachHang(maKH)
                }
            } catch (e: Exception) {
                Log.e("TaiKhoanViewModel", "Lỗi lấy tài khoản: ${e.message}")
            }
        }
    }

    private fun kiemTraThongTinKhachHang(maKH: String) {
        viewModelScope.launch {
            try {
                val kh = LaptopStoreRetrofitClient.khachHangAPIService.getKhachHangById(maKH)
                _khachHang.value = kh
                _isThongTinDayDu.value = listOf(
                    kh.HoTen,
                    kh.GioiTinh,
                    kh.NgaySinh,
                    kh.Email,
                    kh.SoDienThoai
                ).all { it.isNotBlank() && it.lowercase() != "null" }
            } catch (e: Exception) {
                _isThongTinDayDu.value = false
                Log.e("TaiKhoanViewModel", "Lỗi kiểm tra thông tin khách hàng: ${e.message}")
            }
        }
    }

    fun kiemTraDangNhap(tendangnhap: String, matkhau: String) {
        viewModelScope.launch {
            try {
                val response = LaptopStoreRetrofitClient.taiKhoanAPIService.kiemTraDangNhap(tendangnhap, matkhau)
                if (response.result) {
                    Log.d("TaiKhoanViewModel", "Đăng nhập thành công: $tendangnhap")
                    _isLoggedIn.value = true
                    dataStore.saveLoginState(tendangnhap)
                    saveLoginState(true, tendangnhap)
                    
                    getTaiKhoanByTentaikhoan(tendangnhap)
                    
                    _loginResult.value = LoginResult(true, "Đăng nhập thành công")
                    
                    Log.d("TaiKhoanViewModel", "Đã cập nhật isLoggedIn = true")
                } else {
                    Log.d("TaiKhoanViewModel", "Đăng nhập thất bại: ${response.message}")
                    _loginResult.value = LoginResult(false, response.message ?: "Sai thông tin đăng nhập")
                }
            } catch (e: Exception) {
                Log.e("TaiKhoanViewModel", "Lỗi đăng nhập", e)
                _loginResult.value = LoginResult(false, "Lỗi: ${e.message}")
            }
        }
    }

    private fun saveLoginState(isLoggedIn: Boolean, username: String? = null) {
        viewModelScope.launch {
            prefs.edit().apply {
                putBoolean("is_logged_in", isLoggedIn)
                username?.let { putString("username", it) }
                apply()
            }
        }
    }

    fun setIsLoggedIn(value: Boolean) {
        _isLoggedIn.value = value
    }

    fun logout() {
        viewModelScope.launch {
            dataStore.clearLoginState()
            prefs.edit().clear().apply()
            _isLoggedIn.value = false
            _taikhoan.value = null
            _khachHang.value = null
            _tentaikhoan.value = null
            _isThongTinDayDu.value = null
            _loginResult.value = null
        }
    }

    fun resetLoginResult() {
        _loginResult.value = null
    }

    fun loginThanhCong(taiKhoanMoi: TaiKhoan) {
        _taikhoan.value = taiKhoanMoi
        taiKhoanMoi.MaKhachHang?.let { kiemTraThongTinKhachHang(it) }
    }

    suspend fun kiemTraTrungUsernameBool(tenTaiKhoan: String): Boolean {
        return try {
            val response = withContext(Dispatchers.IO) {
                LaptopStoreRetrofitClient.taiKhoanAPIService.kiemTraTrunUsername(tenTaiKhoan)
            }
            response.result == true
        } catch (e: Exception) {
            Log.e("TaiKhoanViewModel", "Lỗi: ${e.message}")
            false
        }
    }

    fun kiemTraTrungUsername(tenTaiKhoan: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.taiKhoanAPIService.kiemTraTrunUsername(tenTaiKhoan)
                }
                _checkUsernameResult.value = response
            } catch (e: Exception) {
                _checkUsernameResult.value = KiemTraTaiKhoanResponse(result = false, message = e.message)
            }
        }
    }

    fun resetCheckResult() {
        _checkUsernameResult.value = null
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
                Log.e("TaoTaiKhoan", "Lỗi: ${e.message}")
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
                taikhoanUpdateResult = "Lỗi khi cập nhật tài khoản: ${e.message}"
                Log.e("TaiKhoanViewModel", taikhoanUpdateResult)
            }
        }
    }
    private fun saveMaKhachHang(maKhachHang: String) {
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            .edit()
            .putString("MaKhachHang", maKhachHang)
            .apply()
    }

    private fun getMaKhachHangFromPrefs(): String? {
        return context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            .getString("MaKhachHang", null)
    }

    fun clearMaKhachHang() {
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            .edit()
            .remove("MaKhachHang")
            .apply()
    }
}
