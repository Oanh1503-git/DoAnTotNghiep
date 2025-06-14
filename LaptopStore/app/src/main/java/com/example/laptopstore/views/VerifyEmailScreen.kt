package com.example.laptopstore.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyEmailScreen(
    navController: NavHostController,
    email: String,
    onVerify: (String) -> Unit,
    onResend: () -> Unit
) {
    var otpCode by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Xác thực email") }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Đã gửi mã xác thực đến:",
                    fontSize = 16.sp
                )
                Text(
                    email,
                    fontSize = 18.sp,
                    color = Color.Blue,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = otpCode,
                    onValueChange = { otpCode = it.take(6) },
                    label = { Text("Nhập mã xác thực") },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                )
                if (errorText.isNotBlank()) {
                    Text(errorText, color = Color.Red, fontSize = 14.sp)
                }
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = {
                        if (otpCode.length != 6) {
                            errorText = "Mã xác thực phải gồm 6 ký tự"
                        } else {
                            errorText = ""
                            onVerify(otpCode)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Text("Xác thực")
                }
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = onResend) {
                    Text("Gửi lại mã")
                }
            }
        }
    }
}