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

    // State
    private val _loginResult = MutableStateFlow<LoginResult?>(null)
    val loginResult: StateFlow<LoginResult?> = _loginResult

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    var taikhoan: TaiKhoan? by mutableStateOf(null)
        private set

    var taiKhoan by mutableStateOf<TaiKhoan?>(null)
        private set

    var khachHang by mutableStateOf<KhachHang?>(null)
        private set

    var isThongTinDayDu by mutableStateOf<Boolean?>(null)
        private set

    var TaoTaiKhoanResult by mutableStateOf("")
    var taikhoanUpdateResult by mutableStateOf("")

    var tentaikhoan: String? = null
        private set

    private val _checkUsernameResult = mutableStateOf<KiemTraTaiKhoanResponse?>(null)
    val checkUsernameResult: State<KiemTraTaiKhoanResponse?> = _checkUsernameResult

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
                    tentaikhoan = savedUsername
                    getTaiKhoanByTentaikhoan(savedUsername)
                }
            } catch (e: Exception) {
                Log.e("TaiKhoanViewModel", "Error initializing login state", e)
            }
        }
    }

    fun kiemTraDangNhap(tendangnhap: String, matkhau: String) {
        viewModelScope.launch {
            try {
                val response = LaptopStoreRetrofitClient.taiKhoanAPIService.kiemTraDangNhap(tendangnhap, matkhau)
                if (response.result) {
                    Log.d("TaiKhoanViewModel", "Đăng nhập thành công: $tendangnhap")
                    _loginResult.value = LoginResult(true, "Đăng nhập thành công")
                    dataStore.saveLoginState(tendangnhap)
                    _isLoggedIn.value = true
                    saveLoginState(true, tendangnhap)
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
        prefs.edit().apply {
            putBoolean("is_logged_in", isLoggedIn)
            username?.let { putString("username", it) }
            apply()
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
            taikhoan = null
            taiKhoan = null
            khachHang = null
            tentaikhoan = null
            isThongTinDayDu = null
            _loginResult.value = null
        }
    }

    fun resetLoginResult() {
        _loginResult.value = null
    }

    fun loginThanhCong(taiKhoanMoi: TaiKhoan) {
        taiKhoan = taiKhoanMoi
        taiKhoanMoi.MaKhachHang?.let { kiemTraThongTinKhachHang(it) }
    }

    private fun kiemTraThongTinKhachHang(maKH: Int) {
        viewModelScope.launch {
            try {
                val kh = LaptopStoreRetrofitClient.khachHangAPIService.getKhachHangById(maKH.toString())
                khachHang = kh
                isThongTinDayDu = listOf(
                    kh.HoTen,
                    kh.GioiTinh,
                    kh.NgaySinh,
                    kh.Email,
                    kh.SoDienThoai
                ).all { it.isNotBlank() && it.lowercase() != "null" }
            } catch (e: Exception) {
                isThongTinDayDu = false
            }
        }
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

    fun getTaiKhoanByTentaikhoan(tentaikhoan: String) {
        this.tentaikhoan = tentaikhoan
        viewModelScope.launch(Dispatchers.IO) {
            try {
                taikhoan = LaptopStoreRetrofitClient.taiKhoanAPIService.getTaiKhoanByTentaikhoan(tentaikhoan)
            } catch (e: Exception) {
                Log.e("TaiKhoanViewModel", "Lỗi lấy tài khoản: ${e.message}")
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
}
