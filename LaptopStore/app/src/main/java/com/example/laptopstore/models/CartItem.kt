package com.example.laptopstore.models

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val price: Int,
    val discount: Int
)

@Serializable
data class CartItem(
    val product: Product,
    val quantity: Int
)