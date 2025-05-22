import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.laptopstore.models.Screens
import com.example.laptopstore.viewmodels.TaiKhoanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login_Screens(navController: NavHostController ) {
//    taiKhoanViewModel: TaiKhoanViewModel = viewModel())
//    val loginResult by taiKhoanViewModel.loginResult
//    val isLoggedIn by taiKhoanViewModel.isLoggedIn
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }



    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Trang Đăng Nhập") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Tên tài khoản") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mật khẩu") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Button(
                onClick = {
                    if (username.isNotBlank() && password.isNotBlank()) {
//                        taiKhoanViewModel.kiemTraDangNhap(username, password)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Đăng nhập")
            }


            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Bạn chưa có tài khoản? Đăng ký tại đây",
                color = Color.Blue,
                modifier = Modifier.clickable {
                    navController.navigate(Screens.Register_Screen.route)
                }
            )
        }
    }
}
