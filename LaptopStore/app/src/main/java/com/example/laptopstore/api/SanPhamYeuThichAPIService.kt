package com.example.laptopstore.api

import com.example.laptopstore.models.SanPhamYeuThich
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class SanPhamYeuThichResponse(
    val sanphamyeuthich: List<SanPhamYeuThich>
)

data class CheckFavoriteResponse(
    val isFavorite: Boolean
)


interface SanPhamYeuThichAPIService {
    @GET("SanPhamYeuThich/check.php")
    suspend fun checkFavorite(
        @Query("MaSanPham") MaSanPham: Int,
        @Query("MaKhachHang") MaKhachHang: Int
    ): CheckFavoriteResponse

    @POST("SanPhamYeuThich/create.php")
    suspend fun addFavorite(
        @Body yeuThich: SanPhamYeuThich
    ): CreateResponse

    @POST("SanPhamYeuThich/delete.php")
    suspend fun removeFavorite(
        @Body yeuThich: SanPhamYeuThich
    ): CreateResponse

    @GET("SanPhamYeuThich/readByKhachHang.php")
    suspend fun getFavoritesByKhachHang(
        @Query("MaKhachHang") MaKhachHang: Int
    ): SanPhamYeuThichResponse
}