package com.example.laptopstore.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
// Gửi OTP
data class SendOtpRequest(
    val email: String,
    val username: String
)

data class SendOtpResponse(
    val success: Boolean,
    val message: String
)

// Xác minh OTP
data class VerifyOtpRequest(
    val username: String,
    val otp: String
)

data class VerifyOtpResponse(
    val success: Boolean,
    val message: String
)
interface OtpAPIService {

    @POST("otp/send_otp.php")
    suspend fun sendOtp(
        @Body request: SendOtpRequest
    ): Response<SendOtpResponse>

    @POST("otp/verify_otp.php")
    suspend fun verifyOtp(
        @Body request: VerifyOtpRequest
    ): Response<VerifyOtpResponse>
}