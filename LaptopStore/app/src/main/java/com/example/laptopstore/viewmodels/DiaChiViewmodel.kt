package com.example.lapstore.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laptopstore.models.DiaChi
import com.example.laptopstore.models.DiaChiResponse
import com.example.laptopstore.models.DiaChiUpdateResponse
import com.example.laptopstore.RetrofitClient.LaptopStoreRetrofitClient
import com.example.laptopstore.api.DeleteDiaChiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiaChiViewmodel : ViewModel() {
    var listDiacHi by mutableStateOf<List<DiaChi>>(emptyList())
        private set

    var diachi by mutableStateOf<DiaChi?>(null)
        private set

    var diachiAddResult by mutableStateOf("")
        private set
        
    var diachiUpdateResult by mutableStateOf("")
        private set

    private val _danhsachDiaChi = MutableStateFlow<List<DiaChi>>(emptyList())
    val danhsachDiaChi: StateFlow<List<DiaChi>> get() = _danhsachDiaChi

    fun getDiaChiByMaKhachHang(maKhachHang: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = LaptopStoreRetrofitClient.diaChiAPIService.getDiaChiByMaKhachHang(maKhachHang)
                listDiacHi = response.diachi ?: emptyList()
            } catch (e: Exception) {
                Log.e("DiaChiViewModel", "Lỗi lấy danh sách địa chỉ", e)
                listDiacHi = emptyList()
            }
        }
    }

    fun getDiaChiByMaDiaChi(madiachi: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = LaptopStoreRetrofitClient.diaChiAPIService.getDiaChiByMaDiaChi(madiachi)
                diachi = response.diachi?.firstOrNull()
            } catch (e: Exception) {
                Log.e("DiaChiViewModel", "Lỗi lấy địa chỉ", e)
            }
        }
    }

    fun getDiaChiMacDinh(maKhachHang: Int?, macDinh: Int?) {
        if (maKhachHang == null || macDinh == null) {
            Log.e("DiaChiViewModel", "Tham số MaKhachHang hoặc MacDinh bị null")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = LaptopStoreRetrofitClient.diaChiAPIService.getDiaChiMacDinh(
                    MaKhachHang = maKhachHang,
                    MacDinh = macDinh
                )
                diachi = response.diachi?.firstOrNull()
                Log.d("DiaChiViewModel", "Đã lấy địa chỉ thành công: $diachi")
            } catch (e: Exception) {
                Log.e("DiaChiViewModel", "Lỗi khi lấy địa chỉ mặc định", e)
            }
        }
    }

    suspend fun themDiaChi(diaChi: DiaChi) {
        try {
            val response = withContext(Dispatchers.IO) {
                LaptopStoreRetrofitClient.diaChiAPIService.themDiaChi(diaChi)
            }
            diachiAddResult = if (response.success) {
                getDiaChiByMaKhachHang(diaChi.MaKhachHang) // Refresh danh sách
                "Thêm địa chỉ thành công"
            } else {
                "Thêm địa chỉ thất bại: ${response.message}"
            }
        } catch (e: Exception) {
            diachiAddResult = "Lỗi khi thêm địa chỉ: ${e.message}"
            Log.e("DiaChiViewModel", "Lỗi thêm địa chỉ", e)
        }
    }

    suspend fun setDefaultAddress(maDiaChi: Int) {
        try {
            val response = withContext(Dispatchers.IO) {
                LaptopStoreRetrofitClient.diaChiAPIService.setDefaultAddress(maDiaChi)
            }
            if (response.success) {
                // Refresh danh sách địa chỉ
                diachi?.MaKhachHang?.let { getDiaChiByMaKhachHang(it) }
            }
            diachiUpdateResult = if (response.success) {
                "Cập nhật địa chỉ mặc định thành công"
            } else {
                "Cập nhật địa chỉ mặc định thất bại: ${response.message}"
            }
        } catch (e: Exception) {
            diachiUpdateResult = "Lỗi khi cập nhật địa chỉ mặc định: ${e.message}"
            Log.e("DiaChiViewModel", "Lỗi cập nhật địa chỉ mặc định", e)
        }
    }

    suspend fun deleteDiaChi(maDiaChi: Int) {
        try {
            val response = withContext(Dispatchers.IO) {
                LaptopStoreRetrofitClient.diaChiAPIService.deleteDiaChi(DeleteDiaChiRequest(maDiaChi))
            }
            if (response.success) {
                // Refresh danh sách địa chỉ
                diachi?.MaKhachHang?.let { getDiaChiByMaKhachHang(it) }
            }
        } catch (e: Exception) {
            Log.e("DiaChiViewModel", "Lỗi xóa địa chỉ", e)
            throw e
        }
    }
}