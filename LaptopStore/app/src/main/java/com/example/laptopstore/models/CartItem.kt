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
    val product: SanPham? = null,
    val TenSanPham: String? = null,
    val Gia: Double? = null,
    val HinhAnh: String? = null,
    val GiamGia: Double? = null
) {
    // Helper function để lấy thông tin sản phẩm an toàn
    fun getProductName(): String {
        return product?.TenSanPham ?: TenSanPham ?: "Sản phẩm không xác định"
    }

    fun getProductPrice(): Double {
        return (product?.Gia ?.toDouble()?: Gia ?.toDouble()?: 0.0)
    }

    fun getProductImage(): String {
        return product?.HinhAnh ?: HinhAnh ?: ""
    }

    fun getProductDiscount(): Double {
        return (product?.GiamGia?.toDouble() ?: GiamGia?.toDouble() ?: 0.0)
    }

}