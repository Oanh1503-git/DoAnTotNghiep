package com.example.laptopstore.models

data class Otp(
    val id: Int? = null,
    val tenTaiKhoan: String,
    val otpCode: String,
    val createdAt: String? = null,   // Định dạng: yyyy-MM-dd HH:mm:ss
    val expiresAt: String,
    val isUsed: Int = 0              // 0 = chưa dùng, 1 = đã dùng
)
