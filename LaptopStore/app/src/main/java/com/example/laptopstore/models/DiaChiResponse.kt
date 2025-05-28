package com.example.laptopstore.models

data class DiaChiResponse(
    val success: Boolean,
    val message: String,
    val diachi: List<DiaChi>? = null
)

data class DiaChiUpdateResponse(
    val success: Boolean,
    val message: String
) 