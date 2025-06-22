package com.example.laptopstore.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.laptopstore.RetrofitClient.LaptopStoreRetrofitClient
import com.example.laptopstore.models.KhachHang
import com.example.laptopstore.models.CustomerIDGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KhachHangViewModels : ViewModel() {

    // ✅ Hiển thị thông tin khách hàng cụ thể

    // ✅ Trạng thái cập nhật khách hàng
    var khachhangUpdateResult = MutableStateFlow<String?>(null)

    // ✅ Trạng thái thêm khách hàng mới (success / fail / error)
    private val _themKhachHangResult = MutableStateFlow<String?>(null)
    val themKhachHangMoiResult: StateFlow<String?> = _themKhachHangResult

    private val _khachhang = MutableStateFlow<KhachHang?>(null)
    val khachhang: StateFlow<KhachHang?> = _khachhang

    private val _makhachhang = MutableStateFlow<String?>(null)
    val makhachhang: StateFlow<String?> = _makhachhang

    private val _maKhachHangState = MutableStateFlow<String?>(null)
    val maKhachHangState: StateFlow<String?> = _maKhachHangState

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // ✅ Load tất cả khách hàng (LiveData)
    val allKhachHang: LiveData<List<KhachHang>> = liveData(Dispatchers.IO) {
        try {
            val response = LaptopStoreRetrofitClient.khachHangAPIService.getAllKhachHang().execute()
            if (response.isSuccessful) {
                emit(response.body()?.khachhang ?: emptyList())
            } else {
                emit(emptyList())
            }
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
    fun getMaKhachHangTuAPI() {
        viewModelScope.launch {
            try {
                val response = LaptopStoreRetrofitClient.khachHangAPIService.taoMaKhachHang()
                if (response.success) {
                    _makhachhang.value = response.ma_khach_hang
                    Log.d("KhachHang", "Mã khách hàng từ API: ${response.ma_khach_hang}")
                } else {
                    _makhachhang.value = null
                    Log.e("KhachHang", "API trả lỗi: ${response}")
                }
            } catch (e: Exception) {
                Log.e("KhachHang", "Lỗi kết nối API mã KH: ${e.message}")
                _makhachhang.value = null
            }
        }
    }


    // ✅ Lấy khách hàng theo mã
    fun getKhachHangById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = LaptopStoreRetrofitClient.khachHangAPIService.getKhachHangById(id)
                if (response.success) {
                    _khachhang.value = response.data
                    Log.d("KhachHangViewModel", "Lấy thông tin khách hàng thành công: ${response.data}")
                } else {
                    Log.e("KhachHangViewModel", "Lỗi: ${response.message}")
                    _khachhang.value = null
                }
            } catch (e: Exception) {
                Log.e("KhachHangViewModel", "Error getting khachhang", e)
                _khachhang.value = null
            }
        }
    }


    // ✅ Cập nhật khách hàng
    fun updateKhachHang(khachhang: KhachHang) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.khachHangAPIService.updateKhachHang(khachhang)
                }
                khachhangUpdateResult.value = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                khachhangUpdateResult.value = "Lỗi khi cập nhật khách hàng: ${e.message}"
                Log.e("KhachHang", "Lỗi khi cập nhật khách hàng", e)
            }
        }
    }

    // ✅ Thêm khách hàng mới
    suspend fun themkhachhang(khachhang: KhachHang): Boolean {
        return try {
            val response = LaptopStoreRetrofitClient.khachHangAPIService.ThemKhachHang(khachhang)
            if (response.success) {
                Log.d("KhachHang", "Tạo khách hàng thành công")
                _themKhachHangResult.value = "success"
                true
            } else {
                Log.e("KhachHang", "Tạo thất bại: ${response.message}")
                _themKhachHangResult.value = "fail"
                false
            }
        } catch (e: Exception) {
            Log.e("KhachHang", "Lỗi tạo khách hàng: ${e.message}")
            _themKhachHangResult.value = "error"
            false
        }
    }


    // ✅ Reset kết quả sau khi sử dụng xong
    fun resetThemKhachHangResult() {
        _themKhachHangResult.value = null
    }

    // ✅ Lấy số lượng khách hàng từ API
    private suspend fun getCustomerCount(): Int {
        return withContext(Dispatchers.IO) {
            try {
                val response = LaptopStoreRetrofitClient.khachHangAPIService.getSoLuongKhachHang()
                if (response.success) response.so_luong else 0
            } catch (e: Exception) {
                Log.e("KhachHang", "Lỗi khi lấy số lượng khách hàng", e)
                0
            }
        }
    }

    // ✅ Tạo mã khách hàng mới tự động
    suspend fun generateNewCustomerId(): String {
        val customerCount = getCustomerCount()
        return CustomerIDGenerator.generateCustomerId(customerCount)
    }
    fun taoMaKhachHang() {
        viewModelScope.launch {
            try {
                val response = LaptopStoreRetrofitClient.khachHangAPIService.taoMaKhachHang()
                if (response.success) {
                    _maKhachHangState.value = response.ma_khach_hang
                    Log.d("TaoMaKH", "Tạo mã KH thành công: ${response.ma_khach_hang}")
                } else {
                    _error.value = "Tạo mã thất bại"
                    _maKhachHangState.value = null
                }
            } catch (e: Exception) {
                _error.value = "Lỗi kết nối: ${e.localizedMessage}"
                _maKhachHangState.value = null
            } finally {
                _loading.value = false
            }
        }
    }

}
