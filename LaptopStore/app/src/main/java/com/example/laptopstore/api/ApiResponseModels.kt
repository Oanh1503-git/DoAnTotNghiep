package com.example.laptopstore.api

data class CreateResponse(
    val message: String
)

data class UpdateResponse(
    val success: Boolean,
    val message: String
)

data class ApiResponse(
    val message: String
)

data class SuccessResponse(
    val success: Boolean,
    val message: String
)