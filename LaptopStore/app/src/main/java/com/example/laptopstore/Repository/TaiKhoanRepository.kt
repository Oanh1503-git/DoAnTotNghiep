package com.example.laptopstore.Repository

import KiemTraTaiKhoanResponse
import TaiKhoanAPIService
import com.example.laptopstore.models.TaiKhoan
import taikhoanUpdateResponse

class TaiKhoanRepository(private val apiService: TaiKhoanAPIService) {

    suspend fun kiemTraDangNhap(tenTaiKhoan: String, matKhau: String): KiemTraTaiKhoanResponse {
        return apiService.kiemTraDangNhap(tenTaiKhoan, matKhau)
    }

    suspend fun kiemTraTrungUsername(tenTaiKhoan: String): KiemTraTaiKhoanResponse {
        return apiService.kiemTraTrunUsername(tenTaiKhoan)
    }

    suspend fun getTaiKhoanByTentaikhoan(tentaikhoan: String): TaiKhoan {
        return apiService.getTaiKhoanByTentaikhoan(tentaikhoan)
    }

    suspend fun updateTaiKhoan(taiKhoan: TaiKhoan): taikhoanUpdateResponse {
        return apiService.updateTaiKhoan(taiKhoan)
    }

    suspend fun taoTaiKhoan(taiKhoan: TaiKhoan): taikhoanUpdateResponse {
        return apiService.TaoTaiKhoan(taiKhoan)
    }
}
