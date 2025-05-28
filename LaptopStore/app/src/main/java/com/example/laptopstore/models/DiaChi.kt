package com.example.laptopstore.models

data class DiaChi(
    val MaDiaChi: Int,
    val MaKhachHang: Int,
    val TenNguoiNhan: String,
    val SoDienThoai: String,
    val DiaChi: String,
    val MacDinh: Int // 1 = mặc định, 0 = không mặc định
)

