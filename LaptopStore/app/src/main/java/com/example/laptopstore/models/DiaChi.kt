package com.example.laptopstore.models


data class DiaChi(
    val MaDiaChi: Int = 0,
    val MaKhachHang: String,
    val ThongTinDiaChi: String,
    val TenNguoiNhan: String,
    val SoDienThoai: String,
    val MacDinh: Int,
    val provinceId: Int?,
    val districtId: Int?,
    val wardId: Int?
)
{
    companion object {
        val EMPTY = DiaChi(0,"","","","",0,0,0,0)
    }
}
