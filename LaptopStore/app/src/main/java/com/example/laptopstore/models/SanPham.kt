package com.example.laptopstore.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SanPham(
    @SerialName("MaSanPham") val MaSanPham: Int,
    @SerialName("TenSanPham") val TenSanPham: String,
    @SerialName("MaLoaiSanPham") val MaLoaiSanPham: Int,
    @SerialName("mathuonghieu") val mathuonghieu: Int,
    @SerialName("CPU") val CPU: String,
    @SerialName("RAM") val RAM: String,
    @SerialName("CardManHinh") val CardManHinh: String,
    @SerialName("SSD") val SSD: String,
    @SerialName("ManHinh") val ManHinh: String,
    @SerialName("MaMauSac") val MaMauSac: Int,
    @SerialName("Gia") val Gia: Int,
    @SerialName("SoLuong") val SoLuong: Int,
    @SerialName("MoTa") val MoTa: String,
    @SerialName("HinhAnh") val HinhAnh: String,
    @SerialName("TrangThai") val TrangThai: Int
)