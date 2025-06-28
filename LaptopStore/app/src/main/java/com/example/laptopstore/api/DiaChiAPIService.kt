
import com.example.laptopstore.api.ApiResponse
import com.example.laptopstore.models.DiaChi
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

    data class DiaChiResponse(
        val diachi: List<DiaChi>? = null,
        val message: String? = null
    )


data class addDiaChiResponse(
    val success: Boolean,
    val message: String
)

data class DeleteDiaChiRequest(
    val MaDiaChi: Int
)
data class UpdateMacDinhRequest(
    val MaKhachHang: String,
    val MaDiaChi: Int
)

interface DiaChiAPIService{
    @GET("DiaChi/show.php")
    suspend fun getDiaChiByMaDiaChi(
        @Query("id") MaDiaChi: Int
    ): DiaChi

    @GET("DiaChi/laydiachimacdinh.php")
    suspend fun getDiaChiMacDinh(
        @Query("MaKhachHang") MaKhachHang: String,
        @Query("MacDinh") MacDinh: Int
    ): DiaChi

    @GET("DiaChi/getdiachibykhachhang.php")
    suspend fun getDiaChiByMaKhachHang(
        @Query("MaKhachHang") MaKhachHang: String?
    ): DiaChiResponse

    @POST("DiaChi/create.php")
    suspend fun addDiaChi(
        @Body diachi: DiaChi
    ): addDiaChiResponse

    suspend fun updateDiaChiMacDinh(@Body request: UpdateMacDinhRequest): addDiaChiResponse
    @PUT("DiaChi/update.php")
    suspend fun updateDiaChi(
        @Body diachi: DiaChi
    ): addDiaChiResponse
    @POST("DiaChi/delete.php")
    suspend fun deleteDiaChi(
        @Body MaDiaChi: DeleteDiaChiRequest
    ): Response<ApiResponse>

    @POST("DiaChi/set_mac_dinh.php")
    suspend fun setdiachimacdich(
        @Body diachi: DiaChi
    ): DiaChiResponse

}