package com.example.laptopstore.api

import retrofit2.http.GET
import retrofit2.http.Path

data class Province(
    val code: Int,
    val name: String,
    val districts: List<District>?
)

data class District(
    val code: Int,
    val name: String,
    val wards: List<Ward>?
)

data class Ward(
    val code: Int,
    val name: String
)

interface ProvinceApiService {
    @GET("p")
    suspend fun getAllProvinces(): List<Province>

    @GET("p/{province_id}?depth=2")
    suspend fun getProvinceWithDistricts(@Path("province_id") provinceId: Int): Province

    @GET("d/{district_id}?depth=2")
    suspend fun getDistrictWithWards(@Path("district_id") districtId: Int): District

}