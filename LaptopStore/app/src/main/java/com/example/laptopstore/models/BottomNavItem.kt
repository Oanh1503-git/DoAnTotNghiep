package com.example.app_e_commerce.model

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)
data class Category(
    val name: String,
    val imageRes: Int
)
