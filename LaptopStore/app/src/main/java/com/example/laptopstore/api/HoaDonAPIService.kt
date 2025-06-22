
import com.example.laptopstore.models.HoaDon
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class addHoaDonResponse(
    val success: Boolean,
    val message: String,
    val maHoaDon: Int?
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
interface HoaDonAPIService{
    @POST("HoaDon/create.php")
    suspend fun addHoaDon(
        @Body hoadon: HoaDon
    ): addHoaDonResponse

    @GET("HoaDon/getHoaDonByKhachHang.php")
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
    ): addHoaDonResponse

    @GET("HoaDon/show.php")
    suspend fun getHoaDonByMaHoaDon(
        @Query("MaHoaDon") MaHoaDon: Int
    ): HoaDon

    @GET("HoaDon/layhoadontheotrangthai.php")
    suspend fun getHoaDonTheoTrangThai(
        @Query("TrangThai") TrangThai: Int
    ): HoaDonResponse
}