package com.example.laptopstore.api


import com.example.lapstore.models.KhachHang
import com.example.lapstore.models.SanPham
import com.example.lapstore.models.TaiKhoan
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

data class KhachHangResponse(
    val khachhang: List<KhachHang>
)

data class KhachHangUpdateResponse(
    val success: Boolean,
    val message: String
)

interface KhachHangAPIService {
    // Lấy tất cả khách hàng
    @GET("KhachHang/read.php")
    fun getAllKhachHang(): Call<KhachHangResponse>

    @PUT("KhachHang/update.php")
    suspend fun updateKhachHang(
        @Body khachHang: KhachHang
    ): KhachHangUpdateResponse

    @GET("KhachHang/show.php")
    suspend fun getKhachHangById(
        @Query("id") id: String
    ): KhachHang

    @POST("KhachHang/create.php")
    suspend fun ThemKhachHang(
        @Body khachhang: KhachHang,
    ): KhachHangUpdateResponse
}
