import com.example.laptopstore.api.ApiResponse
import com.example.laptopstore.models.HoaDon
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

// ===================
// RESPONSE DATA CLASS
// ===================

data class AddHoaDonResponse(
    val success: Boolean,
    val message: String,
    val maHoaDon: Int? = null
)

data class HoaDonResponse(
    val hoadon: List<HoaDon>?
)

data class HoaDonDeleteRequest(
    val MaHoaDon: Int
)

data class HoaDonUpdateTrangThaiRequest(
    val MaHoaDon: Int,
    val TrangThai: Int
)

// =======================
// DON HANG DAY DU RESPONSE
// =======================

data class DonHangDayDuResponse(
    val message: String,
    val success: Boolean,
    val data: List<DonHangDayDuItem>
)

data class DonHangDayDuItem(
    val MaHoaDon: Int,
    val NgayDatHang: String,
    val TongTien: String,
    val TrangThai: String,
    val PhuongThucThanhToan: String,
    val SanPham: List<SanPhamItem>
)

data class SanPhamItem(
    val MaSanPham: Int,
    val TenSanPham: String,
    val Gia: Int,
    val SoLuong: String,
    val ThanhTien: String,
    val HinhAnh: String
)

// ===================
// INTERFACE API SERVICE
// ===================

interface HoaDonAPIService {

    @POST("HoaDon/create.php")
    suspend fun addHoaDon(@Body hoaDon: HoaDon): AddHoaDonResponse

    @GET("HoaDon/getHoaDonBanByKhachHang.php")
    suspend fun getHoaDonByKhachHang(
        @Query("MaKhachHang") MaKhachHang: String,
        @Query("TrangThai") TrangThai: Int
    ): HoaDonResponse

    @POST("HoaDon/delete.php")
    suspend fun deleteHoaDon(
        @Body deleteRequest: HoaDonDeleteRequest
    ): Response<ApiResponse>

    @PUT("HoaDon/update.php")
    suspend fun updateHoaDon(
        @Body hoaDon: HoaDon
    ): AddHoaDonResponse

    @GET("HoaDon/show.php")
    suspend fun getHoaDonByMaHoaDon(
        @Query("MaHoaDon") MaHoaDon: Int
    ): HoaDon

    @GET("HoaDon/layhoadontheotrangthai.php")
    suspend fun getHoaDonTheoTrangThai(
        @Query("TrangThai") TrangThai: Int
    ): HoaDonResponse

    @GET("HoaDon/get_donhang_daydu.php")
    suspend fun getDonHangDayDuTheoKhachHang(
        @Query("MaKhachHang") maKhachHang: String
    ): Response<DonHangDayDuResponse>

    @PUT("HoaDon/updatetrangthai.php")
    suspend fun updateTrangThai(
        @Body request: HoaDonUpdateTrangThaiRequest
    ): ApiResponse
}
