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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.laptopstore.models.Screens
import com.example.laptopstore.viewmodels.KhachHangViewModels
import com.example.laptopstore.viewmodels.TaiKhoanViewModel
import com.example.laptopstore.viewmodels.TaiKhoanViewsModelsFactory
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController
) {
    var isLoading by remember { mutableStateOf(false) }
    var loadingMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    val activity = context as? Activity
    
    val taiKhoanViewModel: TaiKhoanViewModel = viewModel(
        factory = TaiKhoanViewsModelsFactory(context)
    )
    val taikhoan by taiKhoanViewModel.taikhoan.collectAsState()
    var makhachhang by remember { mutableStateOf<String?> (null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var tendangnhap by remember { mutableStateOf("") }
    var matkhau by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var shouldReload by remember { mutableStateOf(false) }

    val loginResult by taiKhoanViewModel.loginResult.collectAsState()
    val isLoggedIn by taiKhoanViewModel.isLoggedIn.collectAsState()
    var openDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Kiểm tra xem có đến từ màn hình giỏ hàng không
    val isFromCart = remember {
        navController.previousBackStackEntry?.destination?.route == Screens.CARTSCREENS.route
    }

    LaunchedEffect (taikhoan){
        Log.d("LoginScreen", "TaiKhoan State Changed: $taikhoan")
        taikhoan?.MaKhachHang?.let {
            makh ->
            if(makh.isNullOrEmpty()){
                makhachhang=makh
                Log.d("LoginScreen", "MaKhachHang retrieved: $makh")
            }
        }
    }
    // Xử lý reload trang khi đăng nhập thành công
    LaunchedEffect(shouldReload) {
        if (shouldReload) {
            // Reset state
            shouldReload = false
            
            // Điều hướng đến màn hình hiện tại để reload
            if (isFromCart) {
                navController.navigate(Screens.CARTSCREENS.route) {
                    popUpTo(Screens.CARTSCREENS.route) { inclusive = true }
                }
            } else {
                navController.navigate(Screens.ACCOUNTSCREENS.route) {
                    popUpTo(Screens.ACCOUNTSCREENS.route) { inclusive = true }
                }
            }
        }
    }

    // Xử lý kết quả đăng nhập
    LaunchedEffect(loginResult) {
        loginResult?.let { result ->
            if (result.result == true) {
                taiKhoanViewModel.setIsLoggedIn(true)

                // Lấy và lưu MaKhachHang
                taikhoan?.MaKhachHang?.let { makh ->
                    if (makh.isNotEmpty()) {
                        // Lưu vào SharedPreferences
                        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                            .edit()
                            .putString("MaKhachHang", makh)
                            .apply()

                        Log.d("LoginScreen", "Saved MaKhachHang: $makh")
                    }
                }
                activity?.let { act ->
                    val intent = act.packageManager.getLaunchIntentForPackage(act.packageName)
                    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    act.startActivity(intent)
                    act.finish()
                }
                scope.launch {
                    snackbarHostState.showSnackbar("Đăng nhập thành công")
                }

                // Kiểm tra điều hướng dựa trên MaKhachHang
                when {
                    isFromCart -> {
                        navController.navigate(Screens.CARTSCREENS.route) {
                            popUpTo(Screens.Login_Screens.route) { inclusive = true }
                        }
                    }
                    else -> {
                        navController.navigate(Screens.HOMEPAGE.route) {
                            popUpTo(Screens.Login_Screens.route) { inclusive = true }
                        }
                    }
                }

                // Reset login result
                taiKhoanViewModel.resetLoginResult()
            } else {
                errorMessage = "Tài khoản hoặc mật khẩu không chính xác"
                openDialog = true
            }
        }
    }

    // Kiểm tra trạng thái đăng nhập
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            // Trigger reload
            shouldReload = true
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
                title = { Text("Đăng nhập tài khoản") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!isLoggedIn) {
                Text("ĐĂNG NHẬP", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.Red)

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = tendangnhap,
                    onValueChange = { tendangnhap = it },
                    label = { Text("Tên đăng nhập") },
                    shape = RoundedCornerShape(17.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Red,
                        unfocusedBorderColor = Color.Red,
                        focusedLabelColor = Color.Red
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = matkhau,
                    onValueChange = { matkhau = it },
                    label = { Text("Mật khẩu") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    shape = RoundedCornerShape(17.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Red,
                        unfocusedBorderColor = Color.Red,
                        focusedLabelColor = Color.Red
                    ),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        when {
                            tendangnhap.isBlank() -> {
                                errorMessage = "Vui lòng nhập tên đăng nhập"
                                openDialog = true
                            }
                            matkhau.isBlank() -> {
                                errorMessage = "Vui lòng nhập mật khẩu"
                                openDialog = true
                            }
                            else -> {
                                taiKhoanViewModel.kiemTraDangNhap(tendangnhap, matkhau)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .shadow(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    shape = RoundedCornerShape(17.dp)
                ) {
                    Text("ĐĂNG NHẬP", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(50.dp))

                    Row( modifier = Modifier
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically)
                    {
                        Text(
                            "Bạn đã quên mật khẩu ",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = {
                            navController.navigate(Screens.VERIFYOTPSCREENFORGOTPASSWORD.route)
                        }) {
                            Text(
                                "lấy lại mật khẩu",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Blue
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    Row ( modifier = Modifier
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically){
                        Text(
                            "Bạn chưa có tài khoản?",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = {
                            navController.navigate(Screens.REGISTERSCREEN.route)
                        }) {
                            Text(
                                "Đăng ký ngay!",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Blue
                            )
                        }
                    }
                }



            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = Color.Red)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(loadingMessage)
                    }
                }
            }

            if (openDialog) {
                AlertDialog(
                    onDismissRequest = { openDialog = false },
                    text = { Text(errorMessage) },
                    confirmButton = {
                        Button(onClick = { openDialog = false }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}