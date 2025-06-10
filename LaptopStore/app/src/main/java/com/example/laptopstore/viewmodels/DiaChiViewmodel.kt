package com.example.lapstore.viewmodels

import DeleteDiaChiRequest
import UpdateMacDinhRequest
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laptopstore.RetrofitClient.LaptopStoreRetrofitClient
import com.example.laptopstore.models.DiaChi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiaChiViewmodel : ViewModel() {

    // StateFlow quản lý danh sách địa chỉ phục vụ cho UI Compose auto update
    private val _listDiaChi = MutableStateFlow<List<DiaChi>>(emptyList())
    val listDiaChi: StateFlow<List<DiaChi>> get() = _listDiaChi

    // Địa chỉ đang thao tác (xem/sửa)
    var diaChi by mutableStateOf<DiaChi?>(null)
        private set

    // Kết quả thêm/sửa địa chỉ
    var diaChiAddResult by mutableStateOf("")
    var diaChiUpdateResult by mutableStateOf("")

    // Lấy địa chỉ theo mã địa chỉ
    fun getDiaChiByMaDiaChi(maDiaChi: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = LaptopStoreRetrofitClient.diaChiAPIService.getDiaChiByMaDiaChi(maDiaChi)
                diaChi = result
            } catch (e: Exception) {
                Log.e("DiaChiViewmodel", "Error getting DiaChi by ID: ${e.message}")
            }
        }
    }

    // Lấy danh sách địa chỉ theo mã khách hàng
    fun getDiaChiKhachHang(maKhachHang: String?) {
        viewModelScope.launch {
            try {
                if (maKhachHang.isNullOrBlank()) {
                    Log.e("DiaChiViewmodel", "MaKhachHang is null or blank")
                    return@launch
                }

                Log.d("DiaChiViewmodel", "Fetching addresses for customer: $maKhachHang")
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.diaChiAPIService.getDiaChiByMaKhachHang(maKhachHang)
                }
                response.diachi?.let {addresses->
                    _listDiaChi.value=addresses
                    Log.d("DiaChiViewmodel","Received ${addresses.size} addresses")

                } ?: run {
                    Log.e("DiaChiViewmodel", "No addresses found or null response")
                    _listDiaChi.value = emptyList()
                }// Lúc này mới dùng .data

            } catch (e: Exception) {
                Log.e("DiaChiViewmodel", "Error fetching addresses: ${e.message}")
                // Don't clear the list on error
                _listDiaChi.value = emptyList()
            }
        }
    }

    // Làm mới danh sách địa chỉ (gọi lại API)
    fun refreshDiaChiKhachHang(maKhachHang: String?) {
        getDiaChiKhachHang(maKhachHang)
    }

    // Lấy địa chỉ mặc định theo mã khách hàng
    fun getDiaChiMacDinh(maKhachHang: String?, macDinh: Int?) {
        if (maKhachHang == null || macDinh == null) {
            Log.e("DiaChiViewmodel", "Tham số MaKhachHang hoặc MacDinh bị null")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = LaptopStoreRetrofitClient.diaChiAPIService.getDiaChiMacDinh(
                    MaKhachHang = maKhachHang,
                    MacDinh = macDinh
                )
                diaChi = result
            } catch (e: Exception) {
                Log.e("DiaChiViewmodel", "Lỗi khi lấy địa chỉ mặc định: ${e.message}")
            }
        }
    }

    // Đặt một địa chỉ là mặc định
    fun setDiaChiMacDinh(maKhachHang: String?, maDiaChi: Int) {
        viewModelScope.launch {
            try {
                if (maKhachHang != null) {
                    val diaChiRequest = DiaChi(
                        MaDiaChi = maDiaChi,
                        MaKhachHang = maKhachHang,
                        TenNguoiNhan = "",        // Hoặc giá trị hợp lệ
                        SoDienThoai = "",
                        ThongTinDiaChi = "",
                        MacDinh = 1               // Vì bạn đang muốn đặt mặc định
                    )

                    val response = LaptopStoreRetrofitClient.diaChiAPIService.setdiachimacdich(diaChiRequest)

                    // Kiểm tra phản hồi thành công
                    if (response.message == "Cập nhật địa chỉ mặc định thành công") {
                        // Làm mới danh sách địa chỉ
                        getDiaChiKhachHang(maKhachHang)
                    } else {
                        Log.e("DiaChiViewmodel", "Lỗi cập nhật: ${response.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e("DiaChiViewmodel", "Lỗi khi đặt địa chỉ mặc định: ${e.message}")
            }
        }
    }

    // Thêm địa chỉ mới
    fun addDiaChi(diaChi: DiaChi, maKhachHang: String) {
        viewModelScope.launch {
            try {
                val response = LaptopStoreRetrofitClient.diaChiAPIService.addDiaChi(diaChi)
                diaChiAddResult = if (response.success) {
                    getDiaChiKhachHang(maKhachHang)
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                diaChiAddResult = "Lỗi kết nối: ${e.message}"
                Log.e("DiaChiViewmodel", "Lỗi khi thêm địa chỉ: ${e.message}")
            }
        }
    }

    // Sửa địa chỉ
    fun updateDiaChi(diaChi: DiaChi, maKhachHang: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.diaChiAPIService.updateDiaChi(diaChi)
                }
                diaChiUpdateResult = if (response.success) {
                    getDiaChiKhachHang(maKhachHang)
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                diaChiUpdateResult = "Lỗi khi cập nhật địa chỉ: ${e.message}"
                Log.e("DiaChiViewmodel", "Lỗi khi cập nhật địa chỉ: ${e.message}")
            }
        }
    }

    // Xóa địa chỉ
    fun deleteDiaChi(maDiaChi: Int, maKhachHang: String) {
        viewModelScope.launch {
            try {
                val deleteRequest = DeleteDiaChiRequest(maDiaChi)
                val response = LaptopStoreRetrofitClient.diaChiAPIService.deleteDiaChi(deleteRequest)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.message == "Dia chi Deleted") {
                        getDiaChiKhachHang(maKhachHang)
                        Log.d("DiaChiViewmodel", "Địa chỉ đã được xóa")
                    } else {
                        Log.e("DiaChiViewmodel", "Lỗi: ${apiResponse?.message}")
                    }
                } else {
                    Log.e("DiaChiViewmodel", "Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("DiaChiViewmodel", "Exception: ${e.message}")
            }
        }
    }

    // Kiểm tra địa chỉ đã tồn tại (trùng lặp)
    fun isDiaChiDuplicated(soDienThoai: String, thongTinDiaChi: String): Boolean {
        return _listDiaChi.value.any {
            it.SoDienThoai == soDienThoai && it.ThongTinDiaChi == thongTinDiaChi
        }
    }

    // Xóa toàn bộ trạng thái kết quả, thường gọi khi mở form mới
    fun clearDiaChiResult() {
        diaChiAddResult = ""
        diaChiUpdateResult = ""
    }

    // Tìm kiếm địa chỉ theo tên người nhận hoặc số điện thoại
    fun searchDiaChi(query: String): List<DiaChi> {
        val lowerQuery = query.trim().lowercase()
        return _listDiaChi.value.filter {
            it.TenNguoiNhan.lowercase().contains(lowerQuery) ||
                    it.SoDienThoai.contains(lowerQuery)
        }
    }

    // Lấy địa chỉ mặc định duy nhất (nếu có)
    fun getOnlyMacDinh(): DiaChi? {
        return _listDiaChi.value.find { it.MacDinh == 1 }
    }
}