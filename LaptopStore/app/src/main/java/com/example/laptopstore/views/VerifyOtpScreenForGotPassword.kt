package com.example.laptopstore.views

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.laptopstore.models.Screens
import com.example.laptopstore.viewmodels.OTPViewModel
import com.example.laptopstore.viewmodels.TaiKhoanViewModel
import com.example.laptopstore.viewmodels.TaiKhoanViewsModelsFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyEmailScreenForGotPassWord(
    navController: NavHostController,
    otpViewModel: OTPViewModel = viewModel()
) {
    val context = LocalContext.current
    val taiKhoanViewModel: TaiKhoanViewModel = viewModel(
        factory = TaiKhoanViewsModelsFactory(context)
    )
    var email by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }

    val otpStatus by otpViewModel.otpStatus.collectAsState()
    val verified by otpViewModel.otpVerified.collectAsState()
    val username = otpViewModel.usernameState.value
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Xác thực tài khoản") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text("Nhập email đã đăng ký để nhận mã xác thực", fontSize = 16.sp)

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                Button(onClick = {
                    if (email.isBlank()) {
                        errorText = "Vui lòng nhập email"
                    } else {
                        errorText = ""
                        taiKhoanViewModel.checkEmail(email) { resultUsername ->
                            if (resultUsername == null) {
                                errorText = "Email chưa được liên kết với tài khoản"
                            } else {
                                otpViewModel.setCachedUsername(resultUsername)
                                otpViewModel.sendOtp(email, resultUsername)
                            }
                        }
                    }
                }) {
                    Text("Gửi mã")
                }

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = otpCode,
                    onValueChange = { otpCode = it.take(6) },
                    label = { Text("Mã xác thực") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (otpCode.length != 6) {
                            errorText = "Mã OTP phải gồm 6 chữ số"
                        } else if (!username.isNullOrEmpty()) {
                            otpViewModel.verifyOtp(username!!, otpCode)
                        } else {
                            errorText = "Bạn cần gửi mã trước"
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Xác minh")
                }

                if (errorText.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(errorText, color = Color.Red)
                }

                if (otpStatus != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(otpStatus ?: "", color = Color.Gray)
                }

                Spacer(Modifier.height(16.dp))

                // ✅ Nút chuyển sang màn hình đổi mật khẩu chỉ hiển thị khi xác minh thành công
                if (verified == true && !username.isNullOrEmpty()) {
                    Button(
                        onClick = {
                            if (!username.isNullOrEmpty()) {
                                navController.navigate(Screens.RESETPASSWORDSCREEN.createRoute(username))
                            }
                            otpViewModel.resetStatus()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Tiếp tục đổi mật khẩu")
                    }
                }
            }
        }
    }
}
