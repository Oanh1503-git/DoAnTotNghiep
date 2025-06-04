package com.example.laptopstore.api

import com.example.laptopstore.Repository.KhachHangRepository
import com.example.laptopstore.models.KhachHang
import retrofit2.Call
import retrofit2.http.*

data class BaseResponse(
    val success: Boolean,
    val message: String? = null
)

data class KhachHangResponse(
    val khachhang: List<KhachHang>
)

data class SoLuongKhachHangResponse(
    val success: Boolean,
    val so_luong: Int
)

data class KhachHangResponse_2(
    val success: Boolean,
    val message: String,
    val data: KhachHang?
)

data class MaKhachHangResponse(
    val success: Boolean,
    val ma_khach_hang: String
)

interface KhachHangAPIService {

    // ✅ 1. Lấy tất cả khách hàng
    @GET("KhachHang/read.php")
    fun getAllKhachHang(): Call<KhachHangResponse>

    // ✅ 2. Lấy 1 khách hàng theo mã
    @GET("KhachHang/get_khachhang.php")
    suspend fun getKhachHangById(
        @Query("maKhachHang") maKhachHang: String
    ): KhachHangResponse_2


    // ✅ 3. Thêm khách hàng mới
    @POST("KhachHang/create.php")
    suspend fun ThemKhachHang(
        @Body khachHang: KhachHang
    ): BaseResponse

    // ✅ 4. Cập nhật khách hàng
    @PUT("KhachHang/update.php")
    suspend fun updateKhachHang(
        @Body khachHang: KhachHang
    ): BaseResponse

    // ✅ 5. Xoá khách hàng
    @HTTP(method = "DELETE", path = "KhachHang/delete.php", hasBody = true)
    suspend fun deleteKhachHang(
        @Body khachHang: KhachHang
    ): BaseResponse

    // ✅ 6. Đếm số lượng khách hàng
    @GET("KhachHang/count_khachhang.php")
    suspend fun getSoLuongKhachHang(): SoLuongKhachHangResponse

    // ✅ 7. Tạo mã khách hàng mới
    @GET("KhachHang/taoMaKhachHang.php")
    suspend fun taoMaKhachHang(): MaKhachHangResponse
}
