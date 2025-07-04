package com.example.laptopstore.models

import kotlinx.serialization.Serializable

@Serializable
data class BinhLuanDanhGia(
    val MaBinhLuan: Int,
    val MaKhachHang: String,
    val TenKhachHang: String?,
    val MaSanPham: Int,
    val MaHoaDon: Int,
    val SoSao: Int,
    val NoiDung: String?,
    val NgayDanhGia: String,
    val TrangThai: Int?
)