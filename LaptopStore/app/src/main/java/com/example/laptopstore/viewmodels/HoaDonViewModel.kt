package com.example.lapstore.viewmodels

import DonHangDayDuResponse
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

class HoaDonViewModel: ViewModel() {
    // Kết quả thêm hóa đơn
    var hoadonAddResult by mutableStateOf("")

    // Thay đổi danh sách hóa đơn thành StateFlow
    private val _danhSachHoaDonCuaKhachHang = MutableStateFlow<List<HoaDon>>(emptyList())
    val danhSachHoaDonCuaKhachHang: StateFlow<List<HoaDon>> = _danhSachHoaDonCuaKhachHang

    private val _danhSachHoaDonTheoTrangThai = MutableStateFlow<List<HoaDon>>(emptyList())
    val danhSachHoaDonTheoTrangThai: StateFlow<List<HoaDon>> = _danhSachHoaDonTheoTrangThai

    private val _maHoaDonState = MutableStateFlow<Int?>(null)
    val maHoaDonState: StateFlow<Int?>get() = _maHoaDonState


    private val _addHoaDonMessage = MutableStateFlow<String?>(null)
    val addHoaDonMessage: StateFlow<String?> = _addHoaDonMessage

    var maHoaDon = maHoaDonState.value

    var HoaDonBan by mutableStateOf<HoaDon?>(null)
        private set
    private val _donHangDayDu = MutableStateFlow<List<DonHangDayDuResponse>>(emptyList())
    val donHangDayDu: StateFlow<List<DonHangDayDuResponse>> = _donHangDayDu

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Lấy hóa đơn theo khách hàng
    fun getHoaDonTheoKhachHang(MaKhachHang: String, TrangThai: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.hoaDonAPIService.getHoaDoByKhachHang(MaKhachHang, TrangThai)
                }
                _danhSachHoaDonCuaKhachHang.value = response.hoadon ?: emptyList() // Cập nhật StateFlow
            } catch (e: Exception) {
                Log.e("HoaDon Error", "Lỗi khi lấy hoadon: ${e.message}")
                _danhSachHoaDonCuaKhachHang.value = emptyList() // Gán danh sách rỗng khi có lỗi
            }
        }
    }

    fun getHoaDonTheoTrangThai(TrangThai: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.hoaDonAPIService.getHoaDonTheoTrangThai(TrangThai)
                }
                _danhSachHoaDonTheoTrangThai.value = response.hoadon ?: emptyList()
            } catch (e: Exception) {
                Log.e("HoaDon Error", "Lỗi khi lấy hoadon: ${e.message}")
                _danhSachHoaDonTheoTrangThai.value = emptyList()
            }
        }
    }
    fun updateTrangThai(maHoaDon: Int, trangThai: Int) {
        viewModelScope.launch {
            try {
                val request = HoaDonUpdateTrangThaiRequest(maHoaDon, trangThai)
                val response = LaptopStoreRetrofitClient.hoaDonAPIService.updateTrangThai(request)
                if (response.success) {
                    // Reload dữ liệu
                    _danhSachHoaDonCuaKhachHang.value = _danhSachHoaDonCuaKhachHang.value.map {
                        if (it.MaHoaDon == maHoaDon) it.copy(TrangThai = trangThai) else it
                    }
                }
            } catch (e: Exception) {
                Log.e("HoaDonViewModel", "Lỗi update trạng thái: ${e.message}")
            }
        }
    }

    fun getDonHangDayDu(maKhachHang: String) {
        viewModelScope.launch {
            try {
                val response = LaptopStoreRetrofitClient.hoaDonAPIService.getDonHangDayDuTheoKhachHang(maKhachHang)
                if (response.isSuccessful && response.body() != null) {
                    _donHangDayDu.value = response.body()!!
                } else {
                    _error.value = response.message()
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
            }
        }
    }

    fun resetTrangThai() {
        _addHoaDonMessage.value = null
        _maHoaDonState.value = null
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
            Log.e("HoaDonBanViewModel", "Lỗi khi thêm hóa đơn: ${e.message}")
            _addHoaDonMessage.value = "Lỗi kết nối: ${e.message}"
            null
        }
    }

    // Xóa hóa đơn
    fun deleteHoaDon(mahoadon: Int) {
        viewModelScope.launch {
            try {
                val deleteRequest = HoaDonDeleteRequest(mahoadon)
                val response = LaptopStoreRetrofitClient.hoaDonAPIService.deleteHoaDon(deleteRequest)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.message == "Gio Hang Deleted") {
                        Log.d("GioHangViewModel", "Giỏ hàng đã được xóa")
                        // Cập nhật danh sách sau khi xóa
                        _danhSachHoaDonCuaKhachHang.value = _danhSachHoaDonCuaKhachHang.value.filter { it.MaHoaDon != mahoadon }
                    } else {
                        Log.e("GioHangViewModel", "Lỗi: ${apiResponse?.message}")
                    }
                } else {
                    Log.e("GioHangViewModel", "Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("GioHangViewModel", "Exception: ${e.message}")
            }
        }
    }

    // Cập nhật hóa đơn
    fun updateHoaDonBan(hoadon: HoaDon) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.hoaDonAPIService.updateHoaDon(hoadon)
                }
                hoadonAddResult = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                hoadonAddResult = "Lỗi khi cập nhật giỏ hàng: ${e.message}"
                Log.e("GioHang Error", "Lỗi khi cập nhật giỏ hàng: ${e.message}")
            }
        }
    }

    fun getHoaDonByMaHoaDon(mahoadon: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                HoaDonBan = LaptopStoreRetrofitClient.hoaDonAPIService.getHoaDonByMaHoaDon(mahoadon)
            } catch (e: Exception) {
                Log.e("HoaDonBan ViewModel", "Error getting HoaDon", e)
            }
        }
    }
}