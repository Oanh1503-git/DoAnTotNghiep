package com.example.laptopstore.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.laptopstore.RetrofitClient.LaptopStoreRetrofitClient

import androidx.lifecycle.viewModelScope
import com.example.laptopstore.api.SendOtpRequest
import com.example.laptopstore.api.VerifyOtpRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OTPViewModel : ViewModel() {

    var cachedUsername: String? = null
        private set
    private val _otpStatus = MutableStateFlow<String?>(null)
    val otpStatus: StateFlow<String?> = _otpStatus


    private val _otpVerified = MutableStateFlow<Boolean?>(null)
    val otpVerified: StateFlow<Boolean?> = _otpVerified


    private val _usernameState = mutableStateOf<String?>(null)
    val usernameState: State<String?> = _usernameState

    fun sendOtp(email: String, username: String) {
        viewModelScope.launch {
            try {
                val response = LaptopStoreRetrofitClient.otpAPIService.sendOtp(
                    SendOtpRequest(email = email, username = username)
                )
                if (response.isSuccessful && response.body()?.success == true) {
                    _otpStatus.value = "Đã gửi OTP tới email"
                } else {
                    _otpStatus.value = response.body()?.message ?: "Gửi OTP thất bại"
                }
            } catch (e: Exception) {
                _otpStatus.value = "Lỗi kết nối: ${e.localizedMessage}"
            }
        }
    }


    fun verifyOtp(username: String, otp: String) {
        viewModelScope.launch {
            try {
                val response = LaptopStoreRetrofitClient.otpAPIService.verifyOtp(
                    VerifyOtpRequest(username = username, otp = otp)
                )
                if (response.isSuccessful && response.body()?.success == true) {
                    _otpVerified.value = true
                    _otpStatus.value = "Xác thực thành công"
                } else {
                    _otpVerified.value = false
                    _otpStatus.value = response.body()?.message ?: "Xác thực thất bại"
                }
            } catch (e: Exception) {
                _otpVerified.value = false
                _otpStatus.value = "Lỗi kết nối: ${e.localizedMessage}"
            }
        }
    }
    fun resetStatus() {
        _otpStatus.value = null
        _otpVerified.value = null
    }
    fun setCachedUsername(username: String) {
        cachedUsername = username
        _usernameState.value = username
    }
}