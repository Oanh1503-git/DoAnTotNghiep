package com.example.laptopstore.RetrofitClient

import ChiTietHoaDonAPIService
import DiaChiAPIService
import GioHangAPIService
import com.example.laptopstore.api.HinhAnhAPIService
import HoaDonAPIService
import com.example.laptopstore.api.SanPhamAPIService
import TaiKhoanAPIService
import com.example.laptopstore.api.BinhLuanAPIService
import com.example.laptopstore.api.KhachHangAPIService
import com.example.laptopstore.api.OtpAPIService
import com.example.laptopstore.api.SanPhamYeuThichAPIService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Constants {
    const val BASE_URL = "https://laptopshop2025.io.vn/lap_store_api/api/"
}

object LaptopStoreRetrofitClient {

    private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()


    val sanphamAPIService: SanPhamAPIService by lazy {
        retrofit.create(SanPhamAPIService::class.java)
    }

    val hinhAnhAPIService: HinhAnhAPIService by lazy {
        retrofit.create(HinhAnhAPIService::class.java)
    }

    val taiKhoanAPIService: TaiKhoanAPIService by lazy {
        retrofit.create(TaiKhoanAPIService::class.java)
    }

    val khachHangAPIService: KhachHangAPIService by lazy {
        retrofit.create(KhachHangAPIService::class.java)
    }

    val giohangAPIService: GioHangAPIService by lazy {
        retrofit.create(GioHangAPIService::class.java)
    }

    val hoaDonAPIService: HoaDonAPIService by lazy {
        retrofit.create(HoaDonAPIService::class.java)
    }

    val diaChiAPIService: DiaChiAPIService by lazy {
        retrofit.create(DiaChiAPIService::class.java)
    }

    val chiTietHoaDonAPIService: ChiTietHoaDonAPIService by lazy {
        retrofit.create(ChiTietHoaDonAPIService::class.java)
    }
    val binhLuanAPIService: BinhLuanAPIService by lazy {
        retrofit.create(BinhLuanAPIService::class.java)
    }

    val sanPhamYeuThichAPIService: SanPhamYeuThichAPIService by lazy {
        retrofit.create(SanPhamYeuThichAPIService::class.java)
    }
    val otpAPIService :OtpAPIService by lazy {
        retrofit.create(OtpAPIService::class.java)
    }
}
