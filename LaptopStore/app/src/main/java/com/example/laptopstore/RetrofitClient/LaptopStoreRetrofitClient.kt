package com.example.laptopstore.RetrofitClient

import ChiTietHoaDonBanAPIService
import DiaChiAPIService
import GioHangAPIService
import com.example.laptopstore.api.HinhAnhAPIService
import HoaDonBanAPIService
import com.example.laptopstore.api.SanPhamAPIService
import TaiKhoanAPIService
import com.example.laptopstore.api.BinhLuanAPIService
import com.example.laptopstore.api.KhachHangAPIService
import com.example.laptopstore.api.SanPhamYeuThichAPIService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Constants {
    const val BASE_URL = "http://10.0.2.2/lap_store_api/api/"  // hoặc địa chỉ XAMPP của bạn
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

    val hoaDonBanAPIService: HoaDonBanAPIService by lazy {
        retrofit.create(HoaDonBanAPIService::class.java)
    }

    val diaChiAPIService: DiaChiAPIService by lazy {
        retrofit.create(DiaChiAPIService::class.java)
    }

    val chiTietHoaDonBanAPIService: ChiTietHoaDonBanAPIService by lazy {
        retrofit.create(ChiTietHoaDonBanAPIService::class.java)
    }
    val binhLuanAPIService: BinhLuanAPIService by lazy {
        retrofit.create(BinhLuanAPIService::class.java)
    }

    val sanPhamYeuThichAPIService: SanPhamYeuThichAPIService by lazy {
        retrofit.create(SanPhamYeuThichAPIService::class.java)
    }
}
