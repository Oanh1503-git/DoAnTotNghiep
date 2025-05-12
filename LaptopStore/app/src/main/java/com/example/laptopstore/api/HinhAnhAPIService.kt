import com.example.lapstore.models.HinhAnh
import com.example.lapstore.models.SanPham
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

data class HinhAnhResponse(
    val hinhanh: List<HinhAnh>
)


interface HinhAnhAPIService{
    @GET("HinhAnh/readhinhanhbymasanpham.php")
    suspend fun getHinhAnhBySanPham(
        @Query("MaSanPham") MaSanPham: Int
    ): HinhAnhResponse
}