package com.san.pizzaapp.repo

import com.san.pizzaapp.network.ApiService
import com.san.pizzaapp.network.SafeApiCall

class Repository constructor(
    private val apiService: ApiService
): SafeApiCall {

    suspend fun listPizzas() = safeApiCall {
        apiService.pizzaList()
    }

}