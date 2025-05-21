import com.example.laptopstore.models.TaiKhoan
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class TaiKhoanResponse(
    val taikhoan: TaiKhoan?,
    val message: String?
)

data class taikhoanUpdateResponse(
    val success: Boolean,
    val message: String
)

data class KiemTraTaiKhoanResponse(
    val result: Boolean,
    val message: String? = null
)

interface TaiKhoanAPIService{
    @GET("TaiKhoan/checktaikhoan.php")
    suspend fun kiemTraDangNhap(
        @Query("tentaikhoan") tenTaiKhoan: String,
        @Query("matkhau") matKhau: String
    ): KiemTraTaiKhoanResponse

    @GET("TaiKhoan/kiemtratrungusername.php")
    suspend fun kiemTraTrunUsername(
        @Query("tentaikhoan") tenTaiKhoan: String,
    ): KiemTraTaiKhoanResponse

    @GET("TaiKhoan/show.php")
    suspend fun getTaiKhoanByTentaikhoan(
        @Query("tentaikhoan") tentaikhoan: String
    ): TaiKhoan

    @PUT("TaiKhoan/update.php")
    suspend fun updateTaiKhoan(
        @Body taikhoan: TaiKhoan
    ): taikhoanUpdateResponse

    @POST("TaiKhoan/create.php")
    suspend fun TaoTaiKhoan(
        @Body taiKhoan: TaiKhoan,
    ): taikhoanUpdateResponse
}