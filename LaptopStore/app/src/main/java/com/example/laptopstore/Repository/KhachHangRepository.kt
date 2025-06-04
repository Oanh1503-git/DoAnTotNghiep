package com.example.laptopstore.Repository


import com.example.laptopstore.RetrofitClient.LaptopStoreRetrofitClient
import com.example.laptopstore.models.KhachHang

class KhachHangRepository {
    private val api = LaptopStoreRetrofitClient.khachHangAPIService

    suspend fun getKhachHangById(id: String) = api.getKhachHangById(id)

    suspend fun updateKhachHang(khachHang: KhachHang) = api.updateKhachHang(khachHang)

    suspend fun themKhachHang(khachHang: KhachHang) = api.ThemKhachHang(khachHang)

    suspend fun getSoLuongKhachHang() = api.getSoLuongKhachHang()
}
