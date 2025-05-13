package com.example.lapstore.viewmodels
import DeleteRequest
import GioHangAPIService
import UpdateResponse
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapstore.models.GioHang
import com.example.laptopstore.RetrofitClient.LaptopStoreRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class GioHangViewModel : ViewModel() {
    var listGioHang by mutableStateOf<List<GioHang>>(emptyList())

    var giohangUpdateResult by mutableStateOf("")
    var giohangAddResult by mutableStateOf("")

    fun getGioHangByKhachHang(MaKhachHang: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.giohangAPIService.getGioHangByKhachHang(MaKhachHang)
                }
                listGioHang = response.giohang
            } catch (e: Exception) {
                Log.e("GioHang Error", "Lỗi khi lấy giỏ hàng: ${e.message}")
            }
        }
    }

    // Cập nhật giỏ hàng đơn lẻ
    fun updateGioHang(gioHang: GioHang) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.giohangAPIService.updateGioHang(gioHang)
                }
                giohangUpdateResult = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                giohangUpdateResult = "Lỗi khi cập nhật giỏ hàng: ${e.message}"
                Log.e("GioHang Error", "Lỗi khi cập nhật giỏ hàng: ${e.message}")
            }
        }
    }

    // Cập nhật tất cả giỏ hàng
    fun updateAllGioHang() {
        viewModelScope.launch {
            try {
                listGioHang.forEach { giohang ->
                    val response = LaptopStoreRetrofitClient.giohangAPIService.updateGioHang(giohang)
                    if (!response.success) {
                        // Nếu cập nhật thất bại, xử lý lỗi (hoặc thông báo lỗi)
                        Log.e("GioHang Error", "Cập nhật thất bại cho sản phẩm: ${giohang.MaSanPham}")
                    }
                }
                giohangUpdateResult = "Cập nhật giỏ hàng thành công"
            } catch (e: Exception) {
                giohangUpdateResult = "Lỗi khi cập nhật toàn bộ giỏ hàng: ${e.message}"
                Log.e("GioHang Error", "Lỗi khi cập nhật toàn bộ giỏ hàng: ${e.message}")
            }
        }
    }

    fun deleteGioHang(maGioHang: Int) {
        viewModelScope.launch {
            try {
                val deleteRequest = DeleteRequest(maGioHang)
                val response = LaptopStoreRetrofitClient.giohangAPIService.deleteGioHang(deleteRequest)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.message == "Gio Hang Deleted") {
                        // Cập nhật lại giỏ hàng trong ViewModel
                        listGioHang = listGioHang.filter { it.MaGioHang != maGioHang }
                        Log.d("GioHangViewModel", "Giỏ hàng đã được xóa")
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

    fun addToCart(giohang:GioHang) {
        viewModelScope.launch {
            try {
                // Gọi API để thêm sản phẩm vào giỏ hàng trên server
                val response = LaptopStoreRetrofitClient.giohangAPIService.addToCart(giohang)
                giohangAddResult = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                Log.e("AddToCart", "Lỗi kết nối: ${e.message}")
            }
        }
    }
}



