package com.example.laptopstore.views

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.laptopstore.models.KhachHang
import com.example.laptopstore.models.Screens
import com.example.laptopstore.models.TaiKhoan
import com.example.laptopstore.viewmodels.KhachHangViewModels
import com.example.laptopstore.viewmodels.TaiKhoanViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavHostController,
    taiKhoanViewModel: TaiKhoanViewModel,
    khachHangViewModel: KhachHangViewModels
) {
    var userInput by remember { mutableStateOf(RegisterFormState()) }
    var isTermsChecked by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val registerStatus by taiKhoanViewModel.registerStatus.collectAsState()
    val isRegistering by taiKhoanViewModel.isRegistering.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Đăng ký tài khoản", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White
    ) { innerPadding ->

            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        "Tạo tài khoản mới",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0D47A1)
                    )
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    // Thông tin tài khoản
                    Text("Thông tin tài khoản", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                    Spacer(Modifier.height(8.dp))
                    RegisterInputField(
                        label = "Tên đăng nhập",
                        value = userInput.username,
                        onValueChange = { userInput = userInput.copy(username = it) }
                    )
                    RegisterInputField(
                        label = "Mật khẩu",
                        value = userInput.password,
                        onValueChange = { userInput = userInput.copy(password = it) },
                        isPassword = true
                    )
                    RegisterInputField(
                        label = "Nhập lại mật khẩu",
                        value = userInput.confirmPassword,
                        onValueChange = { userInput = userInput.copy(confirmPassword = it) },
                        isPassword = true
                    )
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    // Thông tin cá nhân
                    Text("Thông tin cá nhân", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                    Spacer(Modifier.height(8.dp))
                    RegisterInputField(
                        label = "Họ tên",
                        value = userInput.fullName,
                        onValueChange = { userInput = userInput.copy(fullName = it) }
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Giới tính:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = userInput.gender == "Nam",
                                onClick = { userInput = userInput.copy(gender = "Nam") }
                            )
                            Text("Nam", modifier = Modifier.padding(end = 16.dp))
                            RadioButton(
                                selected = userInput.gender == "Nữ",
                                onClick = { userInput = userInput.copy(gender = "Nữ") }
                            )
                            Text("Nữ")
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    RegisterInputField(
                        label = "Ngày sinh (YYYY-MM-DD)",
                        value = userInput.birthDate,
                        onValueChange = { userInput = userInput.copy(birthDate = it) }
                    )
                    RegisterInputField(
                        label = "Số điện thoại",
                        value = userInput.phone,
                        onValueChange = { userInput = userInput.copy(phone = it) },
                        keyboardType = KeyboardType.Phone
                    )
                    RegisterInputField(
                        label = "Email",
                        value = userInput.email,
                        onValueChange = { userInput = userInput.copy(email = it) },
                        keyboardType = KeyboardType.Email
                    )
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isTermsChecked,
                            onCheckedChange = { isTermsChecked = it }
                        )
                        Text("Tôi đồng ý với các điều khoản sử dụng", fontSize = 14.sp)
                    }
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    Button(
                        onClick = {
                            val validationResult = validateRegisterInput(userInput, isTermsChecked)
                            if (validationResult != null) {
                                scope.launch { snackbarHostState.showSnackbar(validationResult) }
                            } else {
                                scope.launch {
                                    try {
                                        // ✅ Kiểm tra username trùng
                                        val trung = taiKhoanViewModel.kiemTraTrungUsername(userInput.username)
                                        if (trung) {
                                            snackbarHostState.showSnackbar("Tên đăng nhập đã tồn tại!")
                                            return@launch
                                        }
                                        Log.d("đăngky", "ten tài khoản trungg $trung")
                                        // ✅ Tạo mã khách hàng
                                        khachHangViewModel.taoMaKhachHang()
                                        val maKH = withContext(Dispatchers.Default) {
                                            khachHangViewModel.maKhachHangState.first { it != null }
                                        }
                                        Log.d("đăngky", "tạo mã $maKH")


                                        if (maKH != null) {
                                            val successThemKH = khachHangViewModel.themkhachhang(
                                                KhachHang(
                                                    MaKhachHang = maKH,
                                                    HoTen = userInput.fullName,
                                                    GioiTinh = userInput.gender,
                                                    NgaySinh = userInput.birthDate,
                                                    SoDienThoai = userInput.phone,
                                                    Email = userInput.email
                                                )
                                            )

                                            Log.d("đăngky", "tao khách hàng $successThemKH")
                                            val success = taiKhoanViewModel.TaoTaiKhoan(
                                                TaiKhoan(
                                                    TenTaiKhoan = userInput.username,
                                                    MaKhachHang = maKH,
                                                    MatKhau = userInput.password,
                                                    LoaiTaiKhoan = 0,
                                                    TrangThai = 1
                                                )
                                            )
                                            Log.d("đăngky", "tao tai khoan  $success")
                                            if (success) {
                                                snackbarHostState.showSnackbar("Đăng ký thành công!")
                                                navController.navigate(
                                                    Screens.VERIFYEMAILSCREEN.createRoute(userInput.email, userInput.username)
                                                )
                                            } else {
                                                snackbarHostState.showSnackbar("Tạo tài khoản thất bại!")
                                            }
                                        } else {
                                            snackbarHostState.showSnackbar("Tạo khách hàng thất bại!")
                                        }
                                    } catch (e: Exception) {
                                        snackbarHostState.showSnackbar("Lỗi: ${e.message}")
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
                    ) {
                        Text("Đăng ký", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    TextButton(onClick = { navController.navigate(Screens.Login_Screens.route) }) {
                        Text("Đã có tài khoản? Đăng nhập", color = Color.Gray)
                    }
                }


        }
    }
}

@Composable
fun RegisterInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF0D47A1),
            unfocusedBorderColor = Color.Gray,
            focusedLabelColor = Color(0xFF0D47A1)
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}

@Composable
fun GenderRadioButton(selectedGender: String, onGenderSelected: (String) -> Unit) {
    Row {
        RadioButton(
            selected = selectedGender == "Nam",
            onClick = { onGenderSelected("Nam") }
        )
        Text("Nam")
        Spacer(Modifier.width(8.dp))
        RadioButton(
            selected = selectedGender == "Nữ",
            onClick = { onGenderSelected("Nữ") }
        )
        Text("Nữ")
    }
}

data class RegisterFormState(
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val fullName: String = "",
    val gender: String = "",
    val birthDate: String = "",
    val phone: String = "",
    val email: String = ""
)

fun validateRegisterInput(input: RegisterFormState, isTermsChecked: Boolean): String? {
    return when {
        input.username.isBlank() -> "Tên đăng nhập không được để trống"
        input.password.isBlank() -> "Mật khẩu không được để trống"
        input.confirmPassword != input.password -> "Mật khẩu xác nhận không khớp"
        input.fullName.isBlank() -> "Họ tên không được để trống"
        input.gender !in listOf("Nam", "Nữ") -> "Bạn phải chọn giới tính"
        !input.birthDate.matches(Regex("^\\d{4}-\\d{2}-\\d{2}$")) -> "Ngày sinh sai định dạng"
        !input.phone.matches(Regex("^0\\d{9}$")) -> "Số điện thoại không hợp lệ"
        !input.email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$")) -> "Email không đúng định dạng"
        input.username.length !in 8..15 -> "Tên đăng nhập phải từ 8 đến 15 ký tự"
        input.password.length < 8 -> "Mật khẩu phải ít nhất 8 ký tự"
        !isTermsChecked -> "Bạn cần đồng ý với điều khoản sử dụng"
        else -> null
    }
}