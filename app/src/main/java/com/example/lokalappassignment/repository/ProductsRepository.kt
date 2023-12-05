package com.example.lokalappassignment.repository

import com.example.lokalappassignment.data.ProductsData
import com.example.lokalappassignment.retrofit.ProductsAPI
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import javax.inject.Inject

class ProductsRepository @Inject constructor(private val productsAPI: ProductsAPI){
    fun getAllProducts(): Call<ProductsData> = productsAPI.getAllProducts()
}