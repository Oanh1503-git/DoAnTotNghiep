import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapstore.models.HinhAnh
import com.example.laptopstore.RetrofitClient.LaptopStoreRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch/**/
import kotlinx.coroutines.withContext

class HinhAnhViewModel : ViewModel() {
    var danhsachhinhanhtheosanpham by mutableStateOf<List<HinhAnh>>(emptyList())

    fun getHinhAnhTheoSanPham(MaSanPham: Int) {
        viewModelScope.launch {
            try {
                // Thực hiện API request trong Dispatchers.IO
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.hinhAnhAPIService.getHinhAnhBySanPham(MaSanPham)
                }
                // Cập nhật dữ liệu lên UI (trong Dispatchers.Main)
                danhsachhinhanhtheosanpham = response.hinhanh
            } catch (e: Exception) {
                // Xử lý lỗi nếu có
                Log.e("HinhAnhError", "Lỗi khi lấy hình ảnh: ${e.message}")
            }
        }
    }
}

