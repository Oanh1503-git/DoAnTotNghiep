package com.example.laptopstore.api

import com.example.laptopstore.models.SanPham
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

data class SanPhamResponse(
    val sanpham: List<SanPham>
)
data class SeachSanphamResponse(
    val status: String,
    val data: List<SanPham>
)

data class SoLuongCheckResponse(
    val status: String,
    val message: String,
    val soLuongGioHang: Int,
    val soLuongKho: Int
)
data class SoLuongTonKhoResponse(
    val success: Boolean,
    val MaSanPham: Int? = null,
    val SoLuongTonKho: Int,
    val message: String? = null
)

interface SanPhamAPIService{
    @GET("SanPham/read.php")
    suspend fun getAllSanPham(): SanPhamResponse

    @GET("SanPham/readByLoaiSanPham.php")
    suspend fun getSanPhamTheoLoai(
        @Query("MaLoaiSanPham") MaLoaiSanPham: Int
    ): SanPhamResponse

    @GET("SanPham/getproductCardCuaKhachHang.php")
    suspend fun getSanPhamByGioHang(
        @Query("MaKhachHang") MaKhachHang: String
    ): SanPhamResponse

    @GET("SanPham/show.php")
    suspend fun getSanPhamById(
        @Query("MaSanPham") MaSanPham: String
    ): SanPham


    @GET("SanPham/searchTenSanPham.php")
    suspend fun searchTenSanPham(@Query("query") query: String): SeachSanphamResponse

    @GET("SanPham/searchSanPham.php")
    suspend fun searchSanPham(@Query("search") search: String): SanPhamResponse

    @GET("SanPham/laysanphamtheohoadon.php")
    suspend fun getSanPhamTheoHoaDon(
        @Query("MaHoaDonBan") MaHoaDonBan: Int
    ): SanPhamResponse

    @PUT("SanPham/update.php")
    suspend fun updateSanPham(
        @Body sanpham: SanPham
    ): BaseResponse
    @GET("kiem_tra_soluong.php")
    suspend fun kiemTraSoLuong(
        @Query("MaKhachHang") maKhachHang: String,
        @Query("MaSanPham") maSanPham: Int
    ): Response<SoLuongCheckResponse>
    @GET("SanPham/kiemtrasoluongkho.php")
    suspend fun kiemTraSoLuongTonKho(
        @Query("MaSanPham") maSanPham: Int
    ): SoLuongTonKhoResponse
}