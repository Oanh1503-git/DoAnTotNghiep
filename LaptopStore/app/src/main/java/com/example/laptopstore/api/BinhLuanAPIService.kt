package com.example.laptopstore.api

import com.example.laptopstore.models.BinhLuanDanhGia
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class BinhLuanResponse(
    val binhluan: List<BinhLuanDanhGia>
)
data class BinhLuanDanhGiaResponse(
    val success: Boolean,
    val data: List<BinhLuanDanhGia>?
)
interface BinhLuanAPIService {

    @POST("BinhLuanDanhGia/create.php")
    suspend fun createBinhLuan(
        @Body binhLuan: BinhLuanDanhGia
    ): CreateResponse

    @GET("BinhLuanDanhGia/get_danhgia_theo_masanpham.php")
    suspend fun getDanhGiaByMaSanPham(
        @Query("MaSanPham") maSanPham: Int
    ): BinhLuanDanhGiaResponse
}