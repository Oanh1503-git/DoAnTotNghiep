package com.example.laptopstore.models

import kotlinx.serialization.Serializable

@Serializable
data class SanPhamYeuThich(
    val MaSanPham: Int,
    val MaKhachHang: Int
)