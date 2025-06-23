package com.example.lapstore.models

data class ChiTietHoaDon(
    val MaChiTietHoaDon: Int = 0,
    val MaHoaDon: Int,
    val MaSanPham: Int,
    val SoLuong: Int,
    val DonGia: Double,
    val GiamGia: Double,
    val ThanhTien: Double
)