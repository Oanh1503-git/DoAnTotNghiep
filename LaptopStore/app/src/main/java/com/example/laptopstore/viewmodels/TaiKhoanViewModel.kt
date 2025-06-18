package com.example.laptopstore.viewmodels

import KiemTraTaiKhoanResponse
import LoginResult
import android.content.Context
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

sealed class LoginState {
    object Initial : LoginState()
    object Loading : LoginState()
    data class Success(val maKhachHang: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

class TaiKhoanViewModel(private val context: Context) : ViewModel() {

    private var dataStore: DataStoreManager = DataStoreManager(context)

    // State management using StateFlow
    private var _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _themKhachHangResult = MutableStateFlow<String?>(null)
    val themKhachHangResult: StateFlow<String?> = _themKhachHangResult

    private val _loginResult = MutableStateFlow<LoginResult?>(null)
    val loginResult: StateFlow<LoginResult?> = _loginResult

    private val _isLoggedIn = MutableStateFlow<Boolean>(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _taikhoan = MutableStateFlow<TaiKhoan?>(null)
    val taikhoan: StateFlow<TaiKhoan?> = _taikhoan

    private val _khachHang = MutableStateFlow<KhachHang?>(null)
    val khachHang: StateFlow<KhachHang?> = _khachHang

    private val _isThongTinDayDu = MutableStateFlow<Boolean?>(null)
    val isThongTinDayDu: StateFlow<Boolean?> = _isThongTinDayDu

    private val _tempAccountLogin = MutableStateFlow<Boolean>(false)
    val tempAccountLogin: StateFlow<Boolean> = _tempAccountLogin

    private val _tentaikhoan = MutableStateFlow<String?>(null)
    val tentaikhoan: StateFlow<String?> = _tentaikhoan

    private val _checkUsernameResult = MutableStateFlow<KiemTraTaiKhoanResponse?>(null)
    val checkUsernameResult: StateFlow<KiemTraTaiKhoanResponse?> = _checkUsernameResult

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Initial)
    val loginState: StateFlow<LoginState> = _loginState

    var TaoTaiKhoanResult: String by mutableStateOf("")
    var taikhoanUpdateResult: String by mutableStateOf("")

    init {
        initializeData()
    }

    private fun initializeData() {
        viewModelScope.launch {
            try {
                _isLoggedIn.value = dataStore.isLoggedIn.first()
                val savedUsername = dataStore.username.first()
                val savedCustomerId = dataStore.customerId.first()
                
                Log.d("TaiKhoanViewModel", "Initial login state: ${_isLoggedIn.value}, username: $savedUsername, customerId: $savedCustomerId")
                
                if (_isLoggedIn.value && !savedUsername.isNullOrEmpty()) {
                    _tentaikhoan.value = savedUsername
                    if (!savedCustomerId.isNullOrEmpty()) {
                        getTaiKhoanByTentaikhoan(savedUsername)
                        kiemTraThongTinKhachHang(savedCustomerId)
                    } else {
                        getTaiKhoanByTentaikhoan(savedUsername)
                    }
                }
            } catch (e: Exception) {
                Log.e("TaiKhoanViewModel", "Error initializing login state", e)
                _loginState.value = LoginState.Error("Lỗi khởi tạo: ${e.message}")
            }
        }
    }

    fun setIsLoggedIn(value: Boolean) {
        viewModelScope.launch {
            try {
                _isLoggedIn.value = value
                if (!value) {
                    dataStore.clearLoginState()
                    clearUserData()
                } else {
                    _loginState.value = LoginState.Success(_taikhoan.value?.MaKhachHang ?: "")
                    Log.d("TaiKhoanViewModel", "Login state updated: $value")
                }
            } catch (e: Exception) {
                Log.e("TaiKhoanViewModel", "Error setting login state: ${e.message}")
                _loginState.value = LoginState.Error("Lỗi cập nhật trạng thái đăng nhập: ${e.message}")
            }
        }
    }

    fun kiemTraDangNhap(tendangnhap: String, matkhau: String) {
        viewModelScope.launch {
            try {
                _loginState.value = LoginState.Loading
                val response = LaptopStoreRetrofitClient.taiKhoanAPIService.kiemTraDangNhap(tendangnhap, matkhau)
                
                if (response.result) {
                    _isLoggedIn.value = true
                    dataStore.saveLoginState(tendangnhap)
                    
                    val taiKhoanInfo = withContext(Dispatchers.IO) {
                        LaptopStoreRetrofitClient.taiKhoanAPIService.getTaiKhoanByTentaikhoan(tendangnhap)
                    }
                    
                    _taikhoan.value = taiKhoanInfo
                    
                    taiKhoanInfo.MaKhachHang?.let { maKH ->
                        dataStore.saveCustomerId(maKH)
                        kiemTraThongTinKhachHang(maKH)
                        _loginState.value = LoginState.Success(maKH)
                    } ?: run {
                        _loginState.value = LoginState.Error("Không tìm thấy mã khách hàng")
                    }
                    
                    _loginResult.value = LoginResult(true, "Đăng nhập thành công")
                } else {
                    _loginState.value = LoginState.Error(response.message ?: "Đăng nhập thất bại")
                    _loginResult.value = LoginResult(false, response.message ?: "Sai thông tin đăng nhập")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Lỗi: ${e.message}")
                _loginResult.value = LoginResult(false, "Lỗi: ${e.message}")
            }
        }
    }

    fun themKhachHang(khachHang: KhachHang) {
        viewModelScope.launch {
            try {
                val response = LaptopStoreRetrofitClient.khachHangAPIService.ThemKhachHang(khachHang)
                _themKhachHangResult.value = if (response.success) {
                    "Đăng ký thành công: ${response.message}"
                } else {
                    "Đăng ký không thành công: ${response.message}"
                }
            } catch (e: Exception) {
                _themKhachHangResult.value = "Lỗi kết nối: ${e.message}"
                Log.e("TaiKhoanViewModel", "Lỗi thêm khách hàng: ${e.message}")
            }
        }
    }

    fun getTaiKhoanByTentaikhoan(tentaikhoan: String) {
        viewModelScope.launch {
            try {
                val taiKhoanInfo = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.taiKhoanAPIService.getTaiKhoanByTentaikhoan(tentaikhoan)
                }
                _taikhoan.value = taiKhoanInfo
                
                taiKhoanInfo.MaKhachHang?.let { maKH ->
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
                Log.d("TaiKhoanViewModel", "Kiểm tra thông tin khách hàng: $maKH")
                val response = LaptopStoreRetrofitClient.khachHangAPIService.getKhachHangById(maKH)
                val khachHang = response.data
                
                if (khachHang == null) {
                    _isThongTinDayDu.value = false
                    _khachHang.value = null
                    return@launch
                }

                _khachHang.value = khachHang
                
                val isValid = listOf(
                    khachHang.HoTen,
                    khachHang.GioiTinh,
                    khachHang.NgaySinh,
                    khachHang.Email,
                    khachHang.SoDienThoai
                ).all { !it.isNullOrBlank() && it.lowercase() != "null" }

                _isThongTinDayDu.value = isValid
            } catch (e: Exception) {
                Log.e("TaiKhoanViewModel", "Lỗi kiểm tra thông tin khách hàng: ${e.message}")
                _isThongTinDayDu.value = false
                _khachHang.value = null
            }
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

   suspend fun TaoTaiKhoan(taiKhoan: TaiKhoan):Boolean {
            return try {
                val response = LaptopStoreRetrofitClient.taiKhoanAPIService.TaoTaiKhoan(taiKhoan)
                TaoTaiKhoanResult = if (response.success) {
                    "Đăng ký thành công: ${response.message}"
                } else {
                    "Đăng ký thất bại: ${response.message}"
                }
                response.success
            } catch (e: Exception) {
                TaoTaiKhoanResult = "Lỗi tạo tài khoản: ${e.message}"
                Log.e("TaiKhoanViewModel", "Lỗi tạo tài khoản: ${e.message}")
                false
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

    fun setTempAccountLogin(value: Boolean) {
        _tempAccountLogin.value = value
    }

    fun logout() {
        viewModelScope.launch {
            dataStore.clearLoginState()
            clearUserData()
        }
    }

    private fun clearUserData() {
        _taikhoan.value = null
        _khachHang.value = null
        _tentaikhoan.value = null
        _isThongTinDayDu.value = null
        _loginResult.value = null
        _loginState.value = LoginState.Initial
        _tempAccountLogin.value = false
        _isLoggedIn.value = false
    }

    fun resetLoginResult() {
        _loginResult.value = null
    }

    fun resetCheckResult() {
        _checkUsernameResult.value = null
    }
}
