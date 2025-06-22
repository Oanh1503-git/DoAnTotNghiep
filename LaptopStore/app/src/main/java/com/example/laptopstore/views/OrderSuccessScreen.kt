package com.example.laptopstore.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.laptopstore.models.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSuccessScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Đặt hàng thành công") })
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
                    text = "🎉 Cảm ơn bạn đã đặt hàng!",
                    fontSize = 20.sp,
                    color = Color.Green
                )
                Spacer(modifier = Modifier.height(20.dp))

                Button(onClick = {
                    // Quay về trang chính
                    navController.navigate(Screens.Login_Screens.route) {
                        popUpTo("home") { inclusive = true }
                    }
                }) {
                    Text("Về trang chủ")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = {
                    navController.navigate(Screens.ORDERSTATUSSCREEN.route)
                    navController.navigate("donhang") // bạn đổi theo route của bạn
                }) {
                    Text("Xem đơn hàng")
                }
            }
        }
    }
}
