package com.example.lokalappassignment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lokalappassignment.data.ProductsData
import com.example.lokalappassignment.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val productsRepository: ProductsRepository): ViewModel() {
    val allProducts = MutableLiveData<ProductsData>()
    fun getAllProducts(){
        productsRepository.getAllProducts().enqueue(object: Callback<ProductsData>{
            override fun onResponse(call: Call<ProductsData>, response: Response<ProductsData>) {
                if(response.isSuccessful) allProducts.postValue(response.body())
            }

            override fun onFailure(call: Call<ProductsData>, t: Throwable) {
            }

        })
    }
}