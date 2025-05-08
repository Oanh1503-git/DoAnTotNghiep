package com.example.laptopstore.models

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val discount: Int = 0,
    val specs: String
)