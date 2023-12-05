package com.example.lokalappassignment.retrofit

import com.example.lokalappassignment.data.ProductsData
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.http.GET

interface ProductsAPI {

    @GET("products")
    fun getAllProducts(): Call<ProductsData>
}