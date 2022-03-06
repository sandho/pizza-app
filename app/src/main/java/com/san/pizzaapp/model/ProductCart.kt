package com.san.pizzaapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductCart(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "product_name") val productName: String,
    @ColumnInfo(name = "product_crust_id") val productCrustID: String,
    @ColumnInfo(name = "product_crust_name") val productCrustName: String,
    @ColumnInfo(name = "product_crust_size_id") val productCrustSizeID: String,
    @ColumnInfo(name = "product_crust_size_name") val productCrustSizeName: String,
    @ColumnInfo(name = "product_price") val productPrice: String,
    @ColumnInfo(name = "product_cart_count") val productCartCount: String
)
