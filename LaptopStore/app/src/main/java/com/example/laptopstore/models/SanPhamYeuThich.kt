package com.example.laptopstore.models

import kotlinx.serialization.Serializable

@Serializable
data class SanPhamYeuThich(
    val MaSanPham: Int,
    val MaKhachHang: String,
)
@Serializable
data class HienSanPhamYeuThich(
    val id: Int,
    val MaSanPham: Int,
    val MaKhachHang: String,
    val TenSanPham: String?,
    val Gia: Int?,
    val HinhAnh: String?
)