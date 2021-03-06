package com.san.pizzaapp.utils

import com.san.pizzaapp.model.ProductCart

interface CartUpdateListener {

    fun deleteCartItem(cartItemID: Int, position: Int)
    fun updateCart(productCart: ProductCart, count: String, position: Int)

}