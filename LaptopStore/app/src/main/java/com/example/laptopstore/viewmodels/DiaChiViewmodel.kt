package com.example.lapstore.viewmodels

import DeleteDiaChiRequest
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapstore.models.DiaChi
import com.example.lapstore.models.GioHang
import com.example.lapstore.models.SanPham
import com.example.laptopstore.RetrofitClient.LaptopStoreRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiaChiViewmodel:ViewModel() {

    var listDiacHi by mutableStateOf<List<DiaChi>>(emptyList())

    var diachi by mutableStateOf<DiaChi?>(null)
        private set

    var diachiAddResult by mutableStateOf("")
    var diachiUpdateResult by mutableStateOf("")

    private val _danhsachDiaChi = MutableStateFlow<List<DiaChi>>(emptyList())
    val danhsachDiaChi: StateFlow<List<DiaChi>> get() = _danhsachDiaChi

    fun getDiaChiByMaDiaChi(madiachi: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                diachi = LaptopStoreRetrofitClient.diaChiAPIService.getDiaChiByMaDiaChi(madiachi)
            } catch (e: Exception) {
                Log.e("Dia Chi ViewModel", "Error getting Dia Chi", e)
            }
        }
    }

    fun getDiaChiByMaDiaChi2(madiachi: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val diachi = LaptopStoreRetrofitClient.diaChiAPIService.getDiaChiByMaDiaChi(madiachi)
                _danhsachDiaChi.update { currentList -> currentList + diachi }
            } catch (e: Exception) {
                Log.e("SanPhamViewModel", "Error getting SanPham", e)
            }
        }
    }

    fun getDiaChiKhachHang(MaKhachHang: Int?) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.diaChiAPIService.getDiaChiByMaKhachHang(MaKhachHang)
                }
                listDiacHi = response.diachi ?: emptyList() // Gán giá trị mảng rỗng nếu response.diachi null
            } catch (e: Exception) {
                Log.e("Dia Chi Error", "Lỗi khi lấy dia chi: ${e.message}")
                listDiacHi = emptyList()
            }
        }
    }

    fun getDiaChiMacDinh(maKhachHang: Int?, macDinh: Int?) {
        if (maKhachHang == null || macDinh == null) {
            Log.e("DiaChiViewModel", "Tham số MaKhachHang hoặc MacDinh bị null")
            return // Ngừng xử lý nếu tham số null
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                diachi = LaptopStoreRetrofitClient.diaChiAPIService.getDiaChiMacDinh(
                    MaKhachHang = maKhachHang,
                    MacDinh = macDinh
                )
                Log.d("DiaChiViewModel", "Đã lấy địa chỉ thành công: $diachi")
            } catch (e: Exception) {
                Log.e("DiaChiViewModel", "Lỗi khi lấy địa chỉ mặc định", e)
            }
        }
    }

    fun addDiaChi(diachi:DiaChi) {
        viewModelScope.launch {
            try {
                // Gọi API để thêm sản phẩm vào giỏ hàng trên server
                val response = LaptopStoreRetrofitClient.diaChiAPIService.addDiaChi(diachi)
                diachiAddResult = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                Log.e("Add Dia Chi", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun updateDiaChi(diachi: DiaChi) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.diaChiAPIService.updateDiaChi(diachi)
                }
                diachiUpdateResult = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                diachiUpdateResult = "Lỗi khi cập nhật giỏ hàng: ${e.message}"
                Log.e("GioHang Error", "Lỗi khi cập nhật giỏ hàng: ${e.message}")
            }
        }
    }

    fun updateDiaChiMacDinh(MaKhachHang: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.diaChiAPIService.updateDiaChiMacDinh(MaKhachHang)
                }
                diachiUpdateResult = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                diachiUpdateResult = "Lỗi khi cập nhật giỏ hàng: ${e.message}"
                Log.e("GioHang Error", "Lỗi khi cập nhật giỏ hàng: ${e.message}")
            }
        }
    }

    fun deleteDiaChi(madiachi: Int) {
        viewModelScope.launch {
            try {
                val deleteRequest = DeleteDiaChiRequest(madiachi)
                val response = LaptopStoreRetrofitClient.diaChiAPIService.deleteDiaChi(deleteRequest)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.message == "Dia chi Deleted") {
                        // Cập nhật lại giỏ hàng trong ViewModel
                        listDiacHi = listDiacHi.filter { it.MaDiaChi != madiachi }
                        Log.d("DiaChiViewModel", "Dia chi đã được xóa")
                    } else {
                        Log.e("DiaChiViewModel", "Lỗi: ${apiResponse?.message}")
                    }
                } else {
                    Log.e("DiaChiViewModel", "Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("DiaChiViewModel", "Exception: ${e.message}")
            }
        }
    }

}