package com.example.laptopstore.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laptopstore.models.SanPham
import com.example.laptopstore.RetrofitClient.LaptopStoreRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SanPhamViewModel : ViewModel() {
    // Chuyển từ mutableStateOf sang StateFlow
    private val _danhSachAllSanPham = MutableStateFlow<List<SanPham>>(emptyList())
    val danhSachAllSanPham: StateFlow<List<SanPham>> get() = _danhSachAllSanPham

    var danhSachSanPhamTrongHoaDon by mutableStateOf<List<SanPham>>(emptyList())

    private val _danhSachSanPhamGaming = MutableStateFlow<List<SanPham>>(emptyList())
    val danhSachSanPhamGaming: StateFlow<List<SanPham>> = _danhSachSanPhamGaming

    private val _danhSachSanPhamVanPhong = MutableStateFlow<List<SanPham>>(emptyList())
    val danhSachSanPhamVanPhong: StateFlow<List<SanPham>> = _danhSachSanPhamVanPhong

    var danhSachSanPhamCuaKhachHang by mutableStateOf<List<SanPham>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    var sanPham by mutableStateOf<SanPham?>(null)
        private set

    var danhSach by mutableStateOf<List<SanPham>>(emptyList())

    private val _danhsachSanPham = MutableStateFlow<List<SanPham>>(emptyList())
    val danhsachSanPham: StateFlow<List<SanPham>> get() = _danhsachSanPham

    fun getAllSanPham() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            errorMessage = null
            try {
                val response = LaptopStoreRetrofitClient.sanphamAPIService.getAllSanPham()
                _danhSachAllSanPham.value = response.sanpham
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
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

    fun getSanPhamTheoGioHang(MaKhachHang: Int) {
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

    fun getSanPhamById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sanPham = LaptopStoreRetrofitClient.sanphamAPIService.getSanPhamById(id)
            } catch (e: Exception) {
                Log.e("SanPhamViewModel", "Error getting SanPham", e)
            }
        }
    }

    fun getSanPhamById2(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val sanPham = LaptopStoreRetrofitClient.sanphamAPIService.getSanPhamById(id)
                _danhsachSanPham.update { currentList -> currentList + sanPham }
            } catch (e: Exception) {
                Log.e("SanPhamViewModel", "Error getting SanPham", e)
            }
        }
    }

    fun getSanPhamSearch(search: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LaptopStoreRetrofitClient.sanphamAPIService.searchSanPham(search)
                }
                danhSach = response.sanpham
            } catch (e: Exception) {
                Log.e("SanPham Error", "Lỗi khi lấy sản phẩm: ${e.message}")
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

    fun clearSanPhamSearch() {
        danhSach = emptyList()
    }
}