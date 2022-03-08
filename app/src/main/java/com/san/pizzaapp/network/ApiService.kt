package com.san.pizzaapp.network

import com.san.pizzaapp.model.Product
import retrofit2.http.GET

interface ApiService {

    @GET("pizzas")
    suspend fun pizzaList(): Product

}