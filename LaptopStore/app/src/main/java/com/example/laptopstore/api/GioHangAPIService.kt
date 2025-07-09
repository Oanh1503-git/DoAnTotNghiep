import com.example.laptopstore.api.ApiResponse
import com.example.laptopstore.api.SuccessResponse
import com.example.laptopstore.api.UpdateResponse
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
data class SoLuongResponse(
    val SoLuong: Int
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
    ): SuccessResponse

    @POST("GioHang/delete_by_khachhang_sanpham.php")
    suspend fun deleteSanPhamTrongGio(
        @Body request: DeleteGioHangRequest
    ): Response<DeleteGioHangResponse>
    @GET("giohang/get_soluong_giohang.php")
    suspend fun getSoLuongTrongGio(
        @Query("MaKhachHang") maKhachHang: String,
        @Query("MaSanPham") maSanPham: Int
    ): Response<SoLuongResponse>
}