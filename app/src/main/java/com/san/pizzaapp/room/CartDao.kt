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

    @Query("SELECT * FROM productcart WHERE product_crust_id = :crustID")
    fun selectByCrustID(crustID: String): List<ProductCart>

    @Query("SELECT * FROM productcart WHERE product_crust_size_id = :crustSizeID")
    fun selectByCrustSizeID(crustSizeID: String): List<ProductCart>

    @Query("DELETE FROM productcart")
    fun clearCart()

    @Query("DELETE FROM productcart WHERE id = :cartItemID")
    fun cartItemDelete(cartItemID: Int)

    @Query("UPDATE productcart SET product_cart_count = :count WHERE id = :cartItemID")
    fun updateCartCount(count: Int, cartItemID: Int)

}