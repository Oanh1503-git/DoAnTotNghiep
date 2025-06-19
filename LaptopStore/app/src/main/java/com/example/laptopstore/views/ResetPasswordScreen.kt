import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.laptopstore.models.Screens
import com.example.laptopstore.models.TaiKhoan
import com.example.laptopstore.viewmodels.TaiKhoanViewModel
import com.example.laptopstore.viewmodels.TaiKhoanViewsModelsFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    navController: NavHostController,
    username: String
) {
    val context = LocalContext.current
    val taiKhoanViewModel: TaiKhoanViewModel = viewModel(
        factory = TaiKhoanViewsModelsFactory(context)
    )
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }

    val resetStatus by taiKhoanViewModel.resetStatus.collectAsState()

    var showSuccessDialog by remember { mutableStateOf(false) }

    // Khi đổi mật khẩu thành công, mở dialog
    LaunchedEffect(resetStatus) {
        if (resetStatus == "Đổi mật khẩu thành công") {
            showSuccessDialog = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Đặt lại mật khẩu") })
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
                Text("Nhập mật khẩu mới cho tài khoản: $username", fontSize = 16.sp)

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Mật khẩu mới") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Xác nhận mật khẩu") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Button(onClick = {
                    if (newPassword.isBlank() || confirmPassword.isBlank()) {
                        errorText = "Vui lòng nhập đầy đủ thông tin"
                    } else if (newPassword != confirmPassword) {
                        errorText = "Mật khẩu xác nhận không khớp"
                    } else {
                        errorText = ""
                        taiKhoanViewModel.resetPassword(username, newPassword)
                        showSuccessDialog = true
                    }
                }) {
                    Text("Đặt lại mật khẩu")
                }

                if (errorText.isNotBlank()) {
                    Spacer(Modifier.height(12.dp))
                    Text(errorText, color = Color.Red)
                }

                if (resetStatus != null && resetStatus != "Đổi mật khẩu thành công") {
                    Spacer(Modifier.height(12.dp))
                    Text(resetStatus ?: "", color = Color.Gray)
                }
            }

            // ✅ Dialog khi thành công
            if (showSuccessDialog) {
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text("Thành công") },
                    text = { Text("Mật khẩu của bạn đã được thay đổi.") },
                    confirmButton = {
                        TextButton(onClick = {
                            showSuccessDialog = false
                            taiKhoanViewModel.clearStatus()
                            navController.navigate(Screens.Login_Screens.route) {
                                popUpTo(Screens.Login_Screens.route) { inclusive = true }
                            }
                        }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}
