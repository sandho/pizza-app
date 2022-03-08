package com.san.pizzaapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.san.pizzaapp.model.ProductCart
import com.san.pizzaapp.room.CartDao
import kotlinx.coroutines.launch

class RoomDbViewModel(
    private val dao: CartDao
): ViewModel() {

    private val listCartResponse: MutableLiveData<List<ProductCart>> = MutableLiveData()
    val listCart: LiveData<List<ProductCart>> get() = listCartResponse

    fun listAllCart() = viewModelScope.launch {
        listCartResponse.value = dao.listAllCart()
    }

}