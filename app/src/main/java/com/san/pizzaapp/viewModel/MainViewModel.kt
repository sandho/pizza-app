package com.san.pizzaapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.san.pizzaapp.model.Product
import com.san.pizzaapp.network.Resource
import com.san.pizzaapp.repo.Repository
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: Repository
): ViewModel() {


    private val _pizzaList: MutableLiveData<Resource<Product>> = MutableLiveData()
    val pizzaList: LiveData<Resource<Product>> get() = _pizzaList

    fun pizzaList() = viewModelScope.launch {
        _pizzaList.value = Resource.Loading
        _pizzaList.value = repository.listPizzas()
    }


}