package com.example.laptopstore.RetrofitClient

import ChiTietHoaDonBanAPIService
import HinhAnhAPIService
import SanPhamAPIService
import TaiKhoanAPIService
import com.google.android.gms.common.internal.Constants

object QuanLyBanLaptopRetrofitClient {
    val sanphamAPIService: SanPhamAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(SanPhamAPIService::class.java)
    }

    val hinhAnhAPIService: HinhAnhAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(HinhAnhAPIService::class.java)
    }
    val taiKhoanAPIService: TaiKhoanAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(TaiKhoanAPIService::class.java)
    }

    val khachHangAPIService: KhachHangAPIService = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()
        .create(KhachHangAPIService::class.java)

    val giohangAPIService: GioHangAPIService = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()
        .create(GioHangAPIService::class.java)

    val hoaDonBanAPIService: HoaDonBanAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(HoaDonBanAPIService::class.java)
    }

    val diaChiAPIService: DiaChiAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(DiaChiAPIService::class.java)
    }

    val chiTietHoaDonBanAPIService: ChiTietHoaDonBanAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(ChiTietHoaDonBanAPIService::class.java)
    }