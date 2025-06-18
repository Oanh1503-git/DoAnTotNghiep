package com.example.laptopstore.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.laptopstore.models.Screens
import com.example.laptopstore.viewmodels.OTPViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyEmailScreen(
    navController: NavHostController,
    email: String,
    username: String,
    otpViewModel: OTPViewModel = viewModel()
) {
    var otpCode by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }

    val otpStatus by otpViewModel.otpStatus.collectAsState()
    val verified by otpViewModel.otpVerified.collectAsState()

    // Gửi mã OTP tự động khi vào màn hình
    LaunchedEffect(Unit) {
        otpViewModel.sendOtp(email, username)
    }

    // Khi xác minh thành công → chuyển về trang đăng nhập
    LaunchedEffect(verified) {
        if (verified == true) {
            navController.navigate(Screens.Login_Screens.route) {

                popUpTo(Screens.VERIFYEMAILSCREEN.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Xác thực email") },
                actions = {
                    TextButton(onClick = {
                        navController.navigate(Screens.Login_Screens.route) {
                            popUpTo(Screens.VERIFYEMAILSCREEN.route) { inclusive = true }
                        }
                    }) {
                        Text("Đăng nhập", color = Color.White)
                    }
                }
            )
        },

    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Đã gửi mã xác thực đến:", fontSize = 16.sp)
                Text(email, fontSize = 18.sp, color = Color.Blue, modifier = Modifier.padding(bottom = 16.dp))

                OutlinedTextField(
                    value = otpCode,
                    onValueChange = { otpCode = it.take(6) },
                    label = { Text("Nhập mã xác thực") },
                    modifier = Modifier.fillMaxWidth(0.7f)
                )

                if (errorText.isNotBlank()) {
                    Text(errorText, color = Color.Red, fontSize = 14.sp)
                }

                if (otpStatus != null) {
                    Text(otpStatus ?: "", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))
                }

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (otpCode.length != 6) {
                            errorText = "Mã xác thực phải gồm 6 ký tự"
                        } else {
                            errorText = ""
                            otpViewModel.verifyOtp(username, otpCode)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Text("Xác thực")
                }

                Spacer(Modifier.height(8.dp))

                TextButton(onClick = {
                    otpViewModel.sendOtp(email, username)
                    otpViewModel.resetStatus()
                }) {
                    Text("Gửi lại mã")
                }
            }
        }
    }
}
