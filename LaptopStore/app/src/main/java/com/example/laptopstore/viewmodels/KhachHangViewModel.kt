package com.example.laptopstore.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.lapstore.models.KhachHang
import com.example.laptopstore.RetrofitClient.LaptopStoreRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KhachHangViewModels: ViewModel(){
    var khachhang by mutableStateOf<KhachHang?>(null)
        private set

    var khachhangUpdateResult by mutableStateOf("")
        private set

    var ThemKhachHangResult by mutableStateOf("")

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
    fun getKhachHangById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                khachhang = LaptopStoreRetrofitClient.khachHangAPIService.getKhachHangById(id)
            } catch (e: Exception) {
                Log.e("KhachHangViewModel", "Error getting khachhang", e)
            }
        }
    }
    fun updateKhachHang(khachhang: KhachHang) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.khachHangAPIService.updateKhachHang(khachhang)
                }
                khachhangUpdateResult = if (response.success) {
                    "Cập nhật thành công: ${response.message}"
                } else {
                    "Cập nhật thất bại: ${response.message}"
                }
            } catch (e: Exception) {
                khachhangUpdateResult = "Lỗi khi cập nhật khách hàng: ${e.message}"
                Log.e("GioHang Error", "Lỗi khi cập nhật khách hàng: ${e.message}")
            }
        }
    }
    fun themkhachhang(khachhang: KhachHang)
    {
        viewModelScope.launch {
            try{
                val response=LaptopStoreRetrofitClient.khachHangAPIService.ThemKhachHang(khachhang)
                ThemKhachHangResult=if(response.success)
                {
                    "Đăng ký thành công: ${response.message}"
                }else{
                    "Đăng ký không thành công: ${response.message}"
                }
            }catch (e:Exception){
                Log.e("Thêm tài khoản", "Lỗi kết nối: ${e.message}")
            }
        }
    }
}