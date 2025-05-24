package com.example.laptopstore.api

import com.example.laptopstore.models.HinhAnh
import retrofit2.http.GET
import retrofit2.http.Query

data class HinhAnhResponse(
    val hinhanh: List<HinhAnh>
)

interface HinhAnhAPIService {
    @GET("HinhAnh/readhinhanhbymasanpham.php")
    suspend fun getHinhAnhBySanPham(
        @Query("MaSanPham") MaSanPham: Int
    ): HinhAnhResponse

    @GET("HinhAnh/read.php")
    suspend fun getAllHinhAnh(): HinhAnhResponse
}