package com.example.laptopstore.api

import com.example.laptopstore.models.DiaChi
import com.example.laptopstore.models.DiaChiResponse
import com.example.laptopstore.models.DiaChiUpdateResponse
import retrofit2.http.*

data class DeleteDiaChiRequest(
    val MaDiaChi: Int
)

interface DiaChiAPIService {
    @GET("DiaChi/getByMaKhachHang.php")
    suspend fun getDiaChiByMaKhachHang(
        @Query("MaKhachHang") maKhachHang: Int
    ): DiaChiResponse

    @GET("DiaChi/getByMaDiaChi.php")
    suspend fun getDiaChiByMaDiaChi(
        @Query("MaDiaChi") maDiaChi: Int
    ): DiaChiResponse

    @GET("DiaChi/getMacDinh.php")
    suspend fun getDiaChiMacDinh(
        @Query("MaKhachHang") MaKhachHang: Int,
        @Query("MacDinh") MacDinh: Int
    ): DiaChiResponse

    @POST("DiaChi/create.php")
    suspend fun themDiaChi(
        @Body diaChi: DiaChi
    ): DiaChiUpdateResponse

    @PUT("DiaChi/setDefault.php")
    suspend fun setDefaultAddress(
        @Query("MaDiaChi") maDiaChi: Int
    ): DiaChiUpdateResponse

    @HTTP(method = "DELETE", path = "DiaChi/delete.php", hasBody = true)
    suspend fun deleteDiaChi(
        @Body request: DeleteDiaChiRequest
    ): DiaChiUpdateResponse
}