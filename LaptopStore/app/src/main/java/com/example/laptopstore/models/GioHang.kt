package com.example.laptopstore.models

import com.google.gson.annotations.SerializedName

data class GioHang(
    @SerializedName("MaGioHang") val MaGioHang: Int,
    @SerializedName("MaKhachHang") var MaKhachHang: String,
    @SerializedName("MaSanPham") val MaSanPham: Int,
    @SerializedName("SoLuong") val SoLuong: Int,
    @SerializedName("TrangThai") val TrangThai: Int,
    @SerializedName("TenSanPham") val TenSanPham: String? = null,
    @SerializedName("Gia") val Gia: Int? = null,
    @SerializedName("HinhAnh") val HinhAnh: String? = null
)