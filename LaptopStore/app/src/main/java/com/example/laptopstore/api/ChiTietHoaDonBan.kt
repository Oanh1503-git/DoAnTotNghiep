import com.example.lapstore.models.ChiTietHoaDonBan
import com.example.lapstore.models.HoaDonBan
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


data class addChiTietHoaDonBanResponse(
    val success: Boolean,
    val message: String
)
data class ChiTietHoaDonBanResponse(
    val chitiethoadonban: List<ChiTietHoaDonBan>
)

interface ChiTietHoaDonBanAPIService{
    @POST("ChiTietHoaDonBan/create.php")
    suspend fun addChiTietHoaDonBan(
        @Body chitiethoadonban: ChiTietHoaDonBan
    ): addChiTietHoaDonBanResponse

    @GET("ChiTietHoaDonBan/laychitiethoadontheomahoadon.php")
    suspend fun getChiTietHoaDoByMaHoaDon(
        @Query("MaHoaDonBan") MaHoaDonBan: Int,
    ): ChiTietHoaDonBanResponse
}