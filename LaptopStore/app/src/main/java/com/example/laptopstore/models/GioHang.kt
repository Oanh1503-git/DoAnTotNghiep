package com.example.laptopstore.models

import com.google.gson.annotations.SerializedName

data class GioHang(
    @SerializedName("MaGioHang") val MaGioHang: Int,
    @SerializedName("MaKhachHang") val MaKhachHang: Int,
    @SerializedName("MaSanPham") val MaSanPham: Int,
    @SerializedName("SoLuong") val SoLuong: Int,
    @SerializedName("TrangThai") val TrangThai: Int
)