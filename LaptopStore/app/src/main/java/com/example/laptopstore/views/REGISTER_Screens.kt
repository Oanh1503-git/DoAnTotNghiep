package com.example.laptopstore.views


import android.icu.text.CaseMap.Title
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
import androidx.navigation.NavHostController
import com.example.lapstore.models.KhachHang
import com.example.lapstore.models.TaiKhoan
import com.example.laptopstore.models.Screens
import com.example.laptopstore.viewmodels.KhachHangViewModels
import com.example.laptopstore.viewmodels.TaiKhoanViewModel
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

                Spacer(modifier = Modifier.height(15.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Nhập email") },
                    shape = RoundedCornerShape(17.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Red,
                        unfocusedBorderColor = Color.Red,
                        focusedLabelColor = Color.Red
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

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
                             email.isEmpty()->{
                                 scope.launch { snackbarHostState.showSnackbar("email không được để trống")  }
                             }
                             matkhau !=comfirmatkhau->{
                                 scope.launch { snackbarHostState.showSnackbar("Mật khẩu và nhập lại mật khẩu không trùng khớp")  }
                             }
                             tentaikhoan.length>15->{
                                 scope.launch { snackbarHostState.showSnackbar("Tên đăng nhập quá dài, tối đa 15 ký tự") }
                             }
                             tentaikhoan == matkhau ->{
                                 scope.launch { snackbarHostState.showSnackbar("tên đăng nhập và mật khẩu không được trùng nhau") }
                             }
                             matkhau.contains("1234")->{
                                 scope.launch{ snackbarHostState.showSnackbar("Mật khẩu có ký tự đơn giãn dễ bị phát hiện ")}
                             }
                             !email.contains("@gmail.com")->{
                                 scope.launch { snackbarHostState.showSnackbar("Vui long nhập đúng ý mail") }
                             }
                             else->{
                                 val newkhachhang = KhachHang(
                                     MaKhachHang = 0,
                                     HoTen = tentaikhoan,
                                     GioiTinh = "Nam",
                                     NgaySinh = "",
                                     Email = email,
                                     SoDienThoai = ""
                                 )
                                 khachHangViewModel.themkhachhang(newkhachhang)
                              val newTaiKhoan= TaiKhoan(
                                  TenTaiKhoan = tentaikhoan,
                                  MaKhachHang = 0,
                                  MatKhau = comfirmatkhau,
                                  LoaiTaiKhoan = 0,
                                  TrangThai = 1
                              )
                                 taiKhoanViewModel.TaoTaiKhoan(newTaiKhoan)
                                 dialogMessage.value= " Đăng ký thành công"
                                 openDialog.value=true
                                 issuccess = true
                             }

                         }
                    }) {
                        Text("Đăng Ký")
                    }
                }
            }
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
