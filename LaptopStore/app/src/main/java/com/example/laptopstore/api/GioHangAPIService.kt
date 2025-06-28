import com.example.laptopstore.models.GioHang
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class GioHangResponse(
    val giohang: List<GioHang>
)

data class UpdateResponse(
    val success: Boolean,
    val message: String
)

data class ApiResponse(
    val message: String
)

data class  DeleteGioHangRequest(
    val MaKhachHang: String,
    val MaSanPham: Int
)
data class DeleteGioHangResponse(
    val success: Boolean,
    val message: String
)
data class DeleteRequest(
    val MaGioHang: Int
)

data class addtocartResponse(
    val success: Boolean,
    val message: String
)

interface GioHangAPIService {
    @GET("GioHang/getgiohangtheomakhachhang.php")
    suspend fun getGioHangByKhachHang(
        @Query("MaKhachHang") MaKhachHang: String
    ): GioHangResponse

    @PUT("GioHang/update.php")
    suspend fun updateGioHang(
        @Body gioHang: GioHang
    ): UpdateResponse

    @POST("GioHang/delete.php")
    suspend fun deleteGioHang(
        @Body deleteRequest: DeleteRequest
    ): Response<ApiResponse>

    @POST("GioHang/create.php")
    suspend fun addToCart(
        @Body gioHang: GioHang
    ): addtocartResponse

    @POST("GioHang/delete_by_khachhang_sanpham.php")
    suspend fun deleteSanPhamTrongGio(
        @Body request: DeleteGioHangRequest
    ): Response<DeleteGioHangResponse>

}