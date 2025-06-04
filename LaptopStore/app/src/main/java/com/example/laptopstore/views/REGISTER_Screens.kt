package com.example.laptopstore.views

import kotlinx.coroutines.flow.collect
import android.icu.text.CaseMap.Title
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape 
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.laptopstore.RetrofitClient.LaptopStoreRetrofitClient
import com.example.laptopstore.models.KhachHang
import com.example.laptopstore.models.Screens
import com.example.laptopstore.models.TaiKhoan
import com.example.laptopstore.viewmodels.KhachHangViewModels
import com.example.laptopstore.viewmodels.TaiKhoanViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register_Screen(
    navController: NavHostController,
    taiKhoanViewModel: TaiKhoanViewModel,
    khachHangViewModel: KhachHangViewModels
) {
    var issuccess by remember { mutableStateOf(false) }
    var tentaikhoan by remember { mutableStateOf("") }
    var matkhau by remember { mutableStateOf("") }
    var comfirmatkhau by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }
    val dialogMessage = remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var hoTen by remember { mutableStateOf("") }
    var gioiTinh by remember { mutableStateOf("") }
    var ngaySinh by remember { mutableStateOf("") }
    var soDienThoai by remember { mutableStateOf("") }
    val themKhachHangResult by taiKhoanViewModel.themKhachHangResult.collectAsState()
    val checkResult by taiKhoanViewModel.checkUsernameResult.collectAsState()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Đăng ký tài khoản") }
            )
        },
        snackbarHost = { androidx.compose.material3.SnackbarHost(hostState = snackbarHostState) }

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Tạo tài khoản",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 20.dp),
                    color = Color.Blue
                )

                Spacer(modifier = Modifier.height(15.dp))

                OutlinedTextField(
                    value = tentaikhoan,
                    onValueChange = { tentaikhoan = it },
                    label = { Text("Tên đăng nhập") },
                    shape = RoundedCornerShape(17.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Red,
                        unfocusedBorderColor = Color.Red,
                        focusedLabelColor = Color.Red
                    )
                )
                Spacer(modifier = Modifier.height(15.dp))

                OutlinedTextField(
                    value = matkhau,
                    onValueChange = { matkhau = it },
                    label = { Text("Nhập mật khẩu") },
                    shape = RoundedCornerShape(17.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Red,
                        unfocusedBorderColor = Color.Red,
                        focusedLabelColor = Color.Red
                    )
                )
                Spacer(modifier = Modifier.height(15.dp))

                OutlinedTextField(
                    value = comfirmatkhau,
                    onValueChange = { comfirmatkhau = it },
                    label = { Text("Nhập lại mật khẩu") },
                    shape = RoundedCornerShape(17.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Red,
                        unfocusedBorderColor = Color.Red,
                        focusedLabelColor = Color.Red
                    )
                )
                Spacer(modifier = Modifier.padding(15.dp))
                Text("Nhập thông tin cá nhân")
                Spacer(modifier = Modifier.height(15.dp))

                OutlinedTextField(
                    value = hoTen,
                    onValueChange = { hoTen = it },
                    label = { Text("Họ tên") },
                    shape = RoundedCornerShape(17.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Red,
                        unfocusedBorderColor = Color.Red,
                        focusedLabelColor = Color.Red
                    )
                )
                Spacer(modifier = Modifier.height(15.dp))

                OutlinedTextField(
                    value = gioiTinh,
                    onValueChange = { gioiTinh = it },
                    label = { Text("Giới tính (Nam/Nữ)") },
                    shape = RoundedCornerShape(17.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Red,
                        unfocusedBorderColor = Color.Red,
                        focusedLabelColor = Color.Red
                    )
                )
                Spacer(modifier = Modifier.height(15.dp))

                OutlinedTextField(
                    value = ngaySinh,
                    onValueChange = { ngaySinh = it },
                    label = { Text("Ngày sinh (YYYY-MM-DD)") },
                    shape = RoundedCornerShape(17.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Red,
                        unfocusedBorderColor = Color.Red,
                        focusedLabelColor = Color.Red
                    )
                )
                Spacer(modifier = Modifier.height(15.dp))

                OutlinedTextField(
                    value = soDienThoai,
                    onValueChange = { soDienThoai = it },
                    label = { Text("Số điện thoại") },
                    shape = RoundedCornerShape(17.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Red,
                        unfocusedBorderColor = Color.Red,
                        focusedLabelColor = Color.Red
                    )
                )
                Spacer(modifier = Modifier.height(15.dp))

                Spacer(modifier = Modifier.height(15.dp))


                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {
                        navController.navigate(Screens.Login_Screens.route)
                    }) {
                        Text("Đăng Nhập")
                    }

                    Spacer(Modifier.width(15.dp))

                    Button(onClick = {
                         when{
                             tentaikhoan.isEmpty()->{
                                 scope.launch {  snackbarHostState.showSnackbar("Tên đăng nhập không được để trống")}
                             }
                             matkhau.isEmpty()->{
                                 scope.launch { snackbarHostState.showSnackbar("Mật khẩu không được để trống")  }
                             }
                             comfirmatkhau.isEmpty()->{
                                 scope.launch { snackbarHostState.showSnackbar("Nhập lại Mật khẩu không được để trống")  }
                             }
                             hoTen.isEmpty() -> {
                                 scope.launch { snackbarHostState.showSnackbar("Họ tên không được để trống") }
                             }
                             gioiTinh.isEmpty() -> {
                                 scope.launch { snackbarHostState.showSnackbar("Giới tính không được để trống") }
                             }
                             !gioiTinh.equals("Nam", ignoreCase = true) && !gioiTinh.equals("Nữ", ignoreCase = true) -> {
                                 scope.launch { snackbarHostState.showSnackbar("Giới tính phải là Nam hoặc Nữ") }
                             }
                             ngaySinh.isEmpty() -> {
                                 scope.launch { snackbarHostState.showSnackbar("Ngày sinh không được để trống") }
                             }
                             !ngaySinh.matches(Regex("^\\d{4}-\\d{2}-\\d{2}$")) -> {
                                 scope.launch { snackbarHostState.showSnackbar("Ngày sinh phải đúng định dạng YYYY-MM-DD") }
                             }
                             soDienThoai.isEmpty() -> {
                                 scope.launch { snackbarHostState.showSnackbar("Số điện thoại không được để trống") }
                             }
                             !soDienThoai.matches(Regex("^0\\d{9}$")) -> {
                                 scope.launch { snackbarHostState.showSnackbar("Số điện thoại phải bắt đầu bằng số 0 và có 10 chữ số") }
                             }
                             email.isEmpty() -> {
                                 scope.launch { snackbarHostState.showSnackbar("Email không được để trống") }
                             }
                             !email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$")) -> {
                                 scope.launch { snackbarHostState.showSnackbar("Email không đúng định dạng") }
                             }
                             matkhau !=comfirmatkhau->{
                                 scope.launch { snackbarHostState.showSnackbar("Mật khẩu và nhập lại mật khẩu không trùng khớp")  }
                             }
                             tentaikhoan.length>=15->{
                                 scope.launch { snackbarHostState.showSnackbar("Tên đăng nhập quá dài, tối đa 15 ký tự") }
                             }
                             tentaikhoan == matkhau ->{
                                 scope.launch { snackbarHostState.showSnackbar("tên đăng nhập và mật khẩu không được trùng nhau") }
                             }
                             matkhau.contains("1234")->{
                                 scope.launch{ snackbarHostState.showSnackbar("Mật khẩu có ký tự đơn giãn dễ bị phát hiện ")}
                             }
                             !tentaikhoan.any{it.isDigit()}->{
                                 scope.launch { snackbarHostState.showSnackbar("tên tài khoản phải có số") }
                             }
                             tentaikhoan.length<8->{
                                 scope.launch { snackbarHostState.showSnackbar("Tên tài khoản phải nhiều hơn 8 ký tự ") }
                             }
                             !tentaikhoan.any{it.isUpperCase()}->{
                                 scope.launch { snackbarHostState.showSnackbar("Tên tài khoản phải có ký tự viết hoa") }
                             }
                             !tentaikhoan.any{it.isLowerCase()}->{
                                 scope.launch { snackbarHostState.showSnackbar("Tên tài khoản Phải có ký tự thường") }
                             }
                             !matkhau.any { it.isLetterOrDigit() }->{
                                 scope.launch { snackbarHostState.showSnackbar("Mật khẩu phải có ký tự đặc biệt ") }
                             }
                             !matkhau.any { it.isUpperCase() }->{
                                 scope.launch { snackbarHostState.showSnackbar("mật khẩu phải có ký tự viết hoa") }
                             }
                             !matkhau.any { it.isDigit() }->{
                                 scope.launch { snackbarHostState.showSnackbar("Mật khẩu phải có số ") }
                             }
                             matkhau.length<8->{
                                 scope.launch { snackbarHostState.showSnackbar("Mật khẩu phải dài từ 8 ký tự trở lên ") }
                             }

                             else->{
                                 taiKhoanViewModel.kiemTraTrungUsername(tentaikhoan)
                             }
                         }
                    }) {
                        Text("Đăng Ký")
                    }
                }
            }
        }
    }
    LaunchedEffect(checkResult) {
        checkResult?.let {
            if (it.result == false ) {
                snackbarHostState.showSnackbar("Tên tài khoản đã tồn tại")
            } else {
                try {
                    val maKhachHang = LaptopStoreRetrofitClient.khachHangAPIService.taoMaKhachHang().ma_khach_hang

                    val newkhachhang = KhachHang(
                        MaKhachHang = maKhachHang,
                        HoTen = hoTen,
                        GioiTinh = gioiTinh,
                        NgaySinh = ngaySinh,
                        Email = email,
                        SoDienThoai = soDienThoai
                    )

                    val newTaiKhoan = TaiKhoan(
                        TenTaiKhoan = tentaikhoan,
                        MaKhachHang = maKhachHang,
                        MatKhau = comfirmatkhau,
                        LoaiTaiKhoan = 0,
                        TrangThai = 1
                    )

                    // Gửi yêu cầu tạo khách hàng
                    khachHangViewModel.themkhachhang(newkhachhang)

                    // Thu thập kết quả trong coroutine
                    scope.launch {
                        khachHangViewModel.themKhachHangMoiResult.collect{ result: String? ->
                            when (result) {
                                "success" -> {
                                    taiKhoanViewModel.TaoTaiKhoan(newTaiKhoan)
                                    Log.d("Đăng ký", "Tạo tài khoản sau khi KH thành công")
                                    dialogMessage.value = "Đăng ký thành công"
                                    openDialog.value = true
                                    issuccess = true
                                }
                                "fail" -> {
                                    Log.e("Đăng ký", "Tạo khách hàng thất bại từ API")
                                }
                                "error" -> {
                                    Log.e("Đăng ký", "Lỗi khi gọi API tạo khách hàng")
                                }
                                else -> {
                                    Log.e("Đăng ký", "Trạng thái không xác định")
                                }
                            }
                        }
                    }

                } catch (e: Exception) {
                    Log.e("Đăng ký", "Lỗi khi đăng ký: ${e.message}")
                    snackbarHostState.showSnackbar("Đăng ký thất bại: ${e.message}")
                }
            }

            taiKhoanViewModel.resetCheckResult()
        }
    }


    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    openDialog.value = false
                    navController.navigate(Screens.Login_Screens.route)
                }) {
                    Text("OK")
                }
            },
            title = { Text("Thành công") },
            text = { Text("Đăng ký tài khoản thành công!") }
        )
    }

}
