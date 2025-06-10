package com.example.laptopstore.models

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val MaSanPham: Int,
    val TenSanPham: String,
    val HinhAnh: String,
    val Gia: Int,
    val GiamGia: Int = 0,
    val SoLuong: Int = 0
)

@Serializable
data class CartItem(
    val MaGioHang: Int,
    val MaSanPham: Int,
    val SoLuong: Int,
    val product: Product? = null  // Thêm trường product để lưu thông tin sản phẩm khi cần
)