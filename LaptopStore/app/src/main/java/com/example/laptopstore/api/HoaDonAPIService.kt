
import com.example.laptopstore.api.ApiResponse
import com.example.laptopstore.models.HoaDon
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class AddHoaDonResponse(
    val success: Boolean,
    val message: String,
    val maHoaDon: Int? = null
)
data class MaHoaDonApiResponse(
    val success: Boolean,
    val ma_hoa_don: Int?,      // Trả về null nếu không có mã hóa đơn
    val message: String? = null
)

data class HoaDonResponse(
    val hoadon: List<HoaDon>?
)

data class MaHoaDonResponse(
    val MaHoaDon: Int
)

data class HoaDonDeleteRequest(
    val MaHoaDon: Int
)
data class DonHangDayDuResponse(
    val MaHoaDon: Int,
    val NgayDatHang: String,
    val TongTien: Int,
    val TrangThai: String,
    val PhuongThucThanhToan: String,
    val SanPham: List<SanPhamDonHang>
)

data class SanPhamDonHang(
    val TenSanPham: String,
    val Gia: Int,
    val SoLuong: Int,
    val ThanhTien: Int,
    val HinhAnh: String?
)
data class HoaDonUpdateTrangThaiRequest(
    val MaHoaDon: Int,
    val TrangThai: Int
)

interface HoaDonAPIService{
    @POST("HoaDon/create.php")
    suspend fun addHoaDon(@Body hoaDon: HoaDon): AddHoaDonResponse

    @GET("HoaDon/getHoaDonBanByKhachHang.php")
    suspend fun getHoaDoByKhachHang(
        @Query("MaKhachHang") MaKhachHang: String,
        @Query("TrangThai") TrangThai: Int
    ): HoaDonResponse


    @POST("HoaDon/delete.php")
    suspend fun deleteHoaDon(
        @Body deleteRequest: HoaDonDeleteRequest
    ): Response<ApiResponse>

    @PUT("HoaDon/update.php")
    suspend fun updateHoaDon(
        @Body hoadon: HoaDon
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
    ): Response<List<DonHangDayDuResponse>>

    @PUT("HoaDon/updatetrangthai.php")
    suspend fun updateTrangThai(
        @Body request: HoaDonUpdateTrangThaiRequest
    ): ApiResponse

}