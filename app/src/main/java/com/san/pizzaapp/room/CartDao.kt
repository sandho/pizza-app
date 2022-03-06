package com.san.pizzaapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.san.pizzaapp.model.ProductCart

@Dao
interface CartDao {

    @Insert
    fun addToCart(productCart: ProductCart)

    @Query("SELECT * FROM productcart")
    fun listAllCart(): List<ProductCart>

}