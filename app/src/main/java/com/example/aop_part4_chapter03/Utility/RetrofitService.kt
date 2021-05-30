package com.example.aop_part4_chapter03.Utility

import retrofit2.Call
import retrofit2.http.GET

interface RetrofitService {

    @GET("tmap/pois")
    fun getPois() : Call<PoisDTO>
}