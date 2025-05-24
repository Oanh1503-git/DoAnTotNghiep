package com.example.laptopstore.models

import com.google.gson.annotations.SerializedName

data class SanPham(
    @SerializedName("MaSanPham") var MaSanPham: Int,
    @SerializedName("TenSanPham") var TenSanPham: String,
    @SerializedName("MaLoaiSanPham") var MaLoaiSanPham: Int,
    @SerializedName("mathuonghieu") var mathuonghieu: Int, // Thêm trường thương hiệu
    @SerializedName("CPU") var CPU: String,
    @SerializedName("RAM") var RAM: String,
    @SerializedName("CardManHinh") var CardManHinh: String,
    @SerializedName("SSD") var SSD: String,
    @SerializedName("ManHinh") var ManHinh: String,
    @SerializedName("MaMauSac") var MaMauSac: Int,
    @SerializedName("Gia") var Gia: Int,
    @SerializedName("SoLuong") var SoLuong: Int,
    @SerializedName("MoTa") var MoTa: String,
    @SerializedName("HinhAnh") var HinhAnh: String,
    @SerializedName("TrangThai") var TrangThai: Int,
)