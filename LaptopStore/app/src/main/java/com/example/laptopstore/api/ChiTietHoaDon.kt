import com.example.lapstore.models.ChiTietHoaDon
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


data class addChiTietHoaDonResponse(
    val success: Boolean,
    val message: String
)
data class ChiTietHoaDonResponse(
    val chitiethoadon: List<ChiTietHoaDon>
)

interface ChiTietHoaDonAPIService{
    @POST("ChiTietHoaDon/create.php")
    suspend fun addChiTietHoaDonBan(
        @Body chitiethoadon: ChiTietHoaDon
    ): addChiTietHoaDonResponse

    @GET("ChiTietHoaDon/laychitiethoadontheomahoadon.php")
    suspend fun getChiTietHoaDoByMaHoaDon(
        @Query("MaHoaDonBan") MaHoaDon: Int,
    ): ChiTietHoaDonResponse
}