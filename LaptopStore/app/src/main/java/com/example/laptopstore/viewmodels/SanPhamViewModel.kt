package com.example.laptopstore.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laptopstore.models.SanPham
import com.example.laptopstore.RetrofitClient.LaptopStoreRetrofitClient
import com.example.laptopstore.api.TruSoLuongRequest
import com.example.laptopstore.api.TruSoLuongTonKhoRequest
import com.example.laptopstore.api.TruSoLuongTonKhoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SanPhamViewModel : ViewModel() {
    // Chuyển từ mutableStateOf sang StateFlow
    var danhSachSanPhamTrongHoaDon by mutableStateOf<List<SanPham>>(emptyList())

    private val _danhSachSanPhamGaming = MutableStateFlow<List<SanPham>>(emptyList())
    val danhSachSanPhamGaming: StateFlow<List<SanPham>> = _danhSachSanPhamGaming

    private val _danhSachSanPhamVanPhong = MutableStateFlow<List<SanPham>>(emptyList())
    val danhSachSanPhamVanPhong: StateFlow<List<SanPham>> = _danhSachSanPhamVanPhong

    var danhSachSanPhamCuaKhachHang by mutableStateOf<List<SanPham>>(emptyList())
        private set


    private val _soLuongTonKhoState = MutableStateFlow<Int?>(null)
    val soLuongTonKhoState: StateFlow<Int?> get() = _soLuongTonKhoState

    var sanPham by mutableStateOf<SanPham?>(null)
        private set
    var soLuong by mutableStateOf<Int?>(null)
        private set
    private val _trangThaiTruSoLuong = MutableStateFlow<TruSoLuongTonKhoResponse?>(null)
    val trangThaiTruSoLuong: StateFlow<TruSoLuongTonKhoResponse?> get() = _trangThaiTruSoLuong



    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    private val _danhsachSanPham = MutableStateFlow<List<SanPham>>(emptyList())
    val danhsachSanPham: StateFlow<List<SanPham>> get() = _danhsachSanPham

    private val _danhSachAllSanPham = MutableStateFlow<List<SanPham>>(emptyList())
    val danhSachAllSanPham: StateFlow<List<SanPham>> get() = _danhSachAllSanPham

    private val _danhSach = MutableStateFlow<List<SanPham>?>(null)
    val danhSach: StateFlow<List<SanPham>?> get() = _danhSach

    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        getAllSanPham() // Tải dữ liệu ngay khi ViewModel được khởi tạo
    }

    fun getAllSanPham() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            errorMessage = null
            try {
                val response = LaptopStoreRetrofitClient.sanphamAPIService.getAllSanPham()
                _danhSachAllSanPham.value = response.sanpham
                Log.d("SanPhamViewModel", "Loaded all products: ${response.sanpham}")
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("SanPhamViewModel", "Error loading products: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun getSanPhamSearch(search: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.sanphamAPIService.searchSanPham(search)
                }
                _danhSach.value = response.sanpham
                Log.d("SanPhamViewModel", "Search results for '$search': ${response.sanpham}")
            } catch (e: Exception) {
                Log.e("SanPham Error", "Lỗi khi tìm kiếm sản phẩm: ${e.message}")
                errorMessage = e.message
            }
        }
    }
    fun getSanPhamById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            errorMessage = null
            try {
                sanPham = LaptopStoreRetrofitClient.sanphamAPIService.getSanPhamById(id)
            } catch (e: Exception) {
                errorMessage = "Lỗi khi lấy sản phẩm: ${e.message}"
                Log.e("SanPhamViewModel", "Error getting SanPham", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun clearSanPhamSearch() {
        _danhSach.value = emptyList()
    }

    fun updateFilteredProducts(filteredList: List<SanPham>) {
        _danhSach.value = filteredList
    }

    fun getSanPhamTheoLoaiGaming() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.sanphamAPIService.getSanPhamTheoLoai(2)
                }
                _danhSachSanPhamGaming.value = response.sanpham ?: emptyList()
            } catch (e: Exception) {
                Log.e("SanPham Error", "Lỗi khi lấy sanpham: ${e.message}")
                _danhSachSanPhamGaming.value = emptyList()
            }
        }
    }



    fun clearTrangThai() {
        _trangThaiTruSoLuong.value = null
        _error.value = null
    }
    fun getSanPhamTheoLoaiVanPhong() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.sanphamAPIService.getSanPhamTheoLoai(1)
                }
                _danhSachSanPhamVanPhong.value = response.sanpham ?: emptyList()
            } catch (e: Exception) {
                Log.e("SanPham Error", "Lỗi khi lấy sanpham: ${e.message}")
                _danhSachSanPhamVanPhong.value = emptyList()
            }
        }
    }

    fun getSanPhamTheoGioHang(MaKhachHang: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.sanphamAPIService.getSanPhamByGioHang(MaKhachHang)
                }
                danhSachSanPhamCuaKhachHang = response.sanpham
            } catch (e: Exception) {
                Log.e("SanPham Error", "Lỗi khi lấy sản phẩm: ${e.message}")
            }
        }
    }

    fun kiemTraSoLuongSanPham(maSanPham: Int) {
        viewModelScope.launch {
            try {
                val response = LaptopStoreRetrofitClient.sanphamAPIService.kiemTraSoLuongTonKho(maSanPham)
                if (response.success) {
                    val tonKho = response.SoLuongTonKho ?: 0
                    _soLuongTonKhoState.value = tonKho
                    Log.d("TonKho", "✅ Số lượng tồn kho: $tonKho")
                } else {
                    Log.w("TonKho", "⚠️ Lỗi logic API: ${response.message}")
                    _soLuongTonKhoState.value = null
                }
            } catch (e: Exception) {
                Log.e("TonKho", "❌ Lỗi mạng: ${e.message}")
                _soLuongTonKhoState.value = null
            }
        }
    }
    fun truSoLuongTrongKho(maSanPham: Int, soLuongCanTru: Int) {
        viewModelScope.launch {
            try {
                val response = LaptopStoreRetrofitClient.sanphamAPIService.truSoLuongTrongKho( TruSoLuongRequest(maSanPham, soLuongCanTru))

                if (response.isSuccessful && response.body()?.success == true) {
                    // Thành công, có thể cập nhật UI hoặc thông báo
                } else {
                    // Xử lý lỗi
                }
            } catch (e: Exception) {
                // Xử lý lỗi mạng hoặc exception
            }
        }
    }

    fun getSanPhamTrongHoaDon(MaHoaDonBan: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.sanphamAPIService.getSanPhamTheoHoaDon(MaHoaDonBan)
                }
                danhSachSanPhamTrongHoaDon = response.sanpham
            } catch (e: Exception) {
                Log.e("Sản Phẩm Error", "Lỗi khi lấy Sản Phẩm")
            }
        }
    }
    fun checkProductAndCart(maKhachHang: String, maSanPham: Int) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = LaptopStoreRetrofitClient.sanphamAPIService.kiemTraSoLuong(maKhachHang, maSanPham)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success") {
                        soLuong = body.soLuongKho
                        Log.d("SanPhamViewModel", "Số lượng kho: ${body.soLuongKho}")
                    } else {
                        errorMessage = body?.message ?: "Có lỗi xảy ra"
                        Log.e("SanPhamViewModel", "Lỗi từ API: $errorMessage")
                    }
                } else {
                    errorMessage = "Lỗi server: ${response.code()}"
                    Log.e("SanPhamViewModel", "Response error: $errorMessage")
                }
            } catch (e: Exception) {
                errorMessage = "Lỗi kết nối: ${e.localizedMessage}"
                Log.e("SanPhamViewModel", "Exception: $errorMessage")
            } finally {
                isLoading = false
            }
        }
    }


}