package com.example.lapstore.viewmodels

import DonHangDayDuItem
import HoaDonDeleteRequest
import HoaDonUpdateTrangThaiRequest
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laptopstore.models.HoaDon
import com.example.laptopstore.RetrofitClient.LaptopStoreRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HoaDonViewModel : ViewModel() {

    // ================
    // STATE FLOW DATA
    // ================

    private val _danhSachHoaDonCuaKhachHang = MutableStateFlow<List<HoaDon>>(emptyList())
    val danhSachHoaDonCuaKhachHang: StateFlow<List<HoaDon>> = _danhSachHoaDonCuaKhachHang

    private val _danhSachHoaDonTheoTrangThai = MutableStateFlow<List<HoaDon>>(emptyList())
    val danhSachHoaDonTheoTrangThai: StateFlow<List<HoaDon>> = _danhSachHoaDonTheoTrangThai

    private val _maHoaDonState = MutableStateFlow<Int?>(null)
    val maHoaDonState: StateFlow<Int?> = _maHoaDonState

    private val _addHoaDonMessage = MutableStateFlow<String?>(null)
    val addHoaDonMessage: StateFlow<String?> = _addHoaDonMessage

    private val _donHangList = MutableStateFlow<List<DonHangDayDuItem>>(emptyList())
    val donHangList: StateFlow<List<DonHangDayDuItem>> = _donHangList

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    var HoaDonBan by mutableStateOf<HoaDon?>(null)
        private set

    // ===================
    // FUNCTIONALITY
    // ===================

    fun getHoaDonTheoKhachHang(maKhachHang: String, trangThai: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.hoaDonAPIService.getHoaDonByKhachHang(maKhachHang, trangThai)
                }
                _danhSachHoaDonCuaKhachHang.value = response.hoadon ?: emptyList()
            } catch (e: Exception) {
                Log.e("HoaDonViewModel", "Lỗi khi lấy hoadon: ${e.message}")
                _danhSachHoaDonCuaKhachHang.value = emptyList()
            }
        }
    }

    fun getHoaDonTheoTrangThai(trangThai: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.hoaDonAPIService.getHoaDonTheoTrangThai(trangThai)
                }
                _danhSachHoaDonTheoTrangThai.value = response.hoadon ?: emptyList()
            } catch (e: Exception) {
                Log.e("HoaDonViewModel", "Lỗi khi lấy hoadon theo trạng thái: ${e.message}")
                _danhSachHoaDonTheoTrangThai.value = emptyList()
            }
        }
    }

    fun getDonHangDayDuTheoKhachHang(maKhachHang: String) {
        viewModelScope.launch {
            try {
                val response = LaptopStoreRetrofitClient.hoaDonAPIService.getDonHangDayDuTheoKhachHang(maKhachHang)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.success == true) {
                        _donHangList.value = body.data ?: emptyList()
                        Log.d("HoaDonViewModel", "Loaded ${_donHangList.value.size} đơn hàng")
                    } else {
                        val errorMessage = body?.message ?: "API trả về success = false nhưng không có message"
                        _error.value = errorMessage
                        Log.e("HoaDonViewModel", "API error: $errorMessage")
                    }
                } else {
                    _error.value = "Response failed: ${response.code()} ${response.message()}"
                    Log.e("HoaDonViewModel", "API call failed: ${response.code()} ${response.message()}")
                }

            } catch (e: Exception) {
                _error.value = "Exception: ${e.message}"
                Log.e("HoaDonViewModel", "Exception: ${e.message}")
            }
        }
    }

    fun updateTrangThai(maHoaDon: Int, trangThai: Int) {
        viewModelScope.launch {
            try {
                val request = HoaDonUpdateTrangThaiRequest(maHoaDon, trangThai)
                val response = LaptopStoreRetrofitClient.hoaDonAPIService.updateTrangThai(request)
                if (response.success) {
                    // Cập nhật trạng thái trong danh sách
                    _danhSachHoaDonCuaKhachHang.value = _danhSachHoaDonCuaKhachHang.value.map {
                        if (it.MaHoaDon == maHoaDon) it.copy(TrangThai = trangThai) else it
                    }
                }
            } catch (e: Exception) {
                Log.e("HoaDonViewModel", "Lỗi update trạng thái: ${e.message}")
            }
        }
    }

    suspend fun addHoaDon(hoaDon: HoaDon): Int? {
        return try {
            val response = LaptopStoreRetrofitClient.hoaDonAPIService.addHoaDon(hoaDon)
            if (response.success && response.maHoaDon != null) {
                _maHoaDonState.value = response.maHoaDon
                _addHoaDonMessage.value = "Tạo hóa đơn thành công!"
                response.maHoaDon
            } else {
                _addHoaDonMessage.value = response.message
                null
            }
        } catch (e: Exception) {
            Log.e("HoaDonViewModel", "Lỗi khi thêm hóa đơn: ${e.message}")
            _addHoaDonMessage.value = "Lỗi kết nối: ${e.message}"
            null
        }
    }

    fun deleteHoaDon(maHoaDon: Int) {
        viewModelScope.launch {
            try {
                val deleteRequest = HoaDonDeleteRequest(maHoaDon)
                val response = LaptopStoreRetrofitClient.hoaDonAPIService.deleteHoaDon(deleteRequest)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.message == "Gio Hang Deleted") {
                        Log.d("HoaDonViewModel", "Đã xóa hóa đơn")
                        _danhSachHoaDonCuaKhachHang.value = _danhSachHoaDonCuaKhachHang.value.filter { it.MaHoaDon != maHoaDon }
                    } else {
                        Log.e("HoaDonViewModel", "Lỗi xóa: ${apiResponse?.message}")
                    }
                } else {
                    Log.e("HoaDonViewModel", "Response error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("HoaDonViewModel", "Exception: ${e.message}")
            }
        }
    }

    fun updateHoaDon(hoaDon: HoaDon) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.hoaDonAPIService.updateHoaDon(hoaDon)
                }
                if (response.success) {
                    _addHoaDonMessage.value = "Cập nhật thành công: ${response.message}"
                } else {
                    _addHoaDonMessage.value = "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                _addHoaDonMessage.value = "Lỗi khi cập nhật hóa đơn: ${e.message}"
                Log.e("HoaDonViewModel", "Lỗi khi cập nhật hóa đơn: ${e.message}")
            }
        }
    }

    fun getHoaDonByMaHoaDon(maHoaDon: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                HoaDonBan = LaptopStoreRetrofitClient.hoaDonAPIService.getHoaDonByMaHoaDon(maHoaDon)
            } catch (e: Exception) {
                Log.e("HoaDonViewModel", "Error getting HoaDon: ${e.message}")
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun resetTrangThai() {
        _addHoaDonMessage.value = null
        _maHoaDonState.value = null
    }
}
