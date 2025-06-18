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
    val product: SanPham? = null,  // Thêm trường product để lưu thông tin sản phẩm khi cần
    val TenSanPham: String? = null,
    val Gia: Int? = null,
    val HinhAnh: String? = null,
    val GiamGia: Int? = null
) {
    // Helper function để lấy thông tin sản phẩm an toàn
    fun getProductName(): String {
        return product?.TenSanPham ?: TenSanPham ?: "Sản phẩm không xác định"
    }

    fun getProductPrice(): Int {
        return product?.Gia ?: Gia ?: 0
    }

    fun getProductImage(): String {
        return product?.HinhAnh ?: HinhAnh ?: ""
    }

    fun getProductDiscount(): Int {
        return product?.GiamGia ?: GiamGia ?: 0
    }
}