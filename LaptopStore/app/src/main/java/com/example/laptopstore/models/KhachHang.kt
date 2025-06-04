package com.example.laptopstore.models

import androidx.room.Entity
import androidx.room.PrimaryKey


data class KhachHang(
    var MaKhachHang: String,
    var HoTen: String,
    var GioiTinh: String,
    var NgaySinh: String,
    var Email: String,
    var SoDienThoai: String,
)
